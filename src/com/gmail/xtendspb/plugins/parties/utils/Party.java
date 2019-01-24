package com.gmail.xtendspb.plugins.parties.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.gmail.xtendspb.plugins.parties.Parties;

public class Party {
	private String partyName;
	private String partyOwner;
	private String partyTopic;
	private String partyPassword;
	
	private boolean closed;
	private boolean pvp;
	private boolean invite;
	private boolean valid;
	
	private long timeCreated;
	private long timeLastPlayed;
	
	private List<String> players;
	
	/*
	 * Init
	 */
	public Party(){
		setValid(false);
	}
	
	public Party(String owner, String name){
		setPartyName(name);
		setPartyOwner(owner);
		setPartyTopic("");
		setPartyPassword("");
		setClosed(true);
		setPvp(false);
		setInvite(false);
		setTimeCreated(Parties.getTime());
		setTimeLastPlayed(Parties.getTime());
		setValid(true);
		players = new ArrayList<String>();
	}
		
	/*
	 * Crew management
	 */
	public void addPlayer(String name){
		players.add(name);
	}
	
	public void delPlayer(String name){		
		for(String s : players){
			if(s.equalsIgnoreCase(name)){
				name = s;
				break;
			}
		}
		players.remove(name);

	}
	
	public void sendMessageToAll(String msg){
		for(String s : players){
			Player p = Bukkit.getPlayerExact(s);
			if(p != null) {
				p.sendMessage(msg.replaceAll("&", "\u00A7"));
			}
		}
	}

	public String getPartyOwner() {
		return partyOwner;
	}

	public void setPartyOwner(String partyOwner) {
		this.partyOwner = partyOwner;
	}
	
	public String getPartyName() {
		return partyName;
	}
	
	public void setPartyName(String partyName) {
		this.partyName = partyName;
	}
	
	public String getPartyTopic() {
		return partyTopic;
	}

	public void setPartyTopic(String partyTopic) {
		this.partyTopic = partyTopic;
	}

	public String getPartyPassword() {
		return partyPassword;
	}

	public void setPartyPassword(String partyPassword) {
		this.partyPassword = partyPassword;
	}

	public boolean isClosed() {
		return closed;
	}

	public void setClosed(boolean closed) {
		this.closed = closed;
	}

	public boolean isPvp() {
		return pvp;
	}

	public void setPvp(boolean pvp) {
		this.pvp = pvp;
	}

	public boolean isInvite() {
		return invite;
	}

	public void setInvite(boolean invite) {
		this.invite = invite;
	}

	public long getTimeCreated() {
		return timeCreated;
	}

	public void setTimeCreated(long timeCreated) {
		this.timeCreated = timeCreated;
	}

	public long getTimeLastPlayed() {
		return timeLastPlayed;
	}

	public void setTimeLastPlayed(long timeLastPlayed) {
		this.timeLastPlayed = timeLastPlayed;
	}
	
	public List<String> getPlayers() {
		return players;
	}
	
	public boolean isPlayerInParty(String p){
		for(String plr : players){
			if(plr.equalsIgnoreCase(p)){
				return true;
			}
		}
		return false;
	}

	public int getPlayersNum(){
		return players.size();
	}
	
	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}
}
