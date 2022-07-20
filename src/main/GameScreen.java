package main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JPanel;
import game.Environment;
import game.Game;
import graphics.Item;
import graphics.sprite.PlayerSprite;
import graphics.tile.*;
import socket.Client;
import socket.request.LeaveRequest;
import socket.request.MoveRequest;

public class GameScreen extends JFrame implements Runnable
{
	private static final long serialVersionUID = 1L;
	Client network;
	int index;
	JPanel panel = new JPanel();
	public boolean running;
	public Game game;
	public PlayerSprite you;
	private static double ups = 60.0; // the amount of updates we want to do in a second
	public final static int mapWidthVisibleOnScreen = 500; // no matter what the screenwidth is, it represents 500 pixels of the map (on zoomlevel 1 ofc)
	private int xFrame, yFrame;
	
	public GameScreen(Client network, int index) {
		this.network = network;
		this.index = index;
		
		setSize(new Dimension(600,300));
		setContentPane(panel);
		panel.setLayout(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		addListeners();
		checkIfGameReady();
	}
	
	public void checkIfGameReady() {
		while (true) {
			game = network.getGame();
			if (!game.loading) {
				running = true;
				beginAnimation();
				update();
				Thread thread = new Thread(this); thread.start(); // will call a new thread doing the run function (start the game)
				System.out.println("game started");
				break;
			}
		}
	}
	
	public void beginAnimation() {
		// nth in here yet
	}
	
	public void run() {
		long lastTime = System.nanoTime(); long timer = System.currentTimeMillis();
		double delta = 0;
		int frames = 0, updates = 0;
		
		while(running) {
			long now = System.nanoTime();
			delta += ((now-lastTime) / 1e9) * ups; // (now-lastTime)/1e9 is the time difference in seconds
			lastTime = now; // reset the last time
			
			if (delta >= 1) {update(); updates++; delta--;}
			render(); frames++;
			
			if (System.currentTimeMillis() - timer > 1000) // show fps and ups
			{ 
				setTitle(updates + " ups, " + frames + " fps");
				timer += 1000; updates = 0; frames = 0;
				System.out.println("your position: " + you.x + ", " + you.y);
			}
		}
	}
	
	public void update() {
		game = network.getGame();
		you = game.getPlayerSpriteList().get(index); // the sprite were controlling
		
		network.send(new MoveRequest(xFrame, yFrame, you.id_ev)); // move your sprite to the left
		xFrame = 0; yFrame = 0; // every frame update how much we moved
	}
	
	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) // only when the bufferstrategy is not created yet, getBufferStrategy() will return null
		{
			createBufferStrategy(3); // always use 3
			return; // terminate func
		}
		Graphics g = bs.getDrawGraphics();
		super.paint(g);

