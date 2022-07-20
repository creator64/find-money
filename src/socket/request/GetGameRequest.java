package socket.request;

import java.net.InetAddress;
import game.Game;
import socket.Server;


public class GetGameRequest implements Request
{
	private static final long serialVersionUID = 1L;
	
	@Override
	public void handleRequest(Server server,  InetAddress ipAddress, int port) {
		Game g = server.register.get(port);
		server.send(g, ipAddress, port);
	}
}
