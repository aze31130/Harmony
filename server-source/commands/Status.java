package commands;

import channels.Channel;
import users.Privilege;
import users.User;

public class Status extends Command {
	public Status() {
		super(
			"status",
			"st",
			"Displays server status",
			Privilege.NONE,
			"core"
		);    
	}

	@Override
	public void execute(User executor, Channel channel, String[] arguments) {
		System.out.println("Status command");
	}
}
