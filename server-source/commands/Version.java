package commands;

public class Version extends Command {
    public Version() {
        super(
            "version",
            "v",
            "Shows the version of the server",
            "core"
        );    
    }

    @Override
    public void execute() {
        System.out.println("Version command test");
    }
}