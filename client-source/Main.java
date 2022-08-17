import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.KeyPair;
import java.security.PublicKey;
import java.util.Base64;
import java.util.Scanner;

import cryptography.Cryptography;

public class Main {

	public static final String SERVER_IP = "127.0.0.1";
	public static final int SERVER_PORT = 3378;

	public static PublicKey SERVER_PUB_KEY = null;

	public static KeyPair keyPair = null;

	public static void main(String[] args) {
		System.out.println("Starting client on CLI mode !");

		keyPair = Cryptography.generateKeyPair(1024);

		try {
			Socket s = new Socket(SERVER_IP, SERVER_PORT);
			DataInputStream input = new DataInputStream(s.getInputStream());
			DataOutputStream output = new DataOutputStream(s.getOutputStream());
			Scanner scanner = new Scanner(System.in);
			Thread sendMessage = new Thread(new Runnable() {
				@Override
				public void run() {

					//send client's pub key
					try {
						System.out.println("Sending pubkey: " + keyPair.getPublic().getEncoded().hashCode());
						System.out.println("Sending pubkey: " + keyPair.getPublic().hashCode());
						output.write(keyPair.getPublic().getEncoded());

					} catch (IOException e) {
						e.printStackTrace();
					}

					while(true) {
						try {
							String clearMessage = scanner.nextLine();

							//byte[] encodedMessage = Cryptography.encrypt(clearMessage.getBytes(), SERVER_PUB_KEY);
							
							//String cipheredMessage = new String(encodedMessage);
							//System.out.println(cipheredMessage);
							//System.out.println(cipheredMessage.hashCode());
							output.writeUTF(clearMessage);
						} catch(IOException e) {
							e.printStackTrace();
						}
					}
				}
			});

			Thread receiveMessage = new Thread( new Runnable() {
				@Override
				public void run() {

					//Handshake
					try {
						byte[] receivedRaw = new byte[1024];
						
						input.read(receivedRaw);


						System.out.println("Received RAW: " + receivedRaw.length);

						SERVER_PUB_KEY = Cryptography.loadPublicKey(receivedRaw);
						System.out.println("Received key: " + SERVER_PUB_KEY.toString());
						System.out.println("Received key: " + SERVER_PUB_KEY.hashCode());

					} catch (IOException e) {
						e.printStackTrace();
					}

					while(true) {
						try {
							String encodedMessage = input.readUTF();

							//byte[] decodedMessage = Cryptography.decrypt(encodedMessage.getBytes(), keyPair.getPrivate());

							//String clearMessage = new String(decodedMessage);

							System.out.println(encodedMessage);
						} catch(IOException e) {
							e.printStackTrace();
							break;
						}
					}
				}
			});
			sendMessage.start();
			receiveMessage.start();
		} catch(IOException e) {
			System.err.println("Cannot connect to server");
		}
	}
}