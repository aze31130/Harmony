package events;

import channels.TextChannel;
import commands.Command;
import json.JSONObject;
import server.Server;
import users.User;

public class CommandEvent extends Event {

	public TextChannel channel;

	public CommandEvent() {
		super("Command");
	}

	/*
	 * Arguments inside data object:
	 * @String: Channel (name of the channel that is receiving the command)
	 * @String: Arguments (json array of arguments)
	 */
	@Override
	public void fire(User sender, JSONObject data) {
		/*
		 * Execute the command
		 */
        for (Command c : Server.getInstance().commands) {
            if (c.name.equalsIgnoreCase(data.getString("CommandName"))) {
                c.execute(
					sender,
					null, //TODO getChannelByName here
					data.getString("CommandContent").split(" "));
            }
        }
	}
}
