import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.management.openmbean.InvalidKeyException;

import cryptography.Cryptography;
import exceptions.ClientReceiveException;
import json.JSONException;
import json.JSONObject;
import json.JSONTokener;
import plugins.Plugin;
import users.Ban;
import users.BanType;
import users.User;

public class ClientHandler implements Runnable {
	public User user;
	public Thread thread;
	public Socket socket;
	public DataInputStream input;
	public DataOutputStream output;
	public SecretKey symetricKey;

	public Boolean isLoggedIn = false;

	public ClientHandler(Socket s) {
		this.thread = new Thread(this);
		this.socket = s;
		this.symetricKey = null;
		try {
			this.input = new DataInputStream(s.getInputStream());
			this.output = new DataOutputStream(s.getOutputStream());
			thread.start();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * This methods waits for the client to send a byte array
	 */
	public byte[] receive() throws ClientReceiveException, IOException {
		//Getting client's message length
		int messageLength = Integer.parseInt(this.input.readUTF());

		//If message length is invalid
		if ((messageLength < 0) || (messageLength > 10000000))
			throw new ClientReceiveException("Message length invalid");

		byte[] message = new byte[messageLength];
		this.input.readFully(message, 0, message.length);
		return message;
	}

	/*
	 * This methods sends to the current client a byte array
	 */
	public void send(byte[] message) {
		try {
			this.output.writeUTF(Integer.toString(message.length));
			this.output.write(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Boolean handshake() {
		try {
			//get ip address of client
			InetAddress ip = this.socket.getInetAddress();

			//if the ip appears in the ban list then return false
			for (Ban ban : Server.getInstance().banList)
				if (ban.type.equals(BanType.IP) && ban.ip.equals(ip.getHostAddress()))
					return false;

			byte[] clientPubKey = this.receive();

			PublicKey clientPublicKey = Cryptography.loadPublicKey(clientPubKey);

			//check if the key is registered in only one profile else return false
			//TODO
			this.user = new User();
			this.user.pubKey = clientPublicKey;
			
			//If the account id appears in the ban list then return false
			for (Ban ban : Server.getInstance().banList)
				if (this.user.id == ban.accountId)
					return false;

			//Check if the user is really the owner of that key by sending a random string encrypted with client's public key
			//The client then decrypt the message, encrypt it with server's pub key and send it
			//The server decrypt it and compare the two strings
			//If the strings matches then generate a symetric key and accepts the client.
			//else return false

			//From here, the user has a valid key 

			//Generate a symetric 256 bits AES key
			this.symetricKey = Cryptography.generateKey(256);

			//Ciphers it with client's pub key
			byte[] encryptedSymetricKey = Cryptography.encrypt(this.user.pubKey, this.symetricKey.getEncoded());

			//Send the encrypted symetric key to client
			this.send(encryptedSymetricKey);

			//Handshake is done
			//If that procedure last for more than 10 seconds, kill the handler
			//TODO
		} catch (ClientReceiveException wrongBufferSize) {
			wrongBufferSize.printStackTrace();
			return false;
		} catch (InvalidKeyException invalidKey) {
			invalidKey.printStackTrace();
			return false;
		} catch (IOException io) {
			io.printStackTrace();
			System.err.println("Client handshake incomplete !");
		}
		return true;
	}

	@Override
	public void run() {
		this.isLoggedIn = handshake();

		while(this.isLoggedIn) {
			try {
				byte[] rawStringReceived = this.receive();

				//Use the symmetric key to decrypt
				byte[] bytePlainText = Cryptography.decrypt(this.symetricKey, rawStringReceived);

				String decryptedMessage = new String(bytePlainText);

				System.out.println(decryptedMessage);

				//Parsing received json
				JSONTokener parser = new JSONTokener(decryptedMessage);

				JSONObject object = new JSONObject(parser);

				//if client is sending a message
				if (object.has("message")) {
					String message = object.getString("message");
					System.out.println("Received message: " + message);
					for(Plugin p : Server.getInstance().plugins) {
						try {
							Object o = p.pluginClass.getDeclaredConstructor().newInstance();
							Method m = p.pluginClass.getMethod("onMessage", String.class);
							m.invoke(o, message);
						} catch (InstantiationException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						} catch (NoSuchMethodException e) {
							e.printStackTrace();
						} catch (SecurityException e) {
							e.printStackTrace();
						}
					}

					//Broadcast it to other clients
					for (ClientHandler client : Server.getInstance().onlineUsers) {
						if (client != this){
							byte[] messageEncrypted = Cryptography.encrypt(client.symetricKey, message.getBytes());
							client.send(messageEncrypted);
						}
					}
					continue;
				}

				//if client is sending a command action
				if (object.has("command")) {
					System.out.println("Received command: " + object.getString("command"));
					continue;
				}
			} catch(IOException e) {
				Server.getInstance().onlineUsers.remove(this);
				System.out.println("Client disconnected !");
				break;
			} catch(JSONException jsonParseFailed) {
				System.err.println("Error in user input, cannot parse json !");
			} catch (ClientReceiveException cre) {
				cre.printStackTrace();
			}
		}

		//Close the handler and stops the thread
		try {
			this.input.close();
			this.output.close();
			this.socket.close();
			this.thread.interrupt();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}