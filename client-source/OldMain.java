import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import cryptography.Cryptography;

public class OldMain {
	public static final String SERVER_IP = "127.0.0.1";
	public static final int SERVER_PORT = 3378;

	public static PublicKey SERVER_PUB_KEY = null;

	public static KeyPair keyPair = null;

	public static SecretKey symmetricKey = null;

	public static void old_main() {
		System.out.println("Starting client on CLI mode !");

		keyPair = Cryptography.generateKeyPair(512);

		try {
			Socket s = new Socket(SERVER_IP, SERVER_PORT);
			DataInputStream input = new DataInputStream(s.getInputStream());
			DataOutputStream output = new DataOutputStream(s.getOutputStream());
			Scanner scanner = new Scanner(System.in);

			//send client's pub key
			try {
				//Length of the message
				output.writeUTF(Integer.toString(keyPair.getPublic().getEncoded().length)); 
				output.write(keyPair.getPublic().getEncoded());

				//Getting AES key
				int length = Integer.parseInt(input.readUTF());
				System.out.println(length);
				if ((length <= 0) || (length > 32768))
					System.err.println("Abort mission");
					
				byte[] message = new byte[length];
				input.readFully(message, 0, message.length);

				//Decrypt AES key sent by the server
				Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
				cipher.init(Cipher.PRIVATE_KEY, keyPair.getPrivate());
				byte[] decryptedKey = cipher.doFinal(message);
				symmetricKey = new SecretKeySpec(decryptedKey , 0, decryptedKey.length, "AES");
				System.out.println(symmetricKey.hashCode());
				System.out.println(new String(symmetricKey.getEncoded()));
			} catch (IOException e) {
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchPaddingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidKeyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalBlockSizeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (BadPaddingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


			Thread sendMessage = new Thread(new Runnable() {
				@Override
				public void run() {
					while(true) {
						try {
							String clearMessage = scanner.nextLine();

							Cipher aesCipher = Cipher.getInstance("AES");
							aesCipher.init(Cipher.ENCRYPT_MODE, symmetricKey);
							byte[] byteCipherText = aesCipher.doFinal(clearMessage.getBytes());

							output.writeUTF(Integer.toString(byteCipherText.length));
							output.write(byteCipherText);
						} catch(IOException e) {
							e.printStackTrace();
						} catch (NoSuchAlgorithmException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (NoSuchPaddingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InvalidKeyException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalBlockSizeException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (BadPaddingException e) {
							// TODO Auto-generated catch block
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
							int length = Integer.parseInt(input.readUTF());
							byte[] rawStringReceived = new byte[length];

							input.readFully(rawStringReceived, 0, rawStringReceived.length);
							//Use the symmetric key to decrypt
							Cipher aesCipher2 = Cipher.getInstance("AES");
							aesCipher2.init(Cipher.DECRYPT_MODE, symmetricKey);
							byte[] bytePlainText = aesCipher2.doFinal(rawStringReceived);
							String decryptedMessage = new String(bytePlainText);

							System.out.println(decryptedMessage);
						} catch(IOException e) {
							e.printStackTrace();
							break;
						} catch (NoSuchAlgorithmException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (NoSuchPaddingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalBlockSizeException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (BadPaddingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InvalidKeyException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
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
