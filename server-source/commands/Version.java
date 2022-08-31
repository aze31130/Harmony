package commands;

import users.Privilege;
import users.User;

public class Version extends Command {
	public Version() {
		super(
			"version",
			"v",
			"Shows the version of the server",
			Privilege.NONE,
			"core"
		);    
	}

	@Override
	public void execute(User executor, String[] arguments) {
		System.out.println("Version command test");
	}
}