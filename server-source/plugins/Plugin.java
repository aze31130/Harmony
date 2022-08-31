package plugins;

public class Plugin {
	public String name;
	public String author;
	public String description;
	public String version;
	public PluginPriority priority;

	public Class<?> pluginClass;

	public Plugin(String name, String author, String description, String version, PluginPriority priority, Class<?> pluginClass) {
		this.name = name;
		this.author = author;
		this.description = description;
		this.version = version;
		this.priority = priority;
		this.pluginClass = pluginClass;
	}
}