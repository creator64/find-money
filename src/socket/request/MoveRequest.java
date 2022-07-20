package socket.request;

import java.net.InetAddress;
import game.Game;
import socket.Server;

public class MoveRequest implements Request 
{
	private static final long serialVersionUID = 1L;
	int x, y, id_ev;

	public MoveRequest(int x, int y, int id_ev) {
		this.x = x;
		this.y = y;
		this.id_ev = id_ev;
	}

	@Override
	public void handleRequest(Server server, InetAddress ipAddress, int port) {
		Game g = server.register.get(port);
		g.moveItem(x, y, id_ev);
	}

}
