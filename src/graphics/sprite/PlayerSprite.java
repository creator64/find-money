package graphics.sprite;

import java.util.Map;

public class PlayerSprite extends Sprite 
{
	private static final long serialVersionUID = 1L;
	public static String type = "player";
	public static int width = 50, height = 50;
	public static Map<String, String> status_path = Map.of("default", "bomb.png");
	public int index;
	public int speed = 8;
	public int moneyCollected = 0;
	public boolean hasMoney = false;

	public PlayerSprite(int xCoord, int yCoord, int index) {
		super(xCoord, yCoord, width, height);
		this.index = index;
	}
}
