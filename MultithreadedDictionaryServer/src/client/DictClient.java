/**
 * 
 */
/**
 * @author yuhandan
 *
 */
package client;
import client.ClientGUI;
import status.StatusCode;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import client.ClientRequestThread;
import java.util.concurrent.TimeoutException;
public class DictClient{
	private String IPaddress;
	private int Port;
	private ClientGUI ui;
	private int status;
	private Socket socket;
	public DictClient(String address, int port) {
		this.IPaddress = address;
		this.Port = port;
		
		ui = null;
	}
	public static void main(String args[]) {
		try {
			// Check port format.
			if (Integer.parseInt(args[1]) <= 1024 || Integer.parseInt(args[1]) >= 49151) {
				System.out.println("Invalid Port Number!\n Port number should be between 1024 and 49151!");
				System.exit(-1);
			}
			DictClient client = new DictClient(args[0], Integer.parseInt(args[1]));
			
			client.run();
			
		}
		// the number of args is our of range
		catch(ArrayIndexOutOfBoundsException e) {
			System.out.println("Wrong Parameters:\nPlease run like \"java MultiThreadDictClient.java <server-adress> <server-port>");
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	public void run() {
		try {
			this.socket = new Socket(this.IPaddress, this.Port);
			System.out.println("Connection established");
			this.ui = new ClientGUI(this);
			ui.setVisible(true);
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Please enter <server-adress> <server-port>");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@SuppressWarnings("unchecked")
	public HashMap excute(int operation, String word, ArrayList<String> meanings) {
		int status = -999;
		try {
			
			ClientRequestThread ReqThread = new ClientRequestThread(this.socket, operation, word, meanings);
			ReqThread.start();
			ReqThread.join(2000);
			HashMap res = new HashMap();
			res = ReqThread.getResults();
			System.out.println("Client receive "+res);
			status = (int) res.get("status");
			System.out.println("status"+status);
			meanings = (ArrayList<String>) res.get("meanings");
			System.out.println("Connect Success!");
		}catch (Exception e) {
			e.printStackTrace();
		}
		HashMap results = new HashMap();
		results.put("status", status);
		results.put("meanings", meanings);
		return results;
	}
	// query a word in the dictionary
	public HashMap query(String word) {
		ArrayList<String> meanings = new ArrayList<String>();
//		meanings.add(word);
		HashMap resultMap = new HashMap();
		resultMap = excute(StatusCode.QUERY, word, null);
		return resultMap;
	}
	
	
	public void disconnect() {
		excute(StatusCode.ESC, "", null);
	}
	// add the new word into the dictionary
	public int add(String word, ArrayList<String> meanings) {
		HashMap resultMap = excute(StatusCode.ADD, word, meanings);
		return (int) resultMap.get("status");
	}
	
	public int remove(String word) {
		HashMap resultMap = excute(StatusCode.REMOVE, word, null);
		return (int) resultMap.get("status");
	}
	
	public int update(String word, ArrayList<String> meanings) {
		HashMap resultMap = excute(StatusCode.UPDATE, word, meanings);
		return (int) resultMap.get("status");
	}
}