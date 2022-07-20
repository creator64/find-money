package graphics.tile;

import graphics.Item;

/* With tiles we work with a coordinate system so for exemple: WallTile wt = new WallTile(5, 6); 
 * equivalent of wt.x = 500, wt.y = 600 (if tileSize = 100)
 */
 
public abstract class Tile extends Item
{
	private static final long serialVersionUID = 1L;
	public static final int tileSize = 100;
	public static int SpanX = 1, SpanY = 1;
	
	public Tile(int xCoord, int yCoord) {
		super(xCoord * tileSize, yCoord * tileSize, SpanX * tileSize, SpanY * tileSize);
	}
	
	public Tile(int x, int y, int SpanX, int SpanY) {
		super(x * tileSize, y * tileSize, SpanX * tileSize, SpanY * tileSize);
	}
	
	@SuppressWarnings("unused")
	public Tile(int SpanX, int SpanY, String mode) // mode is actually only there becuz otherwise theres duplicate Tile(int, int), for now mode always equals to "background"
	{
		super(0, 0, SpanX * tileSize, SpanY * tileSize);
	}
	
	@SuppressWarnings("unused")
	public Tile(String mode)
	{
		super(0, 0, SpanX * tileSize, SpanY * tileSize);
	}
}
