package socket;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Map; import java.util.HashMap;
import game.Environment;
import game.Game;
import main.Main;
import socket.request.*;

public class Server extends Thread 
{
	private static int dataSize = 4096;
	private int gameId;
	private DatagramSocket socket;
	public ArrayList<Game> gameList = new ArrayList<Game>(); // a list that stores all the games
	public Map<Integer, Game> register = new HashMap<Integer, Game>(); // a dictionnary that binds a port to its game so we can easily give the right game instance when a client wants it
	private ByteArrayInputStream bis; private ObjectInputStream ois;
	private ByteArrayOutputStream bos; private ObjectOutputStream oos;
	
	public Server() {
		Main.loadSomeEnvironment();
		try {
			this.socket = new DatagramSocket(1331);
		}
		catch (Exception e) {e.printStackTrace();}
	}
	
	public void run() // a while loop where we constantly try to get data from clients
	{
		System.out.println("Server is running");
		while (true) {
			byte[] data = new byte[dataSize];
			DatagramPacket packet = new DatagramPacket(data, data.length);
			try {
				socket.receive(packet); // get the data (in ByteArrays) from the client
				createInputStreams(data);
				Request request = (Request)ois.readObject(); // convert the byteArrays data in an object (server only receives request objects)
				request.handleRequest(this, packet.getAddress(), packet.getPort()); 
				/* every request contains the function handleRequest. By passing a Server instance (ourself) 
				   the request can do anything in that function that can be done in the Server class */
			}
			catch (Exception e) {e.printStackTrace();}
		}
	}
	
	public void send(Object object, InetAddress ipAddress, int port) {
		try {
			createOutputStreams();
			oos.writeObject(object);
			byte[] data = bos.toByteArray(); // convert object to byteArrays
			DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port); // make a packet meant for the client with port 'port'
			socket.send(packet); // send the byteArrays to the client
		} catch (IOException e) {e.printStackTrace();}
	}
	
	public void createInputStreams(byte[] data) {
		bis = new ByteArrayInputStream(data);
		try {
			ois = new ObjectInputStream(bis);
		} catch(Exception e) {e.printStackTrace();}
	}
	
	public void createOutputStreams() {
		try {
			bos = new ByteArrayOutputStream(6400);
			oos = new ObjectOutputStream(bos);
		} catch(Exception e) {e.printStackTrace();}
	}
	
	public Game getGame(JoinRequest request) // handeling joinrequests 
	{
		for (Game g: gameList) // look in our gamelist if the requested type of game (for now only the mode matters) is in our list
		{
			if (g.mode == request.mode || !g.full) {
				return g; // return the requested type of game
			}
		}
		// in case the requested type of game was not found then create a new one and return it
		Game g = new Game(request.mode, new Environment(Main.tl), Main.players, gameId);
		gameList.add(g);
		gameId++;
		return g;
	}
	
	public static void main(String[] args) {
		Server server = new Server();
		server.start();
	}

	public void registrate(int port, Game g) {register.put(port, g);} // bind a port to a game
	public void deregistrate(int port) 
	{
		Game g = register.get(port);
		g.removePlayerByPort(port); // remove the player in the game
		register.remove(port); // remove the player in the servers register
	} 
}
