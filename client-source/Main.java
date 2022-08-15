import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.KeyPair;
import java.security.PublicKey;
import java.util.Scanner;

import cryptography.Cryptography;

public class Main {

	public static final String SERVER_IP = "127.0.0.1";
	public static final int SERVER_PORT = 3378;

	public static void main(String[] args) {
		System.out.println("Starting client on CLI mode !");

		KeyPair keyPair = Cryptography.generateKeyPair(1024);
		PublicKey serverPubKey = null;

		try {
			Socket s = new Socket(SERVER_IP, SERVER_PORT);
			DataInputStream input = new DataInputStream(s.getInputStream());
			DataOutputStream output = new DataOutputStream(s.getOutputStream());
			Scanner scanner = new Scanner(System.in);
			Thread sendMessage = new Thread(new Runnable() {
				@Override
				public void run() {
					while(true) {
						try {
							String clearMessage = scanner.nextLine();
							//String cipheredMessage = new String(Cryptography.encrypt(clearMessage.getBytes(), ));
							
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
					while(true) {
						try {
							String message = input.readUTF();
							System.out.println(message);
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