		int xVis = you.x + you.width/2; int yVis = you.y + you.height/2; // visualize from the middle of our sprite
		visualizeMap(xVis, yVis, 1, new GrassTile(), g);
		g.dispose();
		bs.show();
	}
	
	private void renderBackground(int x, int y, double zoomlevel, Tile bg, Graphics g) {
		int screenWidth = getWidth(); int screenHeight = getHeight(); // the width and the height of the screen

		int xOffset = x % Tile.tileSize; int yOffset = y % Tile.tileSize; 
		int relxOffset = mapLengthToScreenLength(xOffset, zoomlevel);
		int relyOffset = mapLengthToScreenLength(yOffset, zoomlevel);
		
		// for ex (x = 324, ts = 100). 
		// If we were at 300 then the midX of the middle tile on the screen would be on the middle of the screen. 
		// However becuz were 24 shifted to the right (on the map)
		// the midX of the middle tile on the screen will be shifted 24 to the left (on the screen)
		int relTileWidth = mapLengthToScreenLength(bg.width, zoomlevel); // the width of the tile ON THE SCREEN
		int relTileHeight = mapLengthToScreenLength(bg.height, zoomlevel); // the height of the tile ON THE SCREEN
		for (int i = 0; i < (screenWidth/relTileWidth) + 2; i++) {
			for (int j = 0; j < (screenHeight/relTileHeight) + 2; j++) {
				int xScreen =  relTileWidth * i - relxOffset;
				int yScreen =  relTileHeight * j - relyOffset;
				BufferedImage img = bg.getImage();
				g.drawImage(img, xScreen, yScreen, relTileWidth, relTileHeight, null);
			}
		}
	}

	public void visualizeMap(int x, int y, double zoomlevel, Tile bg, Graphics g) {
		/* (x, y) is the coordinate on the map we want to visualize (on the screen the middle will be (x, y)
		 * zoomlevel indicates how much of the map around us we can see.
		 * bg is the tile we want that is on the background. if no background bg = null
		 * g is just a Graphics instance with which we can draw on the screen
		 */
		if (bg != null) renderBackground(x, y, zoomlevel, new GrassTile(), g);
		
		//length_on_screen = length_on_map * widthRatio (* zoomlevel)
		int screenWidth = getWidth(); int screenHeight = getHeight(); // the width and the height of the screen
		int xMiddleScreen = screenWidth / 2; int yMiddleScreen = screenHeight / 2;
		
		// how many pixels on the map we can see on our screen
		int widthVisible = (int)((mapWidthVisibleOnScreen / 2) / zoomlevel); // the position we visualize from is in the middle 
		int heightVisible = screenLengthToMapLength(screenHeight / 2, zoomlevel); 
		
		Environment e = game.ev;
		for (Item i: e.objectlist) {
			int dx = i.x - x;
			int dy = i.y - y;
			if (dx <= widthVisible && dy <= heightVisible) {
				int relWidth = mapLengthToScreenLength(i.width, zoomlevel); // the width of the item ON THE SCREEN
				int relHeight = mapLengthToScreenLength(i.height, zoomlevel); // the height of the item ON THE SCREEN
				int relDistanceX = mapLengthToScreenLength(dx, zoomlevel); // the x distance between the pos we visualize from and the item ON THE SCREEN
				int relDistanceY = mapLengthToScreenLength(dy, zoomlevel); // the x distance between the pos we visualize from and the item ON THE SCREEN
				g.drawImage(i.getImage(), xMiddleScreen + relDistanceX, yMiddleScreen + relDistanceY, relWidth, relHeight, null);
				// handle playersprites username
				if (i instanceof PlayerSprite) {
					PlayerSprite ps = (PlayerSprite)i;
					String username = game.players.get(ps.index).username;
					printText(username, xMiddleScreen + relDistanceX + i.width/2, yMiddleScreen + relDistanceY, g, 30f);
				}
			}
		}
	}
	
	public void printText(String s, int x, int y, Graphics g, float size) {
		g.setFont(g.getFont().deriveFont(size));
		int stringLen = (int)g.getFontMetrics().getStringBounds(s, g).getWidth();
		g.drawString(s, x - stringLen/2, y);
	}
	
	public int mapLengthToScreenLength(int distanceMap, double zoomlevel) {
		/* if 500 (mapWidthVisibleOnScreen) pixels on the map cover 1350 (screenWidth) pixels on the screen
		   how many pixels do x pixels on the map cover on the screen? x * (1350/500) 
		   so the formula is:
		   length_on_screen = (length_on_map * widthRatio) * zoomlevel 
		*/
		double widthRatio = (double)getWidth() / (double)mapWidthVisibleOnScreen; 
		int distanceScreen =  (int)(distanceMap * widthRatio * zoomlevel);
		return distanceScreen;
	}
	
	public int screenLengthToMapLength(int distanceScreen, double zoomlevel) {
		/* length_on_screen = length_on_map * widthRatio * zoomlevel ->
		 length_on_map = length_on_screen / (widthRatio * zoomlevel) */
		
		double widthRatio = (double)getWidth() / (double)mapWidthVisibleOnScreen; 
		int distanceMap =  (int)(distanceScreen / (widthRatio * zoomlevel));
		return distanceMap;
	}
	
	
	private void addListeners()
	{
		this.addWindowListener(new WindowListener()
		{
			public void windowClosing(WindowEvent e) // when we close the window this code will run
			{
				network.send(new LeaveRequest()); // tell the server we want to leave
            }
			public void windowOpened(WindowEvent e) {} public void windowClosed(WindowEvent e) {}
			public void windowIconified(WindowEvent e) {} public void windowDeiconified(WindowEvent e) {}
			public void windowActivated(WindowEvent e) {} public void windowDeactivated(WindowEvent e) {}
		});
		this.addKeyListener(new KeyListener() 
		{
			public void keyTyped(KeyEvent e) {}
			public void keyPressed(KeyEvent e) 
			{
				switch (e.getKeyCode()) {
					case 37: // left-arrow
						xFrame -= you.speed;
						break;
					case 38: // up-arrow
						yFrame -= you.speed;
						break;
			        case 39: // right-arrow
			        	xFrame += you.speed;
			        	break;
			        case 40: // down-arrow
			        	yFrame += you.speed;
			        	break;
				 }
			}
			public void keyReleased(KeyEvent e) {}
		});
	}
}
