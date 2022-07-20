package main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

import graphics.Item;
import graphics.tile.*;

public class Main 
{	
	public static int players = 1;
	public static Map<String, BufferedImage> images = new HashMap<String, BufferedImage>(); // {"wall": BufferedImage(resources/wall.jpg), ...}
	public static ArrayList<Item> tl = new ArrayList<Item>(); // temporary variable
	
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		loadResources();
		loadSomeEnvironment();
		MenuScreen ms = new MenuScreen();
	}
	
	public static void loadSomeEnvironment() // temporary function
	{
		for (int x=0; x<1; x++) {
			tl.add(new WallTile(x, 2));
		}
	}
	
	private static void loadResources() // very unclean: blama Java 
	{
		try {
			images.put("wall", ImageIO.read(new File("resources/wall.jpg")));
			images.put("grass", ImageIO.read(new File("resources/grass.jpg")));
			images.put("player", ImageIO.read(new File("resources/bomb.png")));
		}
		catch(Exception e) {e.printStackTrace();}
	}
}
