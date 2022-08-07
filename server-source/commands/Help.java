package commands;

public class Help extends Command {
    public Help() {
        super(
            "help",
            "h",
            "Displays an help page",
            "core"
        );    
    }

    @Override
    public void execute() {
        System.out.println("Help command test");
    }
}