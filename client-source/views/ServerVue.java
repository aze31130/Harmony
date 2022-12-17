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
import models.Server;

public class ServerVue extends JFrame implements Runnable {

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
	public Thread receiveSignalsThread;

	
	public ServerVue() {
		this.setTitle("Harmony client 0.0.1");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setLayout(new BorderLayout());

		/*
		 * Init connection container
		 */
		this.connectionContainer = new JPanel();

		this.serverIp = new JTextField("server ip", 15);
		this.serverPort = new JTextField("server port", 8);
		this.serverConnect = new JButton("Connect");
		this.serverConnect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String ip = serverIp.getText();
				Integer port = Integer.parseInt(serverPort.getText());
				Server s = new Server(ip, port);
				s.connect();

				//TODO
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
		this.message = new JTextField("Your message here");
		this.sendMessage = new JButton("Send");
		this.sendMessage.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String content = "";

				//TODO
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
		
		/*this.server = server;
		this.receiveSignalsThread = new Thread(this);
		this.receiveSignalsThread.start();
		*/

		/*
		 * Compress everything and set a better resolution
		 */
		this.pack();
	}

	public void printToChat(String message) {

	}

	
	@Override
	public void run() {
		try {
			while(true) {
				byte[] receiveRaw = this.server.receive();

				byte[] decryptedMessage = Cryptography.decrypt(this.server.symmetricKey, receiveRaw);

				String message = new String(decryptedMessage);

				//String existingChat = this.messageArea.getText();
				//this.messageArea.setText(existingChat + "\n<Server>: " + message);
			}
		} catch (IllegalArgumentException e) {
			System.err.println("Server sent invalid size, dropping message.");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
