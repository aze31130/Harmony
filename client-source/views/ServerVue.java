package views;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import cryptography.Cryptography;
import json.JSONObject;
import models.Server;

public class ServerVue extends JFrame implements Action,Runnable {
	public Server server;
	public Thread receiveSignalsThread;

	public JTextArea messageArea;

	public JTextField messageInput;
	public JButton sendButton;
	
	public ServerVue(Server server) {
		this.server = server;
		this.receiveSignalsThread = new Thread(this);
		this.receiveSignalsThread.start();
		this.setTitle("Harmony client");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setLocationRelativeTo(null);
		this.setLayout(null);
		this.setResizable(true);
		this.setSize(1280, 720);
		
		this.messageArea = new JTextArea();
		this.messageArea.setBounds(0, 0, 300, 600);
		this.messageArea.setBackground(new Color(250, 250, 0));
		this.messageArea.setEditable(false);

		this.messageInput = new JTextField();
		this.messageInput.setBounds(0, 620, 150, 50);
		this.messageInput.setBackground(new Color(250, 0, 250));
		this.messageInput.setText("Your message here");
		
		this.sendButton = new JButton();
		this.sendButton.setBounds(250, 620, 100, 50);
		this.sendButton.setActionCommand("sendMessage");
		this.sendButton.setBackground(new Color(125, 70, 42));
		this.sendButton.setAction(this);

		this.add(messageArea);
		this.add(messageInput);
		this.add(this.sendButton);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		String message = this.messageInput.getText();

		JSONObject JSONMessage = new JSONObject();
		JSONObject JSONMessageContent = new JSONObject();
		JSONMessageContent.put("message", message);

		JSONMessage.put("id", "0");
		JSONMessage.put("type", "REQUEST");
		JSONMessage.put("name", "CREATE_MESSAGE");
		JSONMessage.put("data", JSONMessageContent);

		byte[] encryptedMessage = Cryptography.encrypt(this.server.symmetricKey, JSONMessage.toString().getBytes());

		this.server.send(encryptedMessage);

		this.messageInput.setText("");
		String existingChat = this.messageArea.getText();
		this.messageArea.setText(existingChat + "\n<You>: " + message);
	}

	@Override
	public Object getValue(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void putValue(String arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void run() {
		try {
			while(true) {
				byte[] receiveRaw = this.server.receive();

				byte[] decryptedMessage = Cryptography.decrypt(this.server.symmetricKey, receiveRaw);

				String message = new String(decryptedMessage);

				String existingChat = this.messageArea.getText();
				this.messageArea.setText(existingChat + "\n<Server>: " + message);
			}
		} catch (IllegalArgumentException e) {
			System.err.println("Server sent invalid size, dropping message.");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
