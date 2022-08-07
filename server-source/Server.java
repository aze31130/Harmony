import java.security.KeyPair;
import java.util.List;

public class Server {
	//Singleton design pattern
	private static Server instance = null;

	//List of every server variables
	public String serverName;
	public String serverDescription;
	public int serverPort;
	public String server_key_name;
	public String server_key_algorithm;
	public int maxMembers;
	//public List<Ban> banList;
	//public List<ClientHandler> onlineUsers;

	private KeyPair keyPair;



	private Server() {
		/*
		Server launch stages:
		-getInstance
		-load config
		-load mods
		-load plugins
		-listen to clients
		*/
	}

	/*
	Singleton design pattern, returns the current server instance
	*/
	public static synchronized Server getInstance() {
		if (instance == null)
			instance = new Server();
		return instance;
	}

	/*
	Load config from file
	*/
	public void loadConfig() {
		System.out.println("Loading configuration file");
		// config file (server variables), banlist
	}

	public void start() {
		System.out.println("Server is running ! Listening on port " + this.serverPort);
	}
}