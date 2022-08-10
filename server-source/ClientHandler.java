import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import users.User;

public class ClientHandler implements Runnable {
    public User user;
    public Thread thread;
    public Socket socket;
    public DataInputStream input;
    public DataOutputStream output;

    public Boolean isLoggedIn = false;

    public ClientHandler(Socket s) {
        this.thread = new Thread(this);
        this.socket = s;
        try {
            this.input = new DataInputStream(s.getInputStream());
            this.output = new DataOutputStream(s.getOutputStream());
            thread.start();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while(!this.isLoggedIn) {
            //get ip address of client, if the ip appears in the ban list then kill the handler
            //get pubkey of client
            //check if the key is registered in only one profile
            //if the key is not registered then kill the handler
            //if the name of the account appear in the ban list then kill the handler
            //generate random string
            //ciphers it with client's pub key
            //send it to client
            //client decode it
            //client ciphers it with server's pub key
            //client send the string back to the server
            //sever decode it, if the string is the same then accept the connection else kill the handler
            //
            //If this procedure last for more than 10 seconds then kill the handler

            //TODO
            break;
        }

        while(true) {
            try {
                String received = this.input.readUTF();

                // unserialize input and trigger an event

                System.out.println(received);

                //Broadcast it to other clients
                for (ClientHandler client : Server.getInstance().onlineUsers) {
                    if (client != this)
                        client.output.writeUTF(received);
                }

            } catch(IOException e) {
                Server.getInstance().onlineUsers.remove(this);
                System.out.println("Client disconnected !");
                break;
            }
        }
    }
}