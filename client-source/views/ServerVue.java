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
		this.setSize(1280, 720);

		this.messageArea = new JTextArea();
		this.messageArea.setBounds(50, 0, 150, 50);
		this.messageArea.setBackground(new Color(250, 250, 0));


		this.messageInput = new JTextField();
		this.messageInput.setBounds(250, 0, 150, 50);
		this.messageInput.setBackground(new Color(250, 0, 250));
		
		
		this.sendButton = new JButton();
		this.sendButton.setBounds(100, 100, 100, 50);
		this.sendButton.setActionCommand("sendMessage");
		this.sendButton.setBackground(new Color(125, 70, 42));
		this.sendButton.setAction(this);

		this.add(messageArea);
		this.add(messageInput);
		this.add(this.sendButton);

		this.messageInput.setText("arg0");
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		String message = this.messageInput.getText();
		System.out.println("Sending: " + message);

		JSONObject JSONMessage = new JSONObject();
		JSONMessage.put("message", message);
		byte[] encryptedMessage = Cryptography.encrypt(this.server.symmetricKey, JSONMessage.toString().getBytes());

		this.server.send(encryptedMessage);

		this.messageInput.setText("");
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

				System.out.println(message);
			}
		} catch (IllegalArgumentException e) {
			System.err.println("Server sent invalid size, dropping message.");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
