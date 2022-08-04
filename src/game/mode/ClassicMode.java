package game.mode;

import java.io.Serializable;
import java.util.Random;
import game.PreGame;
import graphics.Item;
import graphics.sprite.MoneySprite;
import graphics.sprite.PlayerSprite;

public class ClassicMode implements Mode, Serializable 
{
	private static final long serialVersionUID = 1L;

	public void addPlayerSprites(PreGame g) {
		for (int i = 0; i < g.amountPlayers; i++) {
			g.ev.addItem(new PlayerSprite(100, 100 + 100*i, i));
		}
	}
	
	public void addMoney(PreGame g) {
		Random rand = new Random();
		int x, y;
		while (true) {
			x = rand.nextInt(g.ev.width);
			y = rand.nextInt(g.ev.height);
			Item i = g.ev.getItemByCoordinatesArea(x, y);
			if (i == null) break; // the random coordinate is not already taken by an object
		}
		MoneySprite money = new MoneySprite(x, y);
		g.ev.addItem(money);
	}

}
