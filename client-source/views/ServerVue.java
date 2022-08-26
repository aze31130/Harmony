package views;

import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;

import models.Server;

public class ServerVue extends JFrame implements Action {
    public Server server;
    public JButton send;
    
    public ServerVue(Server server) {
        this.server = server;
        this.setTitle("Harmony client");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setLayout(null);
		this.setSize(1280, 720);

        this.send = new JButton();

    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Object getValue(String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void putValue(String arg0, Object arg1) {
        // TODO Auto-generated method stub
        
    }
}
