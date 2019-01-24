package com.gmail.xtendspb.plugins.parties;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.bukkit.plugin.Plugin;

import com.gmail.xtendspb.plugins.parties.utils.Party;
import com.gmail.xtendspb.plugins.parties.utils.PartyInvite;
import com.gmail.xtendspb.plugins.parties.utils.PartyTeleport;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class PartiesManager {
    private File dbFile;
    private String dbFileName;
    private List<Party> parties;
    private List<PartyInvite> invites;
    private List<PartyTeleport> teleports;
    private List<String> chat;
    
    public PartiesManager(Plugin plugin){
    	dbFileName 	= "parties.json";
    	invites = new ArrayList<PartyInvite>();
    	teleports = new ArrayList<PartyTeleport>();
    	chat = new ArrayList<String>();
    	initConfig(plugin);
    	
    }
    
    /*
     * INIT
     */
    public void initConfig(Plugin plugin){
    	File dataFolder = plugin.getDataFolder();
        if(!dataFolder.isDirectory()){
        	dataFolder.mkdirs();
        }
        dbFile = new File(dataFolder, dbFileName);
        if(dbFile.exists()){
        	if(!loadParties()){
            	parties = new ArrayList<Party>();
        		Parties.log.warning("Error loading database");
        	}
        }else{
        	parties = new ArrayList<Party>();
        }

    }
    
    /*
     * Save / Load Parties from JSON
     */
	public boolean saveParties(){
    	Gson gson = new GsonBuilder().setPrettyPrinting().create();
    	String gsonstr = gson.toJson(parties);
		try (FileWriter f = new FileWriter(dbFile)){
			f.write(gsonstr);
			f.flush();
			f.close();
			return true;
		}catch (IOException e) {
			return false;
		}
	}
	
	public boolean loadParties(){
		String json;
		try {
			json = FileUtils.readFileToString(dbFile);
		} catch (IOException e) {
			return false;
		}
		Type listType = new TypeToken<ArrayList<Party>>() {}.getType();
		parties = new Gson().fromJson(json, listType);
		return true;
	}
	
	/*
	 * Party Management
	 */
	public void addParty(String party, String owner){
		Party p = new Party(owner, party);
		p.addPlayer(owner);
		parties.add(p);
	}
	
	public void removeParty(String party){
		parties.remove(getParty(party));
	}
	
	public Party getParty(String name){
		for(Party p : parties){
			if(name.equalsIgnoreCase(p.getPartyName())){
				return p;
			}
		}
		return new Party();
	}
	
	public boolean isPartyExists(String name){
		for(Party p : parties){
			if(name.equalsIgnoreCase(p.getPartyName())){
				return true;
			}
		}
		return false;
	}
	
	public List<Party> getParties(){
		return parties;
	}
	
	/*
	 * Players in Party management
	 */
	public String getPlayerPartyName(String player){
		for(Party p : parties){
			if(p.isPlayerInParty(player)){
				return p.getPartyName();
			}
		}
		return "";
	}
	
	public Party getPlayerParty(String player){
		for(Party p : parties){
			if(p.isPlayerInParty(player)){
				return p;
			}
		}
		return new Party();
	}
	
	/*
	 * Invites Management
	 */
	public void addInvite(String playerName, String whoInvited, String partyName){
		PartyInvite inv = getInvite(playerName);
		if(inv != null){
			removeInvite(playerName);
		}
		invites.add(new PartyInvite(playerName, whoInvited, partyName));
	}
	
	public void removeInvite(String playerName){
		PartyInvite inv = getInvite(playerName);
		invites.remove(inv);
	}
	
	public PartyInvite getInvite(String playerName){
		for(PartyInvite inv : invites){
			if(inv.getPlayerName().equalsIgnoreCase(playerName)){
				return inv;
			}
		}
		return null;
	}
	
	public boolean isInvited(String playerName){
		PartyInvite inv = getInvite(playerName);
		if(inv != null){
			if(!inv.isTimedOut()){
				return true;
			}
		}
		return false;
	}
	/*
	 * Teleports Management
	 */
	public void addTeleport(String nameTo, String nameFrom, String partyName){
		PartyTeleport pt = getTeleport(nameTo);
		if(pt != null){
			removeTeleport(nameTo);
		}
		teleports.add(new PartyTeleport(nameTo, nameFrom, partyName));
	}
	
	public void removeTeleport(String nameTo){
		teleports.remove(getTeleport(nameTo));
	}
	
	public PartyTeleport getTeleport(String nameTo){
		for(PartyTeleport pt : teleports){
			if(pt.getTo().equalsIgnoreCase(nameTo)){
				return pt;
			}
		}
		return null;
	}
	
	public boolean isTeleporting(String nameTo){
		PartyTeleport pt = getTeleport(nameTo);
		if(pt != null){
			if(!pt.isTimedOut()){
				return true;
			}
		}
		return false;
	}
	
	/*
	 * Party Chat 
	 */
	public void enablePartyChat(String player){
		chat.add(player.toLowerCase());
	}
	
	public void disablePartyChat(String player){
		chat.remove(player.toLowerCase());
	}
	
	public boolean isPartyChatToggled(String player){
		if(chat.contains(player.toLowerCase())){
			return true;
		}
		return false;
	}
}
