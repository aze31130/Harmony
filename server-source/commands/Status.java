package commands;

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
    public void execute(User executor, String[] arguments) {
        System.out.println("Status command");
    }
}
