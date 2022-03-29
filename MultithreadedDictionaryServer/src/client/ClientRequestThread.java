package client;
import status.ExceptionCode;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONObject;

import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import status.OperationCode;
import client.DictClient;
public class ClientRequestThread extends Thread{
	private int operation;
	private int port;
	private String address;
	static private String word;
	static private ArrayList<String> meanings;
	private Socket socket;
	private int status = -999;
	private HashMap results;
	private DictClient client;
	public ClientRequestThread(DictClient client, Socket socket, int operation, String word, ArrayList<String> meanings) {
		this.client = client;
		this.socket = socket;
		this.operation = operation;
		this.word = word;
		this.meanings = meanings;
	}
	// create request
	public JSONObject createRequest() {
		JSONObject request = new JSONObject();
		request.put("operation", this.operation);
		request.put("word", this.word);
		request.put("meanings", this.meanings);
		return request;
	}
	// parse string request to json
	public JSONObject parseRequest(String res) {
		JSONObject resString = null;
		try {
			JSONParser parser = new JSONParser();
			resString = (JSONObject) parser.parse(res);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resString;	
	}


	public void run() {
		ArrayList receive_meaning = new ArrayList();
		try {
			DataInputStream in = new DataInputStream(socket.getInputStream());
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			// send request
			out.writeUTF(createRequest().toString());
			out.flush(); 
			// print log
			client.printLog("**Send Message**", createRequest().toString());
			// disconnect
			if (this.operation == OperationCode.DISCONNECT) {
				in.close();
				out.close();
				client.printLog("DISCONNECT", "Closed...");
				socket.close();
				System.exit(-1);
			}
			// get reply
			String receive = in.readUTF();
			client.printLog("==Message received==", receive);
			JSONObject receiveJSON = parseRequest(receive);
			status = Integer.parseInt(receiveJSON.get("status").toString());
			if (status == ExceptionCode.SUCCESS) {
				receive_meaning = (ArrayList) receiveJSON.get("meanings");
			}

		}catch(UnknownHostException e){
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		results = new HashMap();
		results.put("status", status);
		results.put("meanings", receive_meaning);
		
	}
	public HashMap getResults() {
		
		return this.results;
	}
	
}
