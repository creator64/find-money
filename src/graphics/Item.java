package graphics;

import java.awt.image.BufferedImage;
import java.io.Serializable;

import main.Main;

/* when commented 'on the map' its talking about virtual positions
there are no negative coordinates on a map: (0,0) is the upper left corner on the map  */

public abstract class Item implements Serializable 
{
	private static final long serialVersionUID = 1L;
	public final int width, height;
	public int x, y; // position on the map
	public int id_ev; // an id given to an Item instance by the environment it has been put in
	public static String type; // this variable makes it easier to identify the type of tile and for now we get the path of its image by the type
	public static boolean collidable = false; // if true then the player can't walk over this object
	
	
	public Item(int x, int y, int width, int height) {
		this.x = x; // x pos on map
		this.y = y; // y pos on map
		this.width = width;
		this.height = height;
	}
	
	public BufferedImage getImage()
	{
		String type = getType();
		return Main.images.get(type);
	}
	
	/* for example:
	 * Item wt = new WallTile(0, 0);
	 * wt.type returns null but wt.getType() returns "wall"
	 * same goes for all other fields except for x, y, width, height, id_ev
	 */
	public String getType() {return (String)getField("type");}
	public boolean isCollidable() {return (boolean)getField("collidable");}
	
	public Object getField(String field) {
		try {
			return this.getClass().getField(field).get(this);
		} catch (Exception e) {e.printStackTrace();}
		return null;
	}
}
