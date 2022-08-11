package commands;

import users.Privilege;
import users.User;

public class Ban extends Command {
    public Ban() {
        super(
            "ban",
            "ban",
            "Bans a member from the server",
            Privilege.MODERATOR,
            "core"
        );    
    }

    @Override
    public void execute(User executor, String[] arguments) {
        System.out.println("Ban command");
    }
}
