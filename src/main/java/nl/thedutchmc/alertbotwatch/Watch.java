package nl.thedutchmc.alertbotwatch;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Watch extends JavaPlugin {

	private TcpServer tcpServer;
	private static Watch INSTANCE;
	
	@Override
	public void onEnable() {
		INSTANCE = this;
		
		ConfigurationHandler configHandler = new ConfigurationHandler(this);
		configHandler.loadConfig();
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				tcpServer = new TcpServer(ConfigurationHandler.port);
				tcpServer.run();
			}
		}.runTaskAsynchronously(this);
		
		Watch.logInfo("AlertBot-Watch Started.");
	}
	
	@Override
	public void onDisable() {
		Watch.logInfo("Attempting to stop TCPServer...");
		tcpServer.stopSocketServer();		
	}
	
	public static void logInfo(String log) {
		INSTANCE.getLogger().info(log);
	}
	
	public static void logWarn(String log) {
		INSTANCE.getLogger().warning(log);
	}
	
	public static String getStackTrace(Throwable t) {
		StringWriter sw = new StringWriter();
		t.printStackTrace(new PrintWriter(sw));
		return sw.toString();
	}
}
