package nl.thedutchmc.alertbotwatch;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServer extends Thread {

	ServerSocket sSock = null;
	private int port;
	boolean isShutdown = false;
	
	public TcpServer(int port) {
		this.port = port;
	}
	
	public void run() {
		try {
			sSock = new ServerSocket(this.port);
		} catch (IOException e) {
			Watch.logWarn("Unable to open ServerSocket due to an IOException");
			Watch.logWarn(Watch.getStackTrace(e));
		}
	
		while(!isShutdown) {
			try {
				Socket clientSocket = sSock.accept();
				new NetworkClientThread(clientSocket).start();
			} catch (IOException e) {
				
				if(isShutdown) continue;
				Watch.logWarn("Unable to accept socket connection");
				Watch.logWarn(Watch.getStackTrace(e));
			}
		}
	}
	
	public void stopSocketServer() {
		isShutdown = true;
		
		try {
			sSock.close();
			Watch.logInfo("TCPServer stopped.");
		} catch (IOException e) {
			Watch.logWarn("Unable to close the server socket.");
			Watch.logWarn(Watch.getStackTrace(e));
		}
	}
}
