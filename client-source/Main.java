import views.CommandLineInterface;
import views.ServerView;

public class Main {
	/*
	 * Entry point of the client
	 */
	public static void main(String[] args) {
		//Read flags in args

		//Read config file (language, server list, preferences)

		if ((args.length >= 3) && (args[0].equals("--cli"))) {
			//Call the CLI interface
			CommandLineInterface.getInstance(args[1], args[2]);
		} else {
			//Displays the graphical main menu and waits for user inputs
			ServerView.getInstance();
		}		
	}
}