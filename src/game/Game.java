package game;

import java.io.Serializable;
import java.util.ArrayList;
import game.mode.Mode;
import graphics.Item;
import graphics.sprite.PlayerSprite;

public class Game extends PreGame implements Serializable
{	
	private static final long serialVersionUID = 1L;

	public Game(Mode mode, Environment ev, int amountPlayers, int gameId) {
		super(mode, ev, amountPlayers, gameId);
	}
	
	public ArrayList<PlayerSprite> getPlayerSpriteList()
	{
		ArrayList<PlayerSprite> playerSpriteList = new ArrayList<PlayerSprite>();
		for (Item item: this.ev.objectlist) {
			if (item instanceof PlayerSprite) {
				playerSpriteList.add((PlayerSprite)item);
			}
		}
		return playerSpriteList;
	}
	
	public void moveItem(int dx, int dy, int id_ev) {
		Item item = this.ev.getItemByIdEv(id_ev);
		item.x += dx; item.y += dy; // move the player
		Item itemAboutToCollideWith = this.ev.getCollidedItem(item);
		if (itemAboutToCollideWith != null) // if it collides with a collidable object undo the move
			{item.x -= dx; item.y -= dy;}
		else if (item.x < 0 || item.x > this.ev.width || item.y < 0 || item.y > this.ev.height) {
			item.x -= dx; item.y -= dy; // if were out of bounds undo the move too
		}
	}
}