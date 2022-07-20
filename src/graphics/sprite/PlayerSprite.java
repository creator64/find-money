package graphics.sprite;


public class PlayerSprite extends Sprite 
{
	private static final long serialVersionUID = 1L;
	public static String type = "player";
	public static int width = 50, height = 50;
	public int index;
	public int speed = 5;

	public PlayerSprite(int xCoord, int yCoord, int index) {
		super(xCoord, yCoord, width, height);
		this.index = index;
	}
}
