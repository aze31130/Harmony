package commands;

import users.Privilege;
import users.User;

public class Help extends Command {
	public Help() {
		super(
			"help",
			"h",
			"Displays an help page",
			Privilege.NONE,
			"core"
		);    
	}

	@Override
	public void execute(User executor, String[] arguments) {
		System.out.println("Help command test");
	}
}