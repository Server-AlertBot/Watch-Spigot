package nl.thedutchmc.alertbotwatch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

import org.json.JSONObject;

public class NetworkClientThread extends Thread {

protected Socket sock;
	
	public NetworkClientThread(Socket sock) {
		this.sock = sock;
	}
	
	public void run() {
		try (
            PrintWriter out = new PrintWriter(sock.getOutputStream(), true);                   
            BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		) {
        	String inputLine;
            while ((inputLine = in.readLine()) != null) {
                JSONObject result = new JSONObject();
                result.put("name", ConfigurationHandler.name);
            	result.put("request", inputLine);
                
            	out.println(result.toString());
                out.flush();                
            }
		} catch (SocketException e) {
		} catch (IOException e) {
            Watch.logWarn("Exception caught when trying to listen on port " + sock.getPort() + " or listening for a connection");
            Watch.logWarn(Watch.getStackTrace(e));
		} finally {
			try {
				sock.close();
			} catch (IOException e) {
				Watch.logWarn("An issue occured when closing the socket.");
				Watch.logWarn(Watch.getStackTrace(e));
			}
		}
	}
}
