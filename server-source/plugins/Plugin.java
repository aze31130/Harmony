package plugins;

import java.lang.reflect.Method;
import java.util.List;

public class Plugin {
    public String name;
    public String author;
    public String description;
    public String version;
    public PluginPriority priority;
    public List<Method> methods;

    public Plugin(String name, String author, String description, String version, PluginPriority priority, List<Method> methods) {
        this.name = name;
        this.author = author;
        this.description = description;
        this.version = version;
        this.priority = priority;
        this.methods = methods;
    }
}