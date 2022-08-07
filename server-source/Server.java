import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyPair;
import java.util.List;
import java.util.ArrayList;

import commands.Command;
import commands.Help;
import commands.Version;

public class Server {
	//Singleton design pattern
	private static Server instance = null;

	//List of every server variables
	public String serverName;
	public String serverDescription;
	public int serverPort = 3378;
	public String server_key_name;
	public String server_key_algorithm;
	public int maxMembers;

	public List<Command> commands;
	//public List<Ban> banList;
	public List<ClientHandler> onlineUsers;

	//Public and private encryption key
	private KeyPair keyPair;



	private Server() {
		//Load config file (server variables)

		//Load banList

		//Load commands (core)
		this.commands = new ArrayList<Command>();
		this.commands.add(new Help());
		this.commands.add(new Version());

		//Load mods and plugins (TODO)

	}

	/*
	Singleton design pattern, returns the current server instance
	*/
	public static synchronized Server getInstance() {
		if (instance == null)
			instance = new Server();
		return instance;
	}

	public void start() {
		System.out.println("Server is running ! Listening on port " + this.serverPort);
		try {
			ServerSocket ss = new ServerSocket(this.serverPort);
			while(true) {
				Socket s = ss.accept();
				
				System.out.println("Accepting socket on " + s.getRemoteSocketAddress());

				ClientHandler ch = new ClientHandler(s);
				this.onlineUsers.add(ch);
				System.out.println("Client connected !");
				ch.output.writeUTF("Welcome !");
			}
		} catch(IOException e) {

		}
	}
}