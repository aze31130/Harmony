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
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.management.openmbean.InvalidKeyException;

import cryptography.Cryptography;
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

	public Boolean handshake() {
		try {
			//get ip address of client
			InetAddress ip = this.socket.getInetAddress();

			//if the ip appears in the ban list then return false
			for (Ban ban : Server.getInstance().banList)
				if (ban.type.equals(BanType.IP) && ban.ip.equals(ip.getHostAddress()))
					return false;

			//Sends server's public key
			System.out.println("Sending public key (hashcode:" + Server.getInstance().keyPair.getPublic().getEncoded().hashCode() + ")");
			this.output.write(Server.getInstance().keyPair.getPublic().getEncoded());

			//get pubkey of client
			byte[] receivedRaw = new byte[1024];
			input.read(receivedRaw);

			PublicKey clientPublicKey = Cryptography.loadPublicKey(receivedRaw);
			this.user = new User();
			this.user.pubKey = clientPublicKey;

			//check if the key is registered in only one profile
			//TODO

			//if the key is not registered then return false
			//TODO
			
			//If the account id appears in the bas list then return false
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
			KeyGenerator generator = KeyGenerator.getInstance("AES");
			generator.init(256);
			this.symetricKey = generator.generateKey();

			//Ciphers it with client's pub key
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.PUBLIC_KEY, clientPublicKey);
            byte[] encryptedSymetricKey = cipher.doFinal(symetricKey.getEncoded());

			//Send the encrypted symetric key to client
			this.output.write(encryptedSymetricKey);

			//Handshake is done
			//If that procedure last for more than 10 seconds, kill the handler
			//TODO
		} catch (NoSuchAlgorithmException noAlgo) {
			noAlgo.printStackTrace();
		} catch (InvalidKeyException invalidKey) {
			invalidKey.printStackTrace();
		} catch (java.security.InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public void run() {
		this.isLoggedIn = handshake();

		while(this.isLoggedIn) {
			try {
				String rawStringReceived = this.input.readUTF();

				//Parsing received json
				JSONTokener parser = new JSONTokener(rawStringReceived);

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
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (NoSuchMethodException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (SecurityException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					//Broadcast it to other clients
					for (ClientHandler client : Server.getInstance().onlineUsers) {
						if (client != this)
							client.output.writeUTF(message);
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