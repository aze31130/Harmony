import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Base64;

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

        //Server sends public key in base 64 or in raw binary ?
        //Base64.getEncoder().encode(pubKey.getEncoded())


        try {
            System.out.println("Sending public key");
            this.output.writeUTF(Base64.getEncoder().encode(Server.getInstance().keyPair.getPublic().getEncoded()).toString());

        } catch(IOException exception) {
            exception.printStackTrace();
        }
        

        while(true) {
            try {
                String rawStringReceived = this.input.readUTF();

                System.out.println(Server.getInstance().keyPair.getPublic());

                byte[] encoded = Cryptography.encrypt(rawStringReceived.getBytes(), Server.getInstance().keyPair.getPublic());
            
                System.out.println("DECODED:" + new String(encoded));

                byte[] decoded = Cryptography.decrypt(encoded, Server.getInstance().keyPair.getPrivate());

                System.out.println("DECODED:" + new String(decoded));

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