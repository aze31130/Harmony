package commands;

import java.util.ArrayList;
import java.util.List;

public class CommandFactory {
	/*
	 * This methods returns a list of instanciated achievements
	 */
	public static List<Command> instanciateCommands() {
		List<Command> commands = new ArrayList<Command>();
		
		commands.add(new Ban());
		commands.add(new Broadcast());
		commands.add(new Help());
		commands.add(new Version());

		return commands;
	}
}