package server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import client.ClientGUI;
import server.DictServer;

public class ServerGUI extends JFrame{
	private JPanel p;
	private JLabel lblAddress, lblPort, txtAddress, txtPort;
	private JTextArea txtRequest;
	private String IPAddress;
	private String Port;

	public ServerGUI(String IPAddress, int Port) {
		this.IPAddress = IPAddress;
		this.Port = String.valueOf(Port);
		initialFrame();
	}
	public JTextArea getLogTextArea() {
		return txtRequest;
	}
	public void initialFrame() {
		p = new JPanel();
		p.setLayout(null);
		lblAddress = new JLabel("IP Address");
		lblPort = new JLabel("Port");
		txtAddress = new JLabel(this.IPAddress);
		txtPort = new JLabel(this.Port);
		txtRequest = new JTextArea(20,30);

		// x,y, w,h
		lblAddress.setBounds(30, 30, 60, 25);
		txtAddress.setBounds(100, 30, 100, 25);
		lblPort.setBounds(30, 60, 60, 25);
		txtPort.setBounds(100, 60, 100, 25);
		txtRequest.setBounds(40, 100, 300, 400);
		
		p.add(lblAddress);
		p.add(lblPort);
		p.add(txtAddress);
		p.add(txtPort);
		p.add(txtRequest);

		this.add(p);
		this.setSize(350, 270);
		this.setLocation(400, 600);		
		}

}
