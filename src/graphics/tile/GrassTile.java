package graphics.tile;

import java.util.Map;

public class GrassTile extends Tile 
{
	private static final long serialVersionUID = 1L;
	public static String type = "grass";
	public static Map<String, String> status_path = Map.of("default", "grass.jpg");

	public GrassTile(int xCoord, int yCoord) {
		super(xCoord, yCoord);
	}

	public GrassTile() {
		super("background");
	}
}
