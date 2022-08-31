package commands;

import users.Privilege;
import users.User;

public class Broadcast extends Command {
	public Broadcast() {
		super(
			"broadcast",
			"bcast",
			"Broadcasts a message to the entire server",
			Privilege.MODERATOR,
			"core"
		);
	}

	@Override
	public void execute(User executor, String[] arguments) {
		System.out.println("Ban command");
	}
}
