package com.gmail.xtendspb.plugins.parties.utils;

import com.gmail.xtendspb.plugins.parties.Parties;

public class PartyInvite {
	private String playerName;
	private String whoInvited;
	private String partyName;
	private long time;
	
	public PartyInvite(String playerName, String whoInvited, String partyName){
		this.playerName = playerName;
		this.whoInvited = whoInvited;
		this.partyName = partyName;
		updateTime();
	}
	
	public String getPlayerName(){
		return this.playerName;
	}
	
	public String getWhoInvited(){
		return this.whoInvited;
	}
	
	public String getPartyName(){
		return this.partyName;
	}
	
	public void updateTime(){
		this.time = Parties.getTime();
	}
	
	public boolean isTimedOut(){
		return (Parties.getTime() - this.time > 60);
	}
}
