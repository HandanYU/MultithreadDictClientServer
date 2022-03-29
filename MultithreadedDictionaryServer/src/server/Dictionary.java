/**
 * 
 */
/**
 * @author yuhandan
 *
 */
package server;
import java.io.BufferedReader;
import status.ExceptionCode;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import status.OperationCode;
public class Dictionary{
	private String path = "dict.dat";
	private HashMap<String, ArrayList> dictionary;
	public Dictionary(String p) {
		path = p;
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));
			dictionary = (HashMap<String, ArrayList>) ois.readObject();
			ois.close();
		} catch (ClassNotFoundException e) {
			System.out.println("Error: Wrong file format! Run default dictionary.");
			setDefaultDict();
		} catch (FileNotFoundException e) {
			System.out.println("Error: No such file! Run default dictionary.");
			setDefaultDict();
		} catch (Exception e) {
			System.out.println("Error: Unknown error, " + e.getMessage());
			e.printStackTrace();
		}
	}
	private void setDefaultDict() {
		path = "dict.dat";
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));
			dictionary = (HashMap<String, ArrayList>) ois.readObject();
			ois.close();
		} catch (FileNotFoundException e) {
			System.out.println("Default Dictionary not Exist, Create a new one.");
			createNewDict(this.path);
		} catch (Exception e) {
			System.out.println("Error: Unknown error, " + e.getMessage());
			e.printStackTrace();
		}
	}
	private void createNewDict(String dictPath) {
		dictionary = new HashMap<String, ArrayList>();
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dictPath));
			oos.writeObject(dictionary);
			oos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public Dictionary() {
		path = "";
		dictionary = new HashMap<String, ArrayList>();
	}
	public synchronized Boolean isWordExist(String word) {
		if (dictionary.containsKey(word)) {
			return true;
		}else {
			return false;
		}
	}
	// search a word's meaning(s) in the dictionary
	public synchronized ArrayList query(String word) {
		if (dictionary.containsKey(word)) {
			ArrayList meaningsArray = new ArrayList();
			meaningsArray = dictionary.get(word);
			
			return meaningsArray;
		}else {
			ArrayList error = new ArrayList();
			error.add("There is no such word!");
			return error;
		}
		
	}
	// add a word and its meaning(s) into the dictionary
	public synchronized int add(String word, ArrayList meanings) {
		System.out.println(meanings);
		// if the word exists in dictionary
		if (dictionary.containsKey(word)) {
			return ExceptionCode.EXIST;
		}
		// if there is no any meanings input
		if (meanings.isEmpty()) {
			return ExceptionCode.EMPTY;
		}
		// if success: return status 1
		else {
				dictionary.put(word, meanings);
				try {
					ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path));
					out.writeObject(dictionary);
					out.close();
				}catch(Exception e) {
					e.printStackTrace();
				}
				return ExceptionCode.SUCCESS;
		}
		
	}
	// remove a word and its meaning in the dictionary
	public synchronized int remove(String word) {
		if (dictionary.containsKey(word)) {
			dictionary.remove(word);
			try {
				ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path));
				out.writeObject(dictionary);
				out.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
			return ExceptionCode.SUCCESS;
		}else {
			return ExceptionCode.UNSEEN;
		}
	}
	// update the existing word
	public synchronized int update(String word, ArrayList meanings) {
		if (dictionary.containsKey(word)) {
			dictionary.put(word, meanings);
			System.out.println("word"+dictionary.get(word));
			try {
				ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path));
				out.writeObject(dictionary);
				out.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
			return ExceptionCode.SUCCESS;
		}else {
			return ExceptionCode.UNSEEN;
		}
	}

}
