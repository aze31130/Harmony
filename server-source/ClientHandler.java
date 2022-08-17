import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.security.PublicKey;

import cryptography.Cryptography;
import json.JSONException;
import json.JSONObject;
import json.JSONTokener;
import plugins.Plugin;
import users.User;

public class ClientHandler implements Runnable {
    public User user;
    public Thread thread;
    public Socket socket;
    public DataInputStream input;
    public DataOutputStream output;
    public String secret;
    public String salt;

    public Boolean isLoggedIn = false;

    public ClientHandler(Socket s) {
        this.thread = new Thread(this);
        this.socket = s;
        this.secret = Cryptography.generateSecureRandomString(32);
        this.salt = Cryptography.generateSecureRandomString(32);
        try {
            this.input = new DataInputStream(s.getInputStream());
            this.output = new DataOutputStream(s.getOutputStream());
            thread.start();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public Boolean handshake() {
        //get ip address of client, if the ip appears in the ban list then kill the handler
        while(!this.isLoggedIn) {
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


        try {
            System.out.println("Sending public key (hashcode:" + Server.getInstance().keyPair.getPublic().getEncoded().hashCode() + ")");
            this.output.write(Server.getInstance().keyPair.getPublic().getEncoded());
        } catch(IOException exception) {
            exception.printStackTrace();
        }

        //Server retrieve client's pub key
        try {
            System.out.println("Getting public key");
            byte[] receivedRaw = new byte[1024];
            input.read(receivedRaw);

            PublicKey clientPubKey = Cryptography.loadPublicKey(receivedRaw);
            this.user = new User();
            this.user.pubKey = clientPubKey;

            System.out.println("Received RAW: " + clientPubKey.getEncoded().hashCode());
            System.out.println("Received RAW: " + clientPubKey.hashCode());
        } catch(IOException exception) {
            exception.printStackTrace();
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
    }
}