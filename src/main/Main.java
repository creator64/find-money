package main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import graphics.Item;
import graphics.tile.*;
import graphics.sprite.*;

public class Main
{	
	public static int players = 1;
	// {"wall": {"default": "wall.jpg", "broken": "brokenwall.jpg", ....},
	//	"grass": {"default": "grass.jpg", ....},
	//  ....
	public static Map<String, Map<String, BufferedImage>> images = new HashMap<String, Map<String, BufferedImage>>();
	public static ArrayList<Item> tl = new ArrayList<Item>(); // temporary variable
	
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		loadResources();
		MenuScreen ms = new MenuScreen();
	}
	
	public static void loadSomeEnvironment() // temporary function
	{
		for (int x=0; x<1; x++) {
			tl.add(new WallTile(x, 2));
		}
	}
	
	@SuppressWarnings("unchecked")
	private static void loadResources() // fills the images field, see declaration for more info
	{
		Class<?>[] items = {WallTile.class, WallTile.class, PlayerSprite.class, MoneySprite.class};
		try {
			for (Class<?> i: items) {
				Map<String, String> status_path = (Map<String, String>)i.getField("status_path").get(i);
				Map<String, BufferedImage> status_image = new HashMap<String, BufferedImage>();
				
				for (Map.Entry<String,String> entry : status_path.entrySet()) {
					String key = entry.getKey();
					BufferedImage value = ImageIO.read(new File("resources/" + entry.getValue())); // entry.getValue is for ex "wall.jpg"
					status_image.put(key, value);
				}
				String type = (String)i.getField("type").get(null);
				images.put(type, status_image);
			}
		}
		catch(Exception e) {e.printStackTrace();}
	}
}
