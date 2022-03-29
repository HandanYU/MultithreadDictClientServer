package server;
import status.ExceptionCode;
import status.OperationCode;
import server.DictServer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import status.OperationCode;
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
	// convert status id to string
	public String status2str(int status) {
		String str = "NULL";
		switch(status) {
		case OperationCode.QUERY:
			str = "QUERY";
			break;
		case OperationCode.ADD:
			str = "ADD";
			break;
		case OperationCode.REMOVE:
			str = "REMOVE";
			break;
		case OperationCode.UPDATE:
			str = "UPDATE";
			break;
		case OperationCode.DISCONNECT:
			str = "DISCONNECT";
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
			
			int status = -999;
			ArrayList meanings = new ArrayList();
			
			while (status!=OperationCode.DISCONNECT) {

			// receive the request
			String receive = in.readUTF();
			
			JSONObject receiveJSON = parseRequest(receive);
			int operation = Integer.parseInt(receiveJSON.get("operation").toString());
			String word = (String) receiveJSON.get("word");
			meanings = (ArrayList) receiveJSON.get("meanings");
			// print the log
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
			String logStr = "Message from the client " + clientSocket.getInetAddress().getHostName() + 
					":\nRequests to " + status2str(operation) + " " + word;
			dictServer.printLog(logStr);
			
			status = ExceptionCode.FAIL;	
			switch(operation) {
			case OperationCode.DISCONNECT:
				// say goodbye
				break;
			case OperationCode.QUERY:
				if (dict.isWordExist(word)) {
					meanings = dict.query(word);
					status = ExceptionCode.SUCCESS;
				}
				else {
					status = ExceptionCode.UNSEEN;
				}
				// send ACK
				out.writeUTF(createReply(status, meanings).toString());
				out.flush();
				dictServer.printLog("Sent Reply!");
				break;
			case OperationCode.ADD:
				if(!dict.isWordExist(word)) {
					status = dict.add(word, meanings);
				}else {
					status = ExceptionCode.EXIST;
				}
				// send ACK
				out.writeUTF(createReply(status, null).toString());
				out.flush();
				dictServer.printLog("Sent Reply!");
				break;
			case OperationCode.REMOVE:
				if(dict.isWordExist(word)) {
					status = dict.remove(word);
				}else {
					status = ExceptionCode.UNSEEN;
				}
				// send ACK
				out.writeUTF(createReply(status, null).toString());
				out.flush();
				dictServer.printLog("Sent Reply!");
				break;
			case OperationCode.UPDATE:
				if (dict.isWordExist(word)) {
					status = dict.update(word, meanings);
				}else {
					status = ExceptionCode.UNSEEN;
				}
				// send ACK
				out.writeUTF(createReply(status, meanings).toString());
				out.flush();
				dictServer.printLog("Sent Reply!");
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
