package server;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import server.Dictionary;
import server.ServerGUI;
import status.OperationCode;
import server.HundleRequest;
import javax.net.ServerSocketFactory;
public class DictServer {
	private int port = 4444;
	private Dictionary dict;
	private ServerSocket server;
	private ServerGUI ui;
	private int ClientCount = 0;
	public DictServer(String port, String dictPath){
		this.port = Integer.parseInt(port);
		this.dict = new Dictionary(dictPath);
		this.server = null;
		this.ui = null;
	}
	public void printLog(String str) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		String date_info = "["+df.format(new Date()) +"]: ";
		System.out.println("\n"+date_info+str);
		ui.getLogTextArea().append("\n"+date_info+str);
	}
	public void run() {
		try {
			this.server = new ServerSocket(port);
			this.ui = new ServerGUI(InetAddress.getLocalHost().getHostAddress(), this.port);
			ui.setVisible(true);
			while (true) {
				System.out.println("Server listening for a connection");
				Socket clientSocket = this.server.accept();
				ClientCount++;
				String connect_msg = "Connecting with a client: "+ clientSocket.getInetAddress().getHostName() + " on port" + clientSocket.getLocalPort();
				printLog(connect_msg+"\n The current number of client is " + String.valueOf(ClientCount));
				HundleRequest solveThread = new HundleRequest(this, clientSocket, dict);
				solveThread.start();
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		try {
			if (Integer.parseInt(args[0]) <= 1024 || Integer.parseInt(args[0]) >= 49151) {
				System.out.println("Invalid Port Number: Port number should be between 1024 and 49151!");
				System.exit(-1);
			}
			DictServer dictServer = new DictServer(args[0], args[1]);
			dictServer.run();
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Lack of Parameters:\nPlease run like \"java - jar DictServer.jar <port> <dictionary-file>\"!");
		} catch (NumberFormatException e) {
			System.out.println("Invalid Port Number: Port number should be between 1024 and 49151!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
