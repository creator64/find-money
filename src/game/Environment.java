package game;

import java.io.Serializable;
import java.util.ArrayList;
import graphics.Item;

public class Environment implements Serializable
{
	private static final long serialVersionUID = 1L;
	public ArrayList<Item> objectlist = new ArrayList<Item>();
	public int width = 2000;
	public int height = 2000; 
	private static int current_id_ev;
	
	public Environment(ArrayList<Item> objl) 
	{
		if (objl != null) // if objl is null it means this environment is (for now) empty
		{
			for (Item i: objl) {
				addItem(i);
			}
		}
	}
	
	public void addItem(Item item) {
		item.id_ev = current_id_ev;
		objectlist.add(item);
		current_id_ev++;
	}

	public Item getItemByIdEv(int id_ev) {
		for (Item i: objectlist) {
			if (i.id_ev == id_ev) {
				return i;
			}
		}
		return null;
	}
	
	 /* for example a wall object with x = 200, y = 300, width, height = 100
	  the function getItemByCoordinates will only return this wall object if RequestedX = 200, RequestedY = 300 */
	public Item getItemByCoordinates(int requestedX, int requestedY) 
	{
		for (Item i: objectlist) {
			if (i.x == requestedX && i.y == requestedY) {
				return i;
			}
		}
		return null;
	}

	/* for example a wall object with x = 200, y = 300, width, height = 100
	  the function getItemByCoordinatesArea will alse return this wall object if RequestedX = 250, RequestedY = 350 or
	  RequestedX = 289, RequestedY = 321, because these coordinates are filled by this wall because of its width and height,
	  but not if RequestedX = 234, RequestedY = 467*/
	public Item getItemByCoordinatesArea(int requestedX, int requestedY) 
	{
		for (Item i: objectlist) {
			if (requestedX >= i.x && requestedX <= i.x + i.width) {
				if (requestedY >= i.y && requestedY <= i.y + i.height) {
					return i;
				}
			}
		}
		return null;
	}
	
	public boolean CollisionDetection(Item item1, Item item2, boolean checkForCollidable) 
	{
		if (checkForCollidable) 
		{
			if (item1.isCollidable() == false && item2.isCollidable() == false) {
				return false;
			}
		}
		if (item1.id_ev == item2.id_ev) return false; // you cant collide with yourself
		
		int x1 = item1.x; int x2 = item1.x + item1.width; int x3 = item2.x; int x4 = item2.x + item2.width;
		int y1 = item1.y; int y2 = item1.y + item1.height; int y3 = item2.y; int y4 = item2.y + item2.height;
		// ||....................|| (item1)           ||............................................|| (item2)
		// x1                    x2                   x3                                            x4
		if (x3 <= x2 && x4 >= x1) { 
			if (y3 <= y2 && y4 >= y1) {
				return true;
			}
		}
		return false;
	}

	public Item getCollidedItem(Item item) // returns to what item variable "item" collides with 
	{
		for (Item i: objectlist) {
			if (CollisionDetection(item, i, true)) {
				return i;
			}
		}
		return null;
	}
}
