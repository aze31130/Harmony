package views;

public class CommandLineInterface {

	//Singleton design pattern
	private static CommandLineInterface instance = null;
	public String serverIp;
	public Integer serverPort;

	/*
	 * Singleton design pattern, returns the current view instance
	 */
	public static synchronized CommandLineInterface getInstance(String serverIp, String serverPort) {
		if (instance == null)
			instance = new CommandLineInterface(serverIp, Integer.parseInt(serverPort));
		return instance;
	}

	private CommandLineInterface(String serverIp, int serverPort) {
		this.serverIp = serverIp;
		this.serverPort = serverPort;

		//TODO CLI Interface
	}
}