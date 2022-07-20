package socket;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import game.Game;
import main.Player;
import socket.request.GetGameRequest;
import socket.request.Request;

public class Client extends Thread {
	private static int dataSize = 1024;
	private InetAddress ipAddress;
	public DatagramSocket socket;
	private ByteArrayInputStream bis; private ObjectInputStream ois;
	private ByteArrayOutputStream bos; private ObjectOutputStream oos;
	public Player player;
	
	public Client(String ipAddress, Player player) {
		try 
		{
			this.player = player;
			this.socket = new DatagramSocket();
			this.ipAddress = InetAddress.getByName(ipAddress);
			this.player.port = this.socket.getLocalPort(); // give a the player instance acces to the port
		} catch (Exception e) {e.printStackTrace();}
	}
	
	public void send(Request request) {
		try {
			createOutputStreams();
			oos.writeObject(request); 
			byte[] data = bos.toByteArray(); // convert request to a ByteArray object
			DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, 1331); // send request to server
			socket.send(packet);
		} catch (IOException e) {e.printStackTrace();}
	}
	
	public Object getData() 
	{
		byte[] data = new byte[dataSize];
		DatagramPacket packet = new DatagramPacket(data, data.length);
		try {
			socket.receive(packet); // fill the packet with data sent from the server
			createInputStreams(data);
			Object object = ois.readObject(); // convert the byteArrays data in an object
			return object; // return the object sent from the server
		} catch(Exception e) {e.printStackTrace();}
		return null; // if it failed return null
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
	
	public Game getGame() {
		send(new GetGameRequest());
		Game g = (Game)getData();
		return g;
	}
	
	public void close() {
		this.socket.close();
	}
}
