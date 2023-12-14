package server;

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

import achievements.Achievement;
import achievements.AchievementFactory;
import channels.Channel;
import java.util.ArrayList;
import java.util.Enumeration;

import commands.Command;
import commands.CommandFactory;
import cryptography.Cryptography;
import events.EventManager;
import json.JSONObject;
import logger.Logger;
import plugins.Plugin;
import plugins.PluginPriority;
import users.Ban;
import users.ClientHandler;
import utils.FileUtils;

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

	public Logger logger = null;

	public Boolean running = true;

	//Interval in seconds to flush messages and ban list to a file 
	public int saveInterval = 600;

	public String banListFileName;

	public EventManager eventManager;

	public List<Achievement> achievements;
	public List<Command> commands;
	public List<Ban> banList;
	public List<Plugin> plugins;
	public List<ClientHandler> onlineUsers;

	public String configFilename = "config.json";
	public String discordWebhookUrl = "";
	public String version = "0.0.1";

	private int keySize = 4096;


	//Public and private encryption key
	public KeyPair keyPair;

	private Server() {
		/*
		 * Loads server's variables
		 */
		this.loadConfig();

		/*
		 * Loads banlist
		 */
		this.loadBanlist();

		/*
		 * Initialize list of online people
		 */
		this.onlineUsers = new ArrayList<ClientHandler>();

		/*
		 * Initialize the event manager
		 */
		this.eventManager = new EventManager();

		/*
		 * Loads core commands
		 */
		this.loadCommands();

		/*
		 * Initialize advancements
		 */
		this.loadAchievements();

		/*
		 * Loads plugins
		 */
		//this.loadPlugins();

		/*
		 * Call the "LoadEvent" 
		 */
		this.eventManager.triggerEventByName("Load", null);
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
		System.out.println("Loading configuration...");
		/*
		 * Create config file if not exists
		 */
		if (!FileUtils.isFileExists(this.configFilename))
			this.generateDefaultConfig();

		//Load the file
		JSONObject config = new JSONObject(FileUtils.readRawFile(this.configFilename));

		//Get discord webhook relay url
		this.discordWebhookUrl = config.getString("discord_webhook_url");

		//Load keyPair (for beta testing, it will be generated at each reboot)
		this.keyPair = Cryptography.generateKeyPair(this.keySize);
		//Cryptography.saveKeyPair(this.keyPair);
		//this.keyPair = Cryptography.loadKeyPair();

		System.out.println("Server configuration succesfully loaded !");
	}

	public void loadAchievements() {
		System.out.println("Loading core achievements...");
		this.achievements = AchievementFactory.instanciateAchievements();

		System.out.println("Successfully loaded " + this.achievements.size() + " achievements !");
	}

	public void loadCommands() {
		System.out.println("Loading core commands...");
		this.commands = CommandFactory.instanciateCommands();

		System.out.println("Successfully loaded " + this.commands.size() + " commands !");
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
	 * Generates a default config file
	 */
	public void generateDefaultConfig() {
		JSONObject defaultConfig = new JSONObject();
		String[] keys = {"config_version", "discord_webhook_url"};
		String[] values = {"1", "https://discord.com/api/webhooks"};

		for (int i = 0 ; i < keys.length && i < values.length ; i++)
			defaultConfig.put(keys[i], values[i]);

		FileUtils.writeRawFile(this.configFilename, defaultConfig.toString());
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

					/*
					 * As we do not know if the plugin will lock into infinite loop, we can setup a timeout like that:
					 * 
					 * 
					 * ExecutorService executor = Executors.newCachedThreadPool();
					 * Callable<Object> task = new Callable<Object>();
					 * Future<Object> future = executor.submit(task);
					 * try {
					 * 	Object result = future.get(5, TimeUnit.SECONDS); 
					 * } catch (Exception ex) {
					 * } finally {
					 * 	future.cancel(true); // may or may not desire this
					 * }
					 */
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

				/*
				 * Check if the server is full.
				 * If it is, an client-sided error message will be displayed and the server
				 * will instantly kills the handler to save ressources
				 */
				if (this.onlineUsers.size() >= this.maxMembers) {
					s.close();
					continue;
				}

				ClientHandler ch = new ClientHandler(s);

				System.out.println("Client connected !" + s.getRemoteSocketAddress());
				this.onlineUsers.add(ch);
			}
			ss.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}