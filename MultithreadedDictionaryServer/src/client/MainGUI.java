package client;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import client.DictClient;
import status.OperationCode;
import client.QueryDeleteGUI;
import client.AddUpdateGUI;
public class MainGUI extends JFrame{
	private DictClient client;
	
	private JButton queryDelete, addUpdate, btnESC;
	public MainGUI(DictClient client){
		setSize(500,290);
		setVisible(true);
		Container mk=getContentPane();
		mk.setLayout(null);
		queryDelete = new JButton("Query/Delete");
		addUpdate = new JButton("Add/Update");
		btnESC = new JButton("ESC");
		queryDelete.setBounds(20, 30, 100, 50);
		addUpdate.setBounds(130, 30, 100, 50);
		btnESC.setBounds(240, 30, 100, 50);
		mk.add(btnESC);
		btnESC.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == btnESC) {
					client.disconnect();
					}
				
			}
		});
		mk.add(addUpdate);
		addUpdate.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == addUpdate) {
					new AddUpdateGUI(client);
				}
			}
		});
		mk.add(queryDelete);
		queryDelete.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == queryDelete) {
					new QueryDeleteGUI(client);
				}
			}
		});
	}
}
