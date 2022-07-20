package socket.request;

import java.net.InetAddress;
import socket.Server;

public class LeaveRequest implements Request
{
	private static final long serialVersionUID = -7521276831887654561L;

	public LeaveRequest() {}

	@Override
	public void handleRequest(Server server, InetAddress ipAddress, int port) {
		server.deregistrate(port);
	}
}