package graphics.tile;


public class WallTile extends Tile
{
	private static final long serialVersionUID = 1L;
	public static int SpanX = 1, SpanY = 1;
	public static String type = "wall";
	public static boolean collidable = true;
	
	public WallTile(int x, int y) {
		super(x, y, SpanX, SpanY);
	}
	
	public WallTile() // if we want the background to be walls 
	{
		super(SpanX, SpanY, "background");
	}
}
