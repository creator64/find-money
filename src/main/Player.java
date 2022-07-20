package main;

import java.io.Serializable;

public class Player implements Serializable 
{
	private static final long serialVersionUID = 1L;
	public String username;
	public int port; // will be assigned in Client class
	
	public Player(String username) {
		this.username = username;
	}

}
