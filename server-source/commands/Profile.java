package commands;

import channels.Channel;
import users.Privilege;
import users.User;

public class Profile extends Command {
	public Profile() {
		super(
			"profile",
			"p",
			"Displays your profile's informations",
			Privilege.NONE,
			"core"
		);    
	}

	@Override
	public void execute(User executor, Channel channel, String[] arguments) {
		System.out.println("Profile command");
	}
}
