package views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import java.awt.event.ActionEvent;
import javax.swing.JTextPane;

import cryptography.Cryptography;
import json.JSONObject;
import models.Server;

public class ServerView extends JFrame {

	//Singleton design pattern
	private static ServerView instance = null;

	public JPanel connectionContainer;
	public JTextField serverIp;
	public JTextField serverPort;
	public JButton serverConnect;

	public JPanel messageContainer;
	public JTextField message;
	public JButton sendMessage;

	public JPanel chatContainer;
	public JScrollPane scrollBar;
	public JTextPane chatContent;

	public Server server;

	/*
	 * Singleton design pattern, returns the current view instance
	 */
	public static synchronized ServerView getInstance() {
		if (instance == null)
			instance = new ServerView();
		return instance;
	}

	private ServerView() {
		this.setTitle("Harmony client 0.0.1");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setLayout(new BorderLayout());

		/*
		 * Init connection container
		 */
		this.connectionContainer = new JPanel();

		this.serverIp = new JTextField("127.0.0.1", 15);
		this.serverPort = new JTextField("3378", 8);
		this.serverConnect = new JButton("Connect");
		this.serverConnect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ServerView view = ServerView.getInstance();

				try {
					String ip = serverIp.getText();
					Integer port = Integer.parseInt(serverPort.getText());
					
					view.server = new Server(ip, port);
					if (view.server.connect()) {
						view.printToChat("Successfully connected to " + ip + " on port " + port + " !");
						
						view.serverIp.setEditable(false);
						view.serverPort.setEditable(false);
						view.serverConnect.setEnabled(false);
					} else {
						view.printToChat("Could not connect to " + ip + " on port " + port + " !");
					}
				} catch (NumberFormatException exception) {
					view.printToChat("Invalid port !");
				}
			}
		});

		this.connectionContainer.add(this.serverIp);
		this.connectionContainer.add(this.serverPort);
		this.connectionContainer.add(this.serverConnect);

		this.add(this.connectionContainer, BorderLayout.NORTH);

		/*
		 * Init message container
		 */
		this.messageContainer = new JPanel();
		this.message = new JTextField("Your message here", 20);
		this.sendMessage = new JButton("Send");
		this.sendMessage.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ServerView view = ServerView.getInstance();

				String message = view.message.getText();
				view.printToChat(message);

				JSONObject jsonPayload = new JSONObject();
				JSONObject messagePayload = new JSONObject();

				messagePayload.put("message", message);

				jsonPayload.put("id", "0");
				jsonPayload.put("type", "REQUEST");
				jsonPayload.put("name", "CREATE_MESSAGE");
				jsonPayload.put("data", messagePayload);

				/*
				 * Encrypt the message
				 */
				byte[] encryptedMessage = Cryptography.encrypt(view.server.symmetricKey, jsonPayload.toString().getBytes());

				view.server.send(encryptedMessage);
			}
		});

		this.messageContainer.add(this.message);
		this.messageContainer.add(this.sendMessage);

		this.add(this.messageContainer, BorderLayout.SOUTH);

		/*
		 * Init chat container
		 */
		this.chatContainer = new JPanel();
		
		this.chatContent = new JTextPane();
		this.chatContent.setText("Welcome to Harmony, you can connect using the button on top. Have fun !");
		this.chatContent.setEditable(false);
		this.chatContent.setPreferredSize(new Dimension(800, 400));
		this.chatContainer.add(this.chatContent);
		this.scrollBar = new JScrollPane(this.chatContent, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.scrollBar.getVerticalScrollBar().setUnitIncrement(23);
		
		this.add(this.scrollBar, BorderLayout.CENTER);

		/*
		 * Compress everything and set a better resolution
		 */
		this.pack();
	}

	public void printToChat(String message) {
		ServerView view = ServerView.getInstance();
		view.chatContent.setText(view.chatContent.getText() + System.lineSeparator() + message);
	}
}
