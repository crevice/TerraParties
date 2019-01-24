package com.gmail.xtendspb.plugins.parties;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class PartiesConfig {
    private FileConfiguration config;
    private File configFile = null;
    
    public PartiesConfig(Plugin plugin){
    	initConfig(plugin);
    }
    
    public void initConfig(Plugin plugin){
    	File dataFolder = plugin.getDataFolder();
        if(!dataFolder.isDirectory()){
        	dataFolder.mkdirs();
        }
        configFile = new File(dataFolder, "config.yml");
        if(!configFile.exists()){
        	plugin.saveResource("config.yml", false);
        }
        config=YamlConfiguration.loadConfiguration(configFile);  
    }
    
    public FileConfiguration getConfig(){
    	return config;
    }
    
	public void saveConfig(){
	    try {
	        config.save(configFile);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
}
