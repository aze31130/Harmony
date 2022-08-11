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
		this.loadConfig();
		this.loadBanlist();

		//Load commands (core)
		this.commands = new ArrayList<Command>();
		this.commands.add(new Help());
		this.commands.add(new Version());

		//Initialize list of online people
		this.onlineUsers = new ArrayList<ClientHandler>();

		//Load mods first
		this.loadMods();

		//Load plugins
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

	public void loadBanlist() {
		//Load the ban list
	}

	public void loadPlugins() {
		//plugin = server sided only (adds behavior on existing things)
		try {
			File pluginsFolder = new File("./plugins");
			List<File> pluginPaths = new ArrayList<File>();
			
			for (File plugin : pluginsFolder.listFiles())
				if (plugin.getName().endsWith(".jar"))
					pluginPaths.add(plugin);
					
			URL[] urls = new URL[pluginPaths.size()];
			
			for (int i = 0; i < pluginPaths.size(); i++)
				urls[i] = pluginPaths.get(i).toURI().toURL();

			for (int i = 0; i < pluginPaths.size(); i++)
				System.out.println(urls[i]);
				
			//for(URL u : urls) {
				URLClassLoader urlcl = new URLClassLoader(urls);
				Class<?> c = urlcl.loadClass("HarmonyPlugin");
				
				Object o = c.getDeclaredConstructor().newInstance();
				Method m = c.getMethod("onLoad");
				m.invoke(o);
				urlcl.close();

				//this.plugins.add()
			//}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loadMods() {
		//mod = server sends the mods to the client (adds content and new things such as new classes)
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