package client;
// convert all words into lowercase letters.
import java.awt.*;
import status.ExceptionCode;
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
public class QueryDeleteGUI extends JFrame{
	private DictClient client;
	private JLabel lblWord, lblMeaning;
	private JTextField txtWord, txtMeanings;
	private JButton btnQuery, btnDelete;
	private String showMeaningField;
	private ArrayList<JTextField> ShowMeaningsFields;
	public QueryDeleteGUI(DictClient client) {
		setSize(500,290);
		setVisible(true);
		Container mk=getContentPane();
		mk.setLayout(null);
		showMeaningField = "mField";
		ShowMeaningsFields = new ArrayList<JTextField>();
		lblWord = new JLabel("word");
		txtWord = new JTextField(20);
		lblMeaning = new JLabel("lblMeaning");
		btnQuery = new JButton("Search");
		btnDelete = new JButton("Delete");
		lblWord.setBounds(30, 30, 60, 25);
		txtWord.setBounds(100, 30, 100, 25);
		btnQuery.setBounds(200, 30, 100, 25);
		btnDelete.setBounds(300,30, 100,25);
		mk.add(lblWord);
		mk.add(txtWord);
		mk.add(lblMeaning);
		mk.add(btnQuery);
		btnQuery.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == btnQuery) {
					int num_meaning = 0;
					String word = txtWord.getText().toLowerCase();
					if (ShowMeaningsFields!=null) {
						for(JTextField txt:ShowMeaningsFields) {
							if (txt!=null) {
								mk.remove(txt);
								mk.revalidate();
								mk.repaint();
							}
						}
						}
						ShowMeaningsFields = new ArrayList<JTextField>();
					HashMap res = new HashMap<String, ArrayList<String>>();
					res = client.query(word);
					int status = (int) res.get("status");
					// not found
					if (status == ExceptionCode.UNSEEN) {
						client.printLog("ERROR", "Not found the word!");
						JOptionPane.showMessageDialog(null, "There is no such word\n Do you want to add it?", "", JOptionPane.ERROR_MESSAGE);
						
						return;
					}

					@SuppressWarnings("unchecked")
					ArrayList<String> meanings = (ArrayList<String>) res.get("meanings");

					for (String meaning : meanings) {
						txtMeanings = new JTextField();
						txtMeanings.setName(showMeaningField + num_meaning);
						ShowMeaningsFields.add(txtMeanings);
						num_meaning++;
						txtMeanings.setBounds(95, 60 + 25 * (num_meaning-1), 120, 25);
						
						mk.add(txtMeanings);
						mk.revalidate();
						mk.repaint();
						txtMeanings.setText(meaning);

					}

					
					}
			}
			
		});
		
		
		mk.add(btnDelete);
		btnDelete.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == btnDelete) {
					String word = txtWord.getText().toLowerCase();
					
					int Status = client.remove(word);
					// not found
					if (Status == ExceptionCode.UNSEEN) {
						client.printLog("ERROR", "Not found the word!");
						JOptionPane.showMessageDialog(null, "no such word", "", JOptionPane.ERROR_MESSAGE);
						
						return;
					}


					if (ShowMeaningsFields!=null) {
					for(JTextField txt:ShowMeaningsFields) {
						if (txt!=null) {
							mk.remove(txt);
							mk.revalidate();
							mk.repaint();
						}
					}
					}
					ShowMeaningsFields = new ArrayList<JTextField>();

					// success
					client.printLog("INFO", "Succeed to delete!");
					JOptionPane.showMessageDialog(null, "Succeed to delete!", "", JOptionPane.INFORMATION_MESSAGE);
					
					}
			}
			
		});
		
		
	}
}
