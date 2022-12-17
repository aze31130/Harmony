package models;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.KeyPair;
import java.security.PublicKey;

import javax.crypto.SecretKey;

import cryptography.Cryptography;

public class Server {
    public String ip;
    public int port;

    public Thread thread;
    public Socket socket;
    public DataInputStream input;
    public DataOutputStream output;

    public PublicKey serverPubKey;
    public KeyPair clientKeys;
    public KeyPair tempKeys;
    public SecretKey symmetricKey;

    public Server(String ip, int port) {
        this.thread = new Thread();
        this.ip = ip;
        this.port = port;
    }

	/*
	 * This methods waits for the server to send a byte array
	 */
	public byte[] receive() throws IllegalArgumentException, IOException {
		//Getting server's message length
		int messageLength = this.input.readInt();

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
	public void send(byte[] data) {
		try {
			this.output.writeInt(data.length);
			this.output.write(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

    public Boolean connect() {
        System.out.println("Connecting to " + this.ip + ":" + this.port);
        try {
            this.socket = new Socket(this.ip, this.port);
            this.input = new DataInputStream(this.socket.getInputStream());
			this.output = new DataOutputStream(this.socket.getOutputStream());

            this.handshake();
            
            return true;
        } catch (IOException e) {
            System.err.println("Cannot connect to host " + this.ip);
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void handshake() throws IllegalAccessError, IOException {
        this.tempKeys = Cryptography.generateKeyPair(4096);
        this.send(tempKeys.getPublic().getEncoded());
        byte[] aesKey = this.receive();
        byte[] decryptedkey = Cryptography.decrypt(this.tempKeys.getPrivate(), aesKey);
        
        this.symmetricKey = Cryptography.loadSymmetricKey(decryptedkey);

        //Once the connection is esthablished and the symmetric key is generated, we can authenticate the client

        //For instance, we can send a password or a public key encrypted with the symmetric key
        System.out.println(symmetricKey.hashCode());
        System.out.println(new String(symmetricKey.getEncoded()));
    }
}
