package com.gmail.xtendspb.plugins.parties.utils;

import com.gmail.xtendspb.plugins.parties.Parties;

public class PartyTeleport {
	private String nameFrom;
	private String nameTo;
	private String partyName;
	private long time;
	
	public PartyTeleport(String nameTo, String nameFrom, String partyName){
		this.nameTo = nameTo;
		this.nameFrom = nameFrom;
		this.partyName = partyName;
		updateTime();
	}
	
	public String getPartyName(){
		return this.partyName;
	}
	
	public String getFrom(){
		return this.nameFrom;
	}
	
	public String getTo(){
		return this.nameTo;
	}
	
	public void updateTime(){
		this.time = Parties.getTime();
	}
	
	public boolean isTimedOut(){
		return (Parties.getTime() - this.time > 60);
	}
}
