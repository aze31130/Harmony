package models;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.KeyPair;
import java.security.PublicKey;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import cryptography.Cryptography;

public class Server {
    public String name;
    public String ip;
    public int port;

    public Socket socket;
    public DataInputStream input;
    public DataOutputStream output;

    public PublicKey serverPubKey;
    public KeyPair clientKeys;
    public KeyPair tempKeys;
    public SecretKey symmetricKey;

    public Server(String name, String ip, int port) {
        this.name = name;
        this.ip = ip;
        this.port = port;
    }

	/*
	 * This methods waits for the server to send a byte array
	 */
	public byte[] receive() throws IllegalArgumentException, IOException {
		//Getting server's message length
		int messageLength = Integer.parseInt(this.input.readUTF());

		//If message length is invalid
		if ((messageLength < 0) || (messageLength > 10000000))
			throw new IllegalArgumentException("Message length invalid");

		byte[] message = new byte[messageLength];
		this.input.readFully(message, 0, message.length);
		return message;
	}

	/*
	 * This methods sends to the current server a byte array
	 */
	public void send(byte[] message) {
		try {
			this.output.writeUTF(Integer.toString(message.length));
			this.output.write(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

    public void connect() {
        System.out.println("Connecting to " + this.ip + ":" + this.port);
        try {
            this.socket = new Socket(this.ip, this.port);
            this.input = new DataInputStream(this.socket.getInputStream());
			this.output = new DataOutputStream(this.socket.getOutputStream());

            this.handshake();

        } catch (IOException e) {
            System.err.println("Cannot connect to host " + this.ip);
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public void handshake() throws IllegalAccessError, IOException {
        this.tempKeys = Cryptography.generateKeyPair(2048);

        this.send(tempKeys.getPublic().getEncoded());

        byte[] aesKey = this.receive();
        byte[] decryptedkey = Cryptography.decrypt(this.tempKeys.getPrivate(), aesKey);
        
        this.symmetricKey = new SecretKeySpec(decryptedkey, 0, decryptedkey.length, "AES");
        System.out.println(symmetricKey.hashCode());
        System.out.println(new String(symmetricKey.getEncoded()));
    }
}
