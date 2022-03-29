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
import status.ExceptionCode;
public class AddUpdateGUI  extends JFrame{
	private DictClient client;
	private JLabel lblWord, lblMeaning;
	private JButton btnAdd, btnUpdate, btnAddMeaning;
	private JTextField txtWord, txtMeanings;
	private String nameTField, showMeaningField;
	private ArrayList<JTextField> MeaningsFields;
	private ArrayList<String> Meanings;
	private int count = 0;
	public AddUpdateGUI(DictClient client) {
		setSize(500,290);
		setVisible(true);
		Container mk=getContentPane();
		mk.setLayout(null);
		Meanings = new ArrayList<String>();
		MeaningsFields = new ArrayList<JTextField>();
		lblWord = new JLabel("word");
		txtWord = new JTextField(20);
		lblMeaning = new JLabel("Meaning");
		btnAdd = new JButton("Add");
		btnUpdate = new JButton("Update");
		btnAddMeaning = new JButton("+");
		lblWord.setBounds(30, 30, 60, 25);
		txtWord.setBounds(100, 30, 100, 25);
		lblMeaning.setBounds(30, 60, 60, 25);
		btnAdd.setBounds(200, 30, 100, 25);
		btnUpdate.setBounds(300,30, 100,25);
		btnAddMeaning.setBounds(90,60, 60, 25 );
		mk.add(lblWord);
		mk.add(txtWord);
		mk.add(lblMeaning);
		mk.add(btnAddMeaning);
		btnAddMeaning.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == btnAddMeaning) {
					txtMeanings = new JTextField();
					txtMeanings.setName(nameTField + count);
					
					MeaningsFields.add(txtMeanings);
					count++;
					txtMeanings.setBounds(140, 60 + 25 * (count-1), 120, 25);
					mk.add(txtMeanings);
					mk.revalidate();
					mk.repaint();
					}
			}
			
		});
		mk.add(btnAdd);
		

		btnAdd.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				count = 0;
				
				if (e.getSource() == btnAdd) {
					Meanings =  new ArrayList<String>();
					String word = txtWord.getText().toLowerCase();

					for(JTextField txt:MeaningsFields) {
						if (txt.getText().equals("")){
							continue;
						}
						String text = txt.getText().toLowerCase();
						Meanings.add(text);
					}
					// meaning empty
					if (MeaningsFields.isEmpty()) {
//						client.printLog("ERROR","At least a meaning!");
						JOptionPane.showMessageDialog(null, "Please enter a meaning!", "", JOptionPane.ERROR_MESSAGE);
						
						return;
					}
					int Status = client.add(word, Meanings);
					
					
					
					// duplicate
					if (Status == ExceptionCode.EXIST) {
//						client.printLog("ERROR", "The word has already existed!");
						JOptionPane.showMessageDialog(null, "The word has already existed!\n Do you want to update?", "", JOptionPane.ERROR_MESSAGE);
						

						return;
					}
					// success
//					client.printLog("INFO", "Success to add!"); 
					JOptionPane.showMessageDialog(null, "Succeed to add!", "", JOptionPane.INFORMATION_MESSAGE);
					
					if (MeaningsFields!=null) {
						for(JTextField txt:MeaningsFields) {
							if (txt!=null) {
								mk.remove(txt);
								mk.revalidate();
								mk.repaint();
							}
						}
						}
					MeaningsFields = new ArrayList<JTextField>();
					}
				
			}
		});
		
		mk.add(btnUpdate);
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Meanings = new ArrayList<String>();
				if (e.getSource() == btnUpdate) {
					count = 0;  
					String word = txtWord.getText().toLowerCase();
					for(JTextField txt:MeaningsFields) {
						if (txt.getText().equals("")) {
							continue;
						}
						String text = txt.getText().toLowerCase();
						Meanings.add(text);
					}
					if (Meanings.isEmpty()) {
//						client.printLog("ERROR", "At least a meaning.");
						JOptionPane.showMessageDialog(null, "Please enter a meaning!", "", JOptionPane.ERROR_MESSAGE);
						
						return;
					}
					int Status = client.update(word, Meanings);
					if (Status == ExceptionCode.UNSEEN) {
						JOptionPane.showMessageDialog(null, "There is no such word!\n Do you want to add it?", "", JOptionPane.ERROR_MESSAGE);
//						client.printLog("ERROR", "Not found the word!");
						return;
					}
//					client.printLog("INFO", "Update success!");
					JOptionPane.showMessageDialog(null, "Succeed to update!", "", JOptionPane.INFORMATION_MESSAGE);
					
					if (MeaningsFields!=null) {
						for(JTextField txt:MeaningsFields) {
							if (txt!=null) {
								mk.remove(txt);
								mk.revalidate();
								mk.repaint();
							}
						}
						}
					MeaningsFields = new ArrayList<JTextField>();
					}
				
			}
		});
	}
}
