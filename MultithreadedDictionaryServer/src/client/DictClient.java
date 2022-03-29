/**
 * 
 */
/**
 * @author yuhandan
 *
 */
package client;
import client.MainGUI;
import status.OperationCode;
import status.ExceptionCode;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import client.ClientRequestThread;
import java.util.concurrent.TimeoutException;
public class DictClient{
	private String IPaddress, name;
	private int Port;
	private MainGUI ui;
	private int status;
	private Socket socket;
	public DictClient(String address, int port) {
		this.IPaddress = address;
		this.Port = port;
		ui = null;
	}
	public void printLog(String state, String msg) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		String date_info = "["+df.format(new Date()) +"]: ";
		System.out.println("\n"+date_info + state + ": " +msg);
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
			System.out.println("Wrong Parameters:\nPlease run like \"java -jar DictClient.jar <server-adress> <server-port>");
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	public void run() {
		try {
			this.socket = new Socket(this.IPaddress, this.Port);
			this.ui = new MainGUI(this);
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
		ArrayList<String> receive_meanings = new ArrayList<String>();
		receive_meanings = null;
		try {
			
			ClientRequestThread ReqThread = new ClientRequestThread(this, this.socket, operation, word, meanings);
			ReqThread.start();
			ReqThread.join(2000);
			HashMap res = new HashMap();
			res = ReqThread.getResults();

			status = (int) res.get("status");
			if (status == ExceptionCode.SUCCESS) {
				 receive_meanings = (ArrayList<String>) res.get("meanings");
			}

		}catch (Exception e) {
			e.printStackTrace();
		}
		HashMap results = new HashMap();
		results.put("status", status);
		results.put("meanings", receive_meanings);
		return results;
	}
	// query a word in the dictionary
	public HashMap query(String word) {
		ArrayList<String> meanings = new ArrayList<String>();
		HashMap resultMap = new HashMap();
		resultMap = excute(OperationCode.QUERY, word, null);
		return resultMap;
	}
	
	
	public void disconnect() {
		excute(OperationCode.DISCONNECT, "", null);
	}
	// add the new word into the dictionary
	public int add(String word, ArrayList<String> meanings) {
		HashMap resultMap = excute(OperationCode.ADD, word, meanings);
		return (int) resultMap.get("status");
	}
	
	public int remove(String word) {
		HashMap resultMap = excute(OperationCode.REMOVE, word, null);
		return (int) resultMap.get("status");
	}
	
	public int update(String word, ArrayList<String> meanings) {
		HashMap resultMap = excute(OperationCode.UPDATE, word, meanings);
		return (int) resultMap.get("status");
	}
}