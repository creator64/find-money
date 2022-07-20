package graphics.tile;

public class GrassTile extends Tile 
{
	private static final long serialVersionUID = 1L;
	//public static String path = "grass.jpg";
	public static String type = "grass";

	public GrassTile(int xCoord, int yCoord) {
		super(xCoord, yCoord);
	}

	public GrassTile() {
		super("background");
	}
}
