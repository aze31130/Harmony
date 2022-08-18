import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.KeyPair;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import channels.Channel;
import java.util.ArrayList;
import java.util.Enumeration;

import commands.Command;
import commands.Help;
import commands.Version;
import cryptography.Cryptography;
import json.JSONObject;
import plugins.Plugin;
import plugins.PluginPriority;
import users.Ban;
import utils.FileUtils;
import utils.JsonIO;

public class Server {
	//Singleton design pattern
	private static Server instance = null;

	//List of every server variables
	public String serverName;
	public String serverDescription;
	public int serverPort = 3378;
	public String server_key_name;
	public String server_key_algorithm;
	public int maxMembers = 10;

	public Boolean running = true;

	//Interval in seconds to flush messages and ban list to a file 
	public int saveInterval = 600;

	public String banListFileName;

	public List<Command> commands;
	public List<Ban> banList;
	public List<Plugin> plugins;
	public List<ClientHandler> onlineUsers;
	public String version = "0.0.1";

	//Public and private encryption key
	public KeyPair keyPair;

	private Server() {
		//Loads server's variables
		this.loadConfig();

		//Loads core commands
		this.loadCommands();

		//Loads banlist
		this.loadBanlist();

		//Initialize list of online people
		this.onlineUsers = new ArrayList<ClientHandler>();

		//Loads plugins
		this.loadPlugins();
	}

	/*
	 * Singleton design pattern, returns the current server instance
	 */
	public static synchronized Server getInstance() {
		if (instance == null)
			instance = new Server();
		return instance;
	}

	public void loadConfig() {
		//Check if config file exists
		//if (FileUtils.isFileExists("config.json")) {
			//Load config file (server variables)
		//} else {
			//Create default one and load it

			//loadConfig();
		//}
		

		

		//Load keyPair (for beta testing, it will be generated at each reboot)
		this.keyPair = Cryptography.generateKeyPair(1024);
		//Cryptography.saveKeyPair(this.keyPair);
		//this.keyPair = Cryptography.loadKeyPair();
	}

	public void loadCommands() {
		this.commands = new ArrayList<Command>();
		this.commands.add(new Help());
		this.commands.add(new Version());
	}

	/*
	 * Loads the ban list file
	 */
	public void loadBanlist() {
		this.banList = new ArrayList<Ban>();
		//JSONObject bans = JsonIO.loadJsonObject(this.banListFileName);
		
		//System.out.println(bans.toString());
	}

	/*
	 * Plugins, server sided, adds behavior on features that are present in the "vanilla" server.
	 */
	public void loadPlugins() {
		/*
		 * <!> WARNING <!>
		 * 
		 * This method will make the server dynamically load class files from the plugin folder.
		 * 
		 * Every plugin needs to have a class named "HarmonyPlugin" that contains events that the plugin wants to listen.
		 * Plugins can import custom libraries but it is mandatory to package every included libraries within the plugin.jar file.
		 * 
		 * There is still a known issue: if inside your dependancies a file imports a module from another dependancy then it is possible
		 * for the server to throw a NoClassDefFoundError. I believe this is due to load order but this will be fixed later on. 
		 * 
		 * The case where 2 plugins brings the same library or depends on each other has not been tested yet and is not consider as priority.
		 * 
		 * Server administrators need to ensure that loaded plugins are not malevolent as this method does not check the injected code
		 * and every plugin can get and modify server's attribute.
		 */
		this.plugins = new ArrayList<Plugin>();

		try {
			File pluginsFolder = new File("./plugins");

			//Iterate through every files in the plugins folder
			for (File plugin : pluginsFolder.listFiles()) {

				//Check if it's a jar file
				if (plugin.getName().endsWith(".jar")) {

					//Convert it to URL[]
					URL[] urls = { new URL("file:" + plugin.getPath()) };

					URLClassLoader urlcl = new URLClassLoader(urls);
					
					JarFile jarFile = new JarFile(plugin.getPath());
					Enumeration<JarEntry> e = jarFile.entries();
					
					//Get all classes path to load plugin dependencies
					while (e.hasMoreElements()) {
						JarEntry je = (JarEntry) e.nextElement();

						//If the entry is not a class file
						if(je.isDirectory() || !je.getName().endsWith(".class") || je.getName().equalsIgnoreCase("HarmonyPlugin"))
							continue;
						
						//Substring with -6 because ".class" is 6 characters long
						String className = je.getName().substring(0, je.getName().length() - 6);
						className = className.replace('/', '.');
						//Load the class into the server
						urlcl.loadClass(className);
					}

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

				//Check if the server is full
				if (this.onlineUsers.size() >= this.maxMembers) {
					System.out.println("Server full !");
					//Error message "Sorry, the server is full" will be client sided
					//Instantly kills the handler to save ressources
					s.close();
					continue;
				}

				ClientHandler ch = new ClientHandler(s);
				this.onlineUsers.add(ch);
				System.out.println("Client connected !");
			}
			ss.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}