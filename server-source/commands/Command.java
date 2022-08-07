package commands;

public abstract class Command {
    public String name;
    public String alias;
    public String description;
    public String pluginName;
    
    public Command(String name, String alias, String description, String pluginName) {
        this.name = name;
        this.alias = alias;
        this.description = description;
        this.pluginName = pluginName;
    }

    //Command design pattern
    public abstract void execute();
}