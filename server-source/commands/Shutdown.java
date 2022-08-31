package commands;

import users.Privilege;
import users.User;

public class Shutdown extends Command {
	public Shutdown() {
		super(
			"shutdown",
			"turnoff",
			"Shutdowns the server",
			Privilege.ADMINISTRATOR,
			"core"
		);    
	}

	@Override
	public void execute(User executor, String[] arguments) {
		//Admin check here
		if (users.Permission.canExecute(Privilege.ADMINISTRATOR, executor)) {
			System.out.println("Shutting down the server !");
		} else {
			System.out.println("Sorry, you do not have the permission do to this !");
		}
	}
}
