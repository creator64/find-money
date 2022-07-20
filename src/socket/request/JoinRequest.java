package socket.request;
import java.net.InetAddress;
import game.Game;
import game.mode.Mode;
import main.Player;
import socket.Server;


public class JoinRequest implements Request
{
	private static final long serialVersionUID = 1L;
	public Mode mode;
	public Player player;
	
	public JoinRequest(Mode mode, Player player) {
		this.mode = mode;
		this.player = player;
	}
	
	@Override
	public void handleRequest(Server server,  InetAddress ipAddress, int port)
	{
		Game g = server.getGame(this); // get the right game
		server.registrate(port, g);
		int index = g.addPlayer(player);
		server.send(index, ipAddress, port);
	}
}
