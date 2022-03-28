package client;

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
import status.StatusCode;
public class ClientRequestThread extends Thread{
	private int operation;
	private int port;
	private String address;
	static private String word;
	static private ArrayList<String> meanings;
	private Socket socket;
	private int status = -999;
	private HashMap results;
	public ClientRequestThread(Socket socket, int operation, String word, ArrayList<String> meanings) {
		this.socket = socket;
		this.operation = operation;
		this.word = word;
		this.meanings = meanings;
//		socket = null;
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
	public HashMap getResults() {
		
		return results;
	}

	public void run() {
		try {
			DataInputStream in = new DataInputStream(socket.getInputStream());
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			
				
			System.out.println(createRequest().toString());
			out.writeUTF(createRequest().toString());
			out.flush(); // send request
			System.out.println("send!");
			String receive = in.readUTF();
			System.out.println("+++++++++"+receive);
			JSONObject receiveJSON = parseRequest(receive);
			status = Integer.parseInt(receiveJSON.get("status").toString());
			System.out.println(status);
			if (status == StatusCode.SUCCESS) {
				meanings = (ArrayList) receiveJSON.get("meanings");
			}
			if (status == StatusCode.ESC) {
				in.close();
				out.close();
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
		results.put("meanings", meanings);
		System.out.println("_____________"+results);
	}
	
}
