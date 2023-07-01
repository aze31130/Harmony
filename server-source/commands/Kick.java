package commands;

import channels.Channel;
import users.Privilege;
import users.User;

public class Kick extends Command {
	public Kick() {
		super(
			"kick",
			"kick",
			"Kicks a member from the server",
			Privilege.MODERATOR,
			"core"
		);    
	}

	@Override
	public void execute(User executor, Channel channel, String[] arguments) {
		System.out.println("Kick command");
	}
}
