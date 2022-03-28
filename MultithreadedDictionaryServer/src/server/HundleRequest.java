package server;
import status.StatusCode;
import server.DictServer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import status.StatusCode;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import client.DictClient;
public class HundleRequest extends Thread{
	private Dictionary dict;
	private Socket clientSocket;
	private DictServer dictServer;
	public HundleRequest(DictServer server, Socket clientSocket, Dictionary dict) {
		this.dictServer = server;
		this.clientSocket = clientSocket;
		this.dict = dict;
	}
	// create a reply in the certain format
	public JSONObject createReply(int status, ArrayList meanings) {
		JSONObject reply = new JSONObject();
		reply.put("status", status);
		reply.put("meanings", meanings);
		return reply;
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

	public String status2str(int status) {
		String str = "NULL";
		switch(status) {
		case StatusCode.QUERY:
			str = "QUERY";
			break;
		case StatusCode.ADD:
			str = "ADD";
			break;
		case StatusCode.REMOVE:
			str = "REMOVE";
			break;
		case StatusCode.UPDATE:
			str = "UPDATE";
			break;
		default:
			break;
		}
		return str;
	}
	public void run() {
		try {
			
			DataInputStream in = new DataInputStream(clientSocket.getInputStream());
			DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
			
			
			ArrayList meanings = new ArrayList();
			meanings.add("Hello client, welcome to use the dictionary!");
			int status = StatusCode.CONNECT;
			out.writeBytes(createReply(status, meanings).toString());
			out.flush();
			
			while (status!=StatusCode.ESC) {

			// receive the request
			String receive = in.readUTF();

			JSONObject receiveJSON = parseRequest(receive);

			int operation = Integer.parseInt(receiveJSON.get("operation").toString());
			
			String word = (String) receiveJSON.get("word");

			meanings = (ArrayList) receiveJSON.get("meanings");
			String logStr = "A client requests to " + status2str(operation) + " " + word;
			dictServer.printLog(logStr);
			status = StatusCode.FAIL;	
			switch(operation) {
			case StatusCode.ESC:
				status = StatusCode.ESC;
				// say goodbye
				break;
			case StatusCode.QUERY:
				
				if (dict.isWordExist(word)) {
					meanings = dict.query(word);
					status = StatusCode.SUCCESS;
				}
				out.writeUTF(createReply(status, meanings).toString());
				out.flush();
				break;
			case StatusCode.ADD:
				
				if(!dict.isWordExist(word)) {
					System.out.println("++++++++++");
					status = dict.add(word, meanings);
					System.out.println(status);
				}
				out.writeUTF(createReply(status, null).toString());
				out.flush();
				break;
			case StatusCode.REMOVE:
				if(dict.isWordExist(word)) {
					status = dict.remove(word);
				}
				out.writeUTF(createReply(status, null).toString());
				out.flush();
				break;
			case StatusCode.UPDATE:
				if (dict.isWordExist(word)) {
					status = dict.update(word, meanings);
				}
				out.writeUTF(createReply(status, meanings).toString());
				out.flush();
				break;
			default:
				break;
			}}
			in.close();
			out.close();
			clientSocket.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	
		
}
