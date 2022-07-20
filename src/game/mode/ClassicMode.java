package game.mode;

import java.io.Serializable;

import game.PreGame;
import graphics.sprite.PlayerSprite;

public class ClassicMode implements Mode, Serializable 
{
	private static final long serialVersionUID = 1L;

	public void addPlayerSprites(PreGame g) {
		for (int i = 0; i < g.amountPlayers; i++) {
			g.ev.addItem(new PlayerSprite(100, 100 + 100*i, i));
		}
	}

}
