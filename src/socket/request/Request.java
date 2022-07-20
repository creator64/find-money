package socket.request;

import java.io.Serializable;
import java.net.InetAddress;
import socket.Server;

public interface Request extends Serializable 
{
	//private static final long serialVersionUID = 1L;

	public void handleRequest(Server server,  InetAddress ipAddress, int port);
}
