package graphics.tile;

import java.util.Map;

public class WallTile extends Tile
{
	private static final long serialVersionUID = 1L;
	public static int SpanX = 1, SpanY = 1;
	public static String type = "wall";
	public static boolean collidable = true;
	public static Map<String, String> status_path = Map.of("default", "wall.jpg");
	
	public WallTile(int x, int y) {
		super(x, y, SpanX, SpanY);
	}
	
	public WallTile() // if we want the background to be walls 
	{
		super(SpanX, SpanY, "background");
	}
}
