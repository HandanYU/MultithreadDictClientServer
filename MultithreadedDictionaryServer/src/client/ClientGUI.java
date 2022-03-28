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
public class ClientGUI extends JFrame{
	private JPanel p;
	private JLabel lblWord, lblMeaning;
	private JTextField txtWord, txtMeaning,txtMeaning1, txtMeanings;
	private JButton btnQuery, btnRemove, btnAdd, btnAddMeaning, btnESC;
	private String nameTField;
	private ArrayList<JTextField> MeaningsFields;
	private ArrayList<String> Meanings;
	private int count;
	private DictClient client;
	public ClientGUI(DictClient client) {
		this.client = client;
		initialFrame();
	}
	public void initialFrame() {
		count = 0;
		nameTField = "tField";
		p = new JPanel();
		p.setLayout(null);
		MeaningsFields = new ArrayList<JTextField>();
		Meanings = new ArrayList<String>();
		lblWord = new JLabel("word");
		lblMeaning = new JLabel("meanings");
		txtWord = new JTextField(20);
		txtMeaning = new JTextField(100);
		MeaningsFields.add(txtMeaning);
		btnQuery = new JButton("Query");
		btnRemove = new JButton("Remove");
		btnAdd = new JButton("Add");
		btnAddMeaning = new JButton("+");
		btnESC = new JButton("ESC");
		// left x,y , width, height
		lblWord.setBounds(30, 30, 60, 25);
		txtWord.setBounds(95, 30, 120, 25);
		btnQuery.setBounds(250, 30, 100, 25);
		btnRemove.setBounds(350, 30, 100, 25);		
		lblMeaning.setBounds(30, 60, 100, 25);
		txtMeaning.setBounds(95, 60, 120, 25);

		btnAddMeaning.setBounds(220, 60, 50, 25);
		btnAdd.setBounds(260, 60, 60, 25);
		btnESC.setBounds(300, 60, 60, 25);
		p.add(lblWord);
		p.add(lblMeaning);
		p.add(txtWord);
		p.add(txtMeaning);
		p.add(btnQuery);
		
		// query the word in the dictionary
		btnQuery.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == btnQuery) {
//					txtMeaning.setVisible(false);
					String word = txtWord.getText();
					HashMap res = new HashMap<String, ArrayList<String>>();
					res = client.query(word);
					System.out.println("+_+_"+res);
					@SuppressWarnings("unchecked")
					ArrayList<String> meanings = (ArrayList<String>) res.get("meanings");
					String str = "";
					
					for (String meaning : meanings) {
						txtMeaning.setVisible(false);
						txtMeanings = new JTextField();
						txtMeanings.setName(nameTField + count);
						MeaningsFields.add(txtMeanings);
						count++;
						txtMeanings.setBounds(95, 60 + 25 * (count-1), 120, 25);
						
						p.add(txtMeanings);
						p.revalidate();
						p.repaint();
						txtMeanings.setText(meaning);
//						str += meaning;
//						str += "\n";
					}
//					System.out.println(":::"+str);
//					txtMeaning.setText(str);
					
					}
			}
			
		});
		p.add(btnAddMeaning);
		btnAddMeaning.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == btnAddMeaning) {
					
					txtMeanings = new JTextField();
					txtMeanings.setName(nameTField + count);
					MeaningsFields.add(txtMeanings);
					count++;
					txtMeanings.setBounds(95, 60 + 25 * count, 120, 25);
					p.add(txtMeanings);
					p.revalidate();
					p.repaint();
					}
			}
			
		});
		
		p.add(btnRemove);
		btnRemove.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == btnRemove) {
					String word = txtWord.getText();
					System.out.println(word);
					}
			}
			
		});
		
		p.add(btnAdd);
		
		btnAdd.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == btnAdd) {
					String word = txtWord.getText();
					for(JTextField txt:MeaningsFields) {
						String text = txt.getText();
						Meanings.add(text);
					}					
					int Status = client.add(word, Meanings);
					System.out.println(Status);
					}
				
			}
		});
		p.add(btnESC);
		btnESC.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == btnESC) {
					client.disconnect();
					}
				
			}
		});
		this.add(p);
		this.setSize(500, 170);
		this.setLocation(300, 300);		
	
	}

}
