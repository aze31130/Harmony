package views;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;

import models.Server;

public class MainMenu extends JFrame implements Action {
	
	public JButton addServer;
	public JButton editServer;
	public JButton deleteServer;
	public JButton connect;

    public MainMenu() {
		this.setTitle("Harmony client");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setLayout(null);
		this.setSize(1280, 720);

		connect = new JButton();
		connect.addActionListener(this);
		connect.setBounds(100, 100, 250, 100);
		connect.setText("Connect to Harmony");
		connect.setEnabled(true);
		this.add(connect);
    }

	@Override
	public void actionPerformed(ActionEvent event) {
		String serverName = "Harmony";
		String host = "127.0.0.1";
		int port = 3378;
		if (event.getSource() == this.connect) {
			connect.setEnabled(false);
			Server harmony = new Server(serverName, host, port);
			if (harmony.connect()) {
				//Change to server window
				this.dispose();
				new ServerVue(harmony);
			} else {
				System.err.println("Cannot connect to host " + host + " on port " + port + ".");
				connect.setEnabled(true);
			}
		}
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
