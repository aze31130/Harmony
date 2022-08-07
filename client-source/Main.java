import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Main {

	public static final String SERVER_IP = "127.0.0.1";
	public static final int SERVER_PORT = 3378;

	public static void main(String[] args) {
		System.out.println("Starting client on CLI mode !");
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
							String message = scanner.nextLine();
							output.writeUTF(message);
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
			e.printStackTrace();
		}
	}
}