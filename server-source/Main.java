import server.Server;

public class Main {
	/*
	 * Entry point of the project
	 */
	public static void main(String[] args) {
		Server server = Server.getInstance();
		System.out.println("Starting Harmony server version " + server.version);
		server.start();
	}
}