package commands;

import channels.Channel;
import users.Privilege;
import users.User;

public abstract class Command {
	public String name;
	public String alias;
	public String description;
	public Privilege privilege;
	public String pluginName;
	
	public Command(String name, String alias, String description, Privilege privilege, String pluginName) {
		this.name = name;
		this.alias = alias;
		this.description = description;
		this.privilege = privilege;
		this.pluginName = pluginName;
	}

	//Command design pattern
	public abstract void execute(User executor, Channel channel, String[] arguments);
}