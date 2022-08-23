import models.Server;

public class Main {
	/*
	 * Entry point of the client
	 */
	public static void main(String[] args) {
		//Read flags in args

		//Read config file (language, server list, preferences)

		//Displays the main menu and waits for user inputs

		//Connection test
		Server harmony = new Server("Harmony", "127.0.0.1", 3378);
		harmony.connect();
	}
}