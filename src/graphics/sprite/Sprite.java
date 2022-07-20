package graphics.sprite;

import graphics.Item;

/* just a sprite with an x, y, width and height*/

public abstract class Sprite extends Item
{
	private static final long serialVersionUID = 1L;
	public static int width, height;

	public Sprite(int xCoord, int yCoord, int width, int height) {
		super(xCoord, yCoord, width, height);
	}
}
