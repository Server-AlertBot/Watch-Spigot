package nl.thedutchmc.alertbotwatch;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigurationHandler {

	public static String name;
	public static int port;
	
	private Watch plugin;
	
	public ConfigurationHandler(Watch plugin) {
		this.plugin = plugin;
	}
	
	private File file;
	private FileConfiguration config;
	
	public void loadConfig() {
		file = new File(plugin.getDataFolder(), "config.yml");
		
		if(!file.exists()) {
			file.getParentFile().mkdirs();
			plugin.saveResource("config.yml", false);
		}
		
		config = new YamlConfiguration();
		
		try {
			config.load(file);
			readConfig();
		} catch (InvalidConfigurationException e) {
			Watch.logWarn("Invalid config.yml!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void readConfig() {
		name = config.getString("name");
		port = config.getInt("port");
	}
}
