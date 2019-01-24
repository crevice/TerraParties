package com.gmail.xtendspb.plugins.parties;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.xtendspb.plugins.parties.listeners.PartiesCommandListener;
import com.gmail.xtendspb.plugins.parties.listeners.PartiesListener;

public class Parties extends JavaPlugin{
	private static PartiesCommandListener pCommands;
	private static PartiesListener pListener;
	public static PartiesCommands cmd;
	public static PartiesManager pm;
	public static Plugin plugin;
	public static Logger log;
	
	public void onEnable(){
		plugin = this;
		log = Logger.getLogger("Minecraft");
		pm = new PartiesManager(plugin);
		cmd = new PartiesCommands();
		pCommands	= new PartiesCommandListener();
		pListener	= new PartiesListener();
		getServer().getPluginManager().registerEvents(pListener, this);
        getCommand("party").setExecutor(pCommands);
        getCommand("pc").setExecutor(pCommands);
	}
	
	public void onDisable(){

	}
	
	public static void doLog(String msg){
		log.info("[" + Parties.plugin.getName() + "] " + msg);
	}
	
	public static void cmdOutput(String playerName,String msg){
		Player player = getPlayer(playerName);
		msg = msg.replaceAll("&", "\u00A7");
		player.sendMessage(msg);
	}
	
	public static void sendMessage(Player player, String msg){
		player.sendMessage(msg.replaceAll("&", "\u00A7"));
	}
	
	public static Player getPlayer(String playerName){
		return Bukkit.getPlayerExact(playerName);
	}
	
	public static long getTime(){
		return System.currentTimeMillis() / 1000L;
	}
}
