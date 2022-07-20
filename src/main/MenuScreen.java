package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent; import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.border.*;
import game.Game; import game.mode.ClassicMode;
import socket.Client;
import socket.request.*;

public class MenuScreen extends JFrame implements Runnable
{
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	public JButton btnPlay;
	public JLabel lblInfo;
	public boolean searching = false;
	public Client network;
	private JFormattedTextField tfUsername;
	private Component verticalStrut_1;
	private JLabel lblNewLabel;
	private Thread thread;
	private int index;

	public MenuScreen() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		
		addComponents(); // making the menuscreen
		addListeners(); // telling the ms what to do when "play" is being clicked
		setVisible(true);
	}
	
	private void addComponents(){
		JLabel lblWelcome = new JLabel("Welcome to project find money");
		lblWelcome.setForeground(new Color(0, 100, 0));
		lblWelcome.setFont(new Font("VAGRounded BT", Font.PLAIN, 27));
		lblWelcome.setHorizontalAlignment(SwingConstants.CENTER);
		lblWelcome.setAlignmentX(0.5f);
		contentPane.add(lblWelcome);
		
		Component verticalStrut = Box.createVerticalStrut(70);
		verticalStrut.setMaximumSize(new Dimension(32767, 40));
		verticalStrut.setPreferredSize(new Dimension(0, 50));
		contentPane.add(verticalStrut);
		
		lblNewLabel = new JLabel("Username:");
		lblNewLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblNewLabel.setFont(new Font("VAGRounded BT", Font.PLAIN, 14));
		contentPane.add(lblNewLabel);
		
		tfUsername = new JFormattedTextField();
		tfUsername.setFont(new Font("VAGRounded BT", Font.PLAIN, 26));
		tfUsername.setMaximumSize(new Dimension(2147483647, 50));
		tfUsername.setPreferredSize(new Dimension(7, 5));
		tfUsername.setSize(new Dimension(0, 50));
		tfUsername.setName("tfUsername");
		contentPane.add(tfUsername);
		
		verticalStrut_1 = Box.createVerticalStrut(70);
		verticalStrut_1.setPreferredSize(new Dimension(0, 50));
		verticalStrut_1.setMaximumSize(new Dimension(32767, 25));
		contentPane.add(verticalStrut_1);
		
		btnPlay = new JButton("PLAY");
		btnPlay.setBorder(new MatteBorder(1, 1, 1, 1, new Color(0, 0, 0)));
		btnPlay.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnPlay.setFont(new Font("Square721 BT", Font.PLAIN, 38));
		btnPlay.setBackground(Color.WHITE);
		btnPlay.setForeground(new Color(255, 165, 0));
		btnPlay.setAlignmentX(0.5f);
		contentPane.add(btnPlay);
	}
	
	private void addListeners()
	{
		btnPlay.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) // this func will run when the play/cancel button is fired
			{
				if (!searching) searchGame();
				else cancelSearchGame();
			}
		});
		this.addWindowListener(new WindowListener()
		{
			public void windowClosing(WindowEvent e) // when we close the window this code will run
			{
				if (searching) // if were not searching then this would be pointless
				network.send(new LeaveRequest()); // tell the server we want to leave
            }
			public void windowOpened(WindowEvent e) {} public void windowClosed(WindowEvent e) {}
			public void windowIconified(WindowEvent e) {} public void windowDeiconified(WindowEvent e) {}
			public void windowActivated(WindowEvent e) {} public void windowDeactivated(WindowEvent e) {}
		});
	}

	public void searchGame() {
		//layout
		btnPlay.setText("CANCEL"); // changing the play button to a cancel button
		lblInfo = new JLabel("Searching for players......");
		lblInfo.setAlignmentX(0.5f);
		contentPane.add(lblInfo);
		
		//logic
		searching = true;
		network = new Client("localhost", new Player(tfUsername.getText())); // making a connection to the server
		network.send(new JoinRequest(new ClassicMode(), network.player)); // basicly tell the server we want to join a (classic) game
		index = (int)network.getData(); // this index is the most important variable because when the game is running you can get your character with it
		thread = new Thread(this); thread.start(); // could be translated to 'waitForGameToStart()';
	}
	
	public void run() {waitForGameToStart();} 
	public void waitForGameToStart() // this func needs to be in a thread to avoid graphical bugs 
	{
		// check if game is full or not
		while (true) {
			Game g = network.getGame();
			//System.out.println(g.players);
			if (g.full) {
				@SuppressWarnings("unused")
				GameScreen gs = new GameScreen(network, index);
				this.dispose(); // close the menuscreen
				break;
			}
			try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();} // prevents lag
		}
	}
	
	public void cancelSearchGame() {
		//layout
		btnPlay.setText("PLAY"); // changing the cancel button to the original play button
		contentPane.remove(lblInfo); contentPane.repaint();
		
		//logic
		network.send(new LeaveRequest()); // tell the server we want to leave
		searching = false;
	}
}
