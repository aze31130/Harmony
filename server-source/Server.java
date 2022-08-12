import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import channels.Channel;
import java.util.ArrayList;

import commands.Command;
import commands.Help;
import commands.Version;
import plugins.Plugin;
import plugins.PluginPriority;
import users.Ban;

public class Server {
	//Singleton design pattern
	private static Server instance = null;

	//List of every server variables
	public String serverName;
	public String serverDescription;
	public int serverPort = 3378;
	public String server_key_name;
	public String server_key_algorithm;
	public int maxMembers;

	public Boolean running = true;
	public int saveInterval = 600;

	public List<Command> commands;
	public List<Ban> banList;
	public List<Plugin> plugins;
	public List<ClientHandler> onlineUsers;
	public String version = "0.0.1";

	//Public and private encryption key
	//private KeyPair keyPair;

	private Server() {
		//Loads server's variables
		this.loadConfig();

		//Loads core commands
		this.loadCommands();

		//Loads banlist
		this.loadBanlist();

		//Initialize list of online people
		this.onlineUsers = new ArrayList<ClientHandler>();

		//Loads mods first
		this.loadMods();

		//Loads plugins
		this.loadPlugins();
	}

	/*
	Singleton design pattern, returns the current server instance
	*/
	public static synchronized Server getInstance() {
		if (instance == null)
			instance = new Server();
		return instance;
	}

	public void loadConfig() {
		//Load config file (server variables)
	}

	public void loadCommands() {
		this.commands = new ArrayList<Command>();
		this.commands.add(new Help());
		this.commands.add(new Version());
	}

	public void loadBanlist() {
		//Load the ban list
	}

	/*
	 * Mods, client and server sided, adds new features and classes that aren't present in the server by default.
	 * Note that mods are loaded BEFORE plugins, that way, plugins can add behaviors on mods.
	 */
	public void loadMods() {
		//mod = server sends the mods to the client (adds content and new things such as new classes)
	}

	/*
	 * Plugins, server sided, adds behavior on features that are present in the "vanilla" server.
	 */
	public void loadPlugins() {
		/*
		 * <!> WARNING <!>
		 * As of today, there is a problem with plugin that imports external libraries.
		 * 
		 * Plugins cannot come with their own libraries. This will be resolved later as this isn't a priority.
		 * The issue comes from the classpath of included lib.
		 * 
		 * If a plugin imports a package, when the plugin will be loaded in the server, it will look for that package
		 * in the server's root class path. If the lib is already present in the server build there is no issue.
		 * 
		 * A solution to that will be to load every class files included in the plugin.jar dynamically. That way,
		 * all the dependancies should be included in the plugin.jar file and aren't installed in the server build.
		 */

		this.plugins = new ArrayList<Plugin>();

		try {
			File pluginsFolder = new File("./plugins");
			
			for (File plugin : pluginsFolder.listFiles()) {
				if (plugin.getName().endsWith(".jar")) {
					URL[] urls = new URL[1];
					urls[0] = plugin.toURI().toURL();

					URLClassLoader urlcl = new URLClassLoader(urls);
					//Search for the class called HarmonyPlugin
					Class<?> c = urlcl.loadClass("HarmonyPlugin");

					//Need to load plugin information with config file or with properties in class as fields
					Plugin p = new Plugin("", "", "", "", PluginPriority.NORMAL, c);
					this.plugins.add(p);

					Object o = c.getDeclaredConstructor().newInstance();
					Method m = c.getMethod("onLoad");
					m.invoke(o);
					urlcl.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addCommand(Command newCommand) {
		//To improve later, create new command from argument here
		//and assign a given execute method
		this.commands.add(newCommand);
	}

	public void addChannel(Channel newChannel) {
		//TODO
	}

	public void start() {
		System.out.println("Server is running ! Listening on port " + this.serverPort);
		try {
			ServerSocket ss = new ServerSocket(this.serverPort);
			while(this.running) {
				Socket s = ss.accept();
				
				System.out.println("Accepting socket on " + s.getRemoteSocketAddress());

				//Check if ban ip here

				ClientHandler ch = new ClientHandler(s);
				this.onlineUsers.add(ch);
				System.out.println("Client connected !");
				ch.output.writeUTF("Welcome !");
			}
			ss.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}