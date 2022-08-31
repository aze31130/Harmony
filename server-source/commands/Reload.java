package commands;

import users.Privilege;
import users.User;

public class Reload extends Command {
	public Reload() {
		super(
			"reload",
			"rld",
			"Reload the server, config, plugin and mods",
			Privilege.ADMINISTRATOR,
			"core"
		);    
	}

	@Override
	public void execute(User executor, String[] arguments) {
		System.out.println("Reload command");
	}
}
