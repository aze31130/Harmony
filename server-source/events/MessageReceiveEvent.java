package events;

import java.io.IOError;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import channels.Message;
import channels.TextChannel;
import cryptography.Cryptography;
import json.JSONObject;
import requests.RequestName;
import requests.RequestType;
import server.Server;
import users.ClientHandler;
import users.User;

public class MessageReceiveEvent extends Event {

	public TextChannel channel;
	public Message message;
	public User author;

	public MessageReceiveEvent() {
		super(RequestType.REQUEST, RequestName.CREATE_MESSAGE);
	}

	/*
	 * Arguments:
	 * @String: author (the name of the author)
	 * @String: channel (the id of the channel)
	 * @String: message (the content of the message)
	 */
	@Override
	public void fire(JSONObject data) {
		/*
		 * When the server receives a message, it broadcast it to every other clients
		 */
		Server server = Server.getInstance();
		String message = data.getString("message");

		for (ClientHandler client : server.onlineUsers) {
			byte[] messageEncrypted = Cryptography.encrypt(client.symetricKey, message.getBytes());
			client.send(messageEncrypted);
		}

		/*
		 * Discord relay, send the message by doing an HTTP request to a given webhook
		 */
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder(URI.create(server.discordWebhookUrl))
			.header("Content-type", "application/json")
			.POST(HttpRequest.BodyPublishers.ofString("{\"content\": \"" + message + "\"}"))
			.build();

		try {
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			System.out.println(response.statusCode());
			System.out.println(response.body());
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

		//Check for achievement triggers ?
		//Check for plugin triggers
	}
}