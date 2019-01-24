package com.gmail.xtendspb.plugins.parties;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.gmail.xtendspb.plugins.parties.utils.Party;
import com.gmail.xtendspb.plugins.parties.utils.PartyTeleport;

public class PartiesCommands {
	
	/*
	 * PARTY CREATION
	 */
	public String createParty(String playerName, String partyName){
		if(Parties.pm.getPlayerParty(playerName).isValid()){
			return "&c> �� ��� �������� � ������.";
		}
		if(partyName.length() > 8 || partyName.length() < 3){
			return "&c> �������� ������ ��������� �� 3 �� 8 ��������.";
		}
		if(!partyName.matches("^[a-zA-Z0-9]*$")){
			return "&c> �������� ����� ��������� ������ ����� � ����� ����������� ��������.";
		}
		if(Parties.pm.getParty(partyName).isValid()){
			return "&c> ������ � ����� ��������� ��� ����������.";
		}
		Parties.pm.addParty(partyName, playerName);
		Parties.doLog(playerName + " created party " + partyName);
		return "&a> �� ������� ������: " + partyName + "\n&e> ���������� ������ ����� ������� ����� 14 ����.";
	}
	
	public String leaveParty(String playerName){
		Party playerParty = Parties.pm.getPlayerParty(playerName);
		if(!playerParty.isValid()){
			return "&c> �� �� �������� � ������.";
		}
		if(playerParty.getPartyOwner().equalsIgnoreCase(playerName)){
			if(playerParty.getPlayersNum() > 1){
				return "&c> ����� �� ����� �������� ������, ���� � ��� ���� ������.";
			}else{
				Parties.pm.removeParty(playerParty.getPartyName());
				return "&e> ������ ������ " + playerParty.getPartyName() +" ���� ��������������.";
			}
		}
		playerParty.sendMessageToAll("&e> *"+ playerName +" �������� ������.");
		Parties.doLog(playerName + " leaved party " + playerParty.getPartyName());
		playerParty.delPlayer(playerName);
		return "&a> �� �������� ������: " + playerParty.getPartyName();
	}
	
	public String joinParty(String playerName, String partyName, String partyPass){
		if(Parties.pm.getPlayerParty(playerName).isValid()){
			return "&c> �� ��� �������� � ������.";
		}
		if(partyName.length() <= 0){
			return "&c> �� �� ������� �������� ������.";
		}
		Party joinParty = Parties.pm.getParty(partyName);
		if(!joinParty.isValid()){
			return "&c> ��������� ������ �� ����������.";
		}
		if(partyPass.length() > 0){
			if(partyPass.equals(joinParty.getPartyPassword())){
				joinParty.sendMessageToAll("&e> *"+ playerName +" �������������� � ������ �� ������.");
				joinParty.addPlayer(playerName);
				Parties.doLog(playerName + " joined party " + partyName + " by password.");
				return "&a> �� ��������������� � ������: " + partyName;
			}else{
				return "&c> ������ ������ �������, ���� �� �����.";
			}
		}
		if(joinParty.isClosed()){
			return "&c> ��� �������� ������. ��� ����� �����������.";
		}
		joinParty.sendMessageToAll("&e> *"+ playerName +" �������������� � ������.");
		joinParty.addPlayer(playerName);
		Parties.doLog(playerName + " joined party " + partyName);
		return "&a> �� ��������������� � ������: " + partyName;
	}
	
	public String listParty(String playerName){
		Party playerParty = Parties.pm.getPlayerParty(playerName);
		if(Parties.pm.getPlayerParty(playerName).isValid()){
			String list = "";
			for(String v : playerParty.getPlayers()){
				if(Bukkit.getPlayerExact(v) == null){
					list +="&f"+v+", ";
				}else{
					list +="&a"+v+"&f, ";
				}
			}
			return "&a> ������ � ������ " + playerParty.getPartyName() + ": &f"+list;
		}else{
			String list = "";
			for(Party p : Parties.pm.getParties()){
				if(p.isClosed()){
					list +="&c"+p.getPartyName()+", ";
				}else{
					list +="&a"+p.getPartyName()+"&f, ";
				}
			}
			return "&a> ������: &f" + list;
		}
	}
	
	/*
	 * PARTY INVITES
	 */
	public String inviteParty(String playerName, String targetName){
		Party playerParty = Parties.pm.getPlayerParty(playerName);
		if(!playerParty.isValid()){
			return "&c> �� �� �������� � ������.";
		}
		if(playerName.equalsIgnoreCase(targetName)){
			return "&c> �� �� ������ ���������� � ������ ������ ����.";
		}
		if(!playerParty.isInvite() && !playerName.equalsIgnoreCase(playerParty.getPartyOwner())){
			return "&c> � ������ ������ ���������� ����� ������ ��������.";
		}
		Player target = Bukkit.getPlayerExact(targetName);
		if(target == null){
			return "&c> ��������� ����� �� � ����.";
		}
		Party targetParty = Parties.pm.getPlayerParty(targetName);
		if(targetParty.isValid()){
			return "&c> ��������� ����� ��� ������� � ������.";
		}
		if(Parties.pm.isInvited(targetName)){
			return "&c> ��������� ����� ��� ��������� � ������.";
		}
		playerParty.sendMessageToAll("&e> *"+ playerName +" ��������� " + targetName + " � ������.");
		Parties.pm.addInvite(targetName, playerName, playerParty.getPartyName());
		Parties.sendMessage(target, 
						"&a> ����������� � ������ �� " +playerName + "\n" +
						"&e> ����������� &a/party accept&e, ����� ������� \n" + 
						"&e> ��� &c/party decline&e ����� ��������� ���."
						);
		return "&a> ����������� ������ " + targetName + " ����������.";
	}
	
	public String acceptParty(String playerName){
		if(Parties.pm.getPlayerParty(playerName).isValid()){
			return "&c> �� ��� �������� � ������.";
		}
		if(!Parties.pm.isInvited(playerName)){
			return "&c> �� �� ���� ���������� � ������.";
		}
		Party p = Parties.pm.getParty(Parties.pm.getInvite(playerName).getPartyName());
		Parties.pm.removeInvite(playerName);
		p.addPlayer(playerName);
		p.sendMessageToAll("&e> *"+ playerName +" ������ ����������� � ������.");
		return "&a> �� ������� ����������� � ������ " + p.getPartyName() + ".";
	}
	
	public String declineParty(String playerName){
		if(!Parties.pm.isInvited(playerName)){
			return "&c> �� �� ���� ���������� � ������.";
		}
		Parties.pm.removeInvite(playerName);		
		return "&c> �� ���������� �� ����������� � ������.";
	}
	
	/*
	 * LEADER-ONLY COMMANDS
	 */
	public String disbandParty(String playerName){
		Party playerParty = Parties.pm.getPlayerParty(playerName);
		if(!playerParty.isValid()){
			return "&c> �� �� �������� � ������.";
		}
		if(!playerParty.getPartyOwner().equalsIgnoreCase(playerName)){
			return "&c> �� �� ����� ������.";
		}
		playerParty.sendMessageToAll("&c> *"+ playerName +" ������������� ������.");
		Parties.pm.removeParty(playerParty.getPartyName()); 
		Parties.doLog(playerName + " disbanded party " + playerParty.getPartyName());
		return "&a> ������ ��������������.";
	}
	
	public String kickPlayerFromParty(String playerName, String target){
		Party playerParty = Parties.pm.getPlayerParty(playerName);
		if(!playerParty.isValid()){
			return "&c> �� �� �������� � ������.";
		}
		if(!playerParty.getPartyOwner().equalsIgnoreCase(playerName)){
			return "&c> �� �� ����� ������.";
		}
		if(playerName.equalsIgnoreCase(target)){
			return "&c> �� �� ������ ��������� ������ ����.";
		}
		if(!playerParty.isPlayerInParty(target)){
			return "&c> ��������� ����� ��������� �� � ����� ������.";
		}
		playerParty.sendMessageToAll("&c> *" + target + " ��� �������� �� ������.");
		playerParty.delPlayer(target);
		return "&a> �� ��������� " + target + " �� ������.";
	}
	
	public String setPassword(String playerName, String password){
		Party playerParty = Parties.pm.getPlayerParty(playerName);
		if(!playerParty.isValid()){
			return "&c> �� �� �������� � ������.";
		}
		if(!playerParty.getPartyOwner().equalsIgnoreCase(playerName)){
			return "&c> �� �� ����� ������.";
		}
		if(password.length() > 16 && password.length() < 4){
			return "&c> ������ ������ ��������� �� 4 �� 16 ��������.";
		}
		playerParty.setPartyPassword(password);
		if(password.length() == 0){
			return "&a> ������ ��� ����� � ������ �����.";
		}else{
			return "&a> ������ ��� ����� � ������ ����������.";
		}
	}
	
	public String setInvite(String playerName){
		Party playerParty = Parties.pm.getPlayerParty(playerName);
		if(!playerParty.isValid()){
			return "&c> �� �� �������� � ������.";
		}
		if(!playerParty.getPartyOwner().equalsIgnoreCase(playerName)){
			return "&c> �� �� ����� ������.";
		}
		if(playerParty.isInvite()){
			playerParty.setInvite(false);
			return "&e> ����������� � ������: &c���������";
		}else{
			playerParty.setInvite(true);
			return "&e> ����������� � ������: &a��������";
		}
	}
	
	public String setTopic(String playerName, String[] arr){
		Party p = Parties.pm.getPlayerParty(playerName);
		if(!p.isValid()){
			return "&c> �� �� �������� � ������.";
		}
		if(!p.getPartyOwner().equalsIgnoreCase(playerName)){
			return "&c> �� �� ����� ������.";
		}
		String motd = "";
		for(int i = 1; i < arr.length; i++){
			motd+=arr[i]+" ";
		}
		
		if(motd.length() >= 99){
			return "&c> ����� ��������� ��������� 99 ��������.";
		}
		p.setPartyTopic(motd);
		return "&a> ��������� ������ ��������.";
	}
	
	public String setType(String playerName){
		Party p = Parties.pm.getPlayerParty(playerName);
		if(!p.isValid()){
			return "&c> �� �� �������� � ������.";
		}
		if(!p.getPartyOwner().equalsIgnoreCase(playerName)){
			return "&c> �� �� ����� ������.";
		}
		if(p.isClosed()){
			p.setClosed(false);
			return "&e> ��� ������ ������� ��: &a��������";
		}else{
			p.setClosed(true);
			return "&e> ��� ������ ������� ��: &c��������";
		}
	}
	
	public String setPvp(String playerName){
		Party p = Parties.pm.getPlayerParty(playerName);
		if(!p.isValid()){
			return "&c> �� �� �������� � ������.";
		}
		if(!p.getPartyOwner().equalsIgnoreCase(playerName)){
			return "&c> �� �� ����� ������.";
		}
		if(p.isPvp()){
			p.setPvp(false);
			return "&e> ����� PVP � ������: &c��������";
		}else{
			p.setPvp(true);
			return "&e> ����� PVP � ������: &a�������";
		}
	}
	
	public String setLeader(String playerName, String targetName){
		Party p = Parties.pm.getPlayerParty(playerName);
		if(!p.isValid()){
			return "&c> �� �� �������� � ������.";
		}
		if(!p.getPartyOwner().equalsIgnoreCase(playerName)){
			return "&c> �� �� ����� ������.";
		}
		Player target = Bukkit.getPlayerExact(targetName);
		if(target == null){
			return "&c> ��������� ����� �� � ����.";
		}
		Party targetParty = Parties.pm.getPlayerParty(targetName);
		if(!p.getPartyName().equalsIgnoreCase(targetParty.getPartyName())){
			return "&c> ��������� ����� ������� � ������ ������.";
		}
		p.sendMessageToAll("&e> " + playerName + " �������� " + target.getName() + " ������� ������.");
		p.setPartyOwner(targetName);
		
		Parties.doLog(playerName + " changed leader of party " + p.getPartyName() + " to " + targetName);
		return "&a> ����� ������ �������.";
	}
	//party tp command
	public String teleportRequest(String playerName, String targetName){
		Party playerParty = Parties.pm.getPlayerParty(playerName);
		if(!playerParty.isValid()){
			return "&c> �� �� �������� � ������.";
		}
		if(playerName.equalsIgnoreCase(targetName)){
			return "&c> �� �� ������ ����������������� � ���� ������.";
		}
		Player target = Bukkit.getPlayerExact(targetName);
		if(target == null){
			return "&c> ��������� ����� �� � ����.";
		}
		Party targetParty = Parties.pm.getPlayerParty(targetName);
		if(!playerParty.getPartyName().equalsIgnoreCase(targetParty.getPartyName())){
			return "&c> ��������� ����� ������� � ������ ������.";
		}
		Parties.pm.addTeleport(targetName, playerName, playerParty.getPartyName());
		Parties.sendMessage(target, 
						"&a> ������ �� �������� �� " +playerName + "\n" +
						"&e> ����������� &a/party tpaccept&e, ����� ������� \n" + 
						"&e> ��� &c/party tpdecline&e ����� ��������� ���."
						);
		return "&a> ������ �� �������� � ������ " + targetName + " ���������.";
	}
	
	public String teleportAccept(String playerName){
		PartyTeleport pt = Parties.pm.getTeleport(playerName);
		if(pt == null){
			return "&c> � ��� ����� �� ���������������.";
		}
		
		if(pt.isTimedOut()){
			Parties.pm.removeTeleport(playerName);
			return "&c> ����� �������� ������������ �������. ���������� ��� ���.";
		}
		
		Player teleFrom = Bukkit.getPlayerExact(pt.getFrom());
		if(teleFrom == null){
			Parties.pm.removeTeleport(playerName);		
			return "&c> ��������� ����� ����� �� ����.";
		}
		Party playerParty = Parties.pm.getPlayerParty(playerName);
		Party targetParty = Parties.pm.getPlayerParty(pt.getFrom());
		if(!playerParty.getPartyName().equalsIgnoreCase(targetParty.getPartyName())){
			return "&c> ��������������� ����� ������� � ������ ������.";
		}
		
		Player teleTarget = Bukkit.getPlayerExact(pt.getTo());
		teleFrom.teleport(teleTarget);
		Parties.pm.removeTeleport(playerName);		
		return "&a> �� ������� ������ �� ������������ �� " + pt.getFrom() + ".";
	}
	
	public String teleportDecline(String playerName){
		if(!Parties.pm.isTeleporting(playerName)){
			return "&c> � ��� ����� �� ���������������.";
		}
		Parties.pm.removeTeleport(playerName);		
		return "&c> �� ���������� �� ������� �� ������������.";
	}
	
	public String toggleChat(String playerName){
		if(Parties.pm.isPartyChatToggled(playerName)){
			Parties.pm.disablePartyChat(playerName);
			return "&a> ������ �� ��������� � ����� ����.";
		}
		Parties.pm.enablePartyChat(playerName);
		return "&b> ������ �� ��������� � ��������� ����.";
	}
	
	public String status(String playerName){
		Party p = Parties.pm.getPlayerParty(playerName);
		if(!p.isValid()){
			return "&c> �� �� �������� � ������.";
		}
		String status = 
				"&a--- &e���������� � ������ &a-------------------------\n"
				+"\n&a��������: &f"+ p.getPartyName()
				+"\n&a�����: &f"+ p.getPartyOwner()
				+"\n&a�������: &f"+ new SimpleDateFormat("dd.MM.yy HH:mm").format(new Date(p.getTimeCreated()*1000))
				+"\n&a�������: &f"+p.getPlayersNum()
				+"\n&a��������: &f"+ (p.isClosed() ? "&a��" : "&c���")
				+"\n&a������: &f"+ (p.getPartyPassword().equals("") ? "&c���" : "&a��")
				+"\n&a�����������: &f"+ (p.isInvite() ? "&a��" : "&c���")
				+"\n&aPvP ��������: &f"+ (p.isPvp() ? "&a��" : "&c���")
				+"\n&a��������� ������: &f\n"
				+p.getPartyTopic();
		return status;
	}
	
	public String help(){
		String help = 
				 "&a--- &e����� ������� ������ &a-----------------------\n"
				+"&a/party create &e��������&f - ������� ������.\n"
				+"&a/party join &e�������� ������&f - ������������� � ������.\n"
				+"&a/party leave&f - �������� ������.\n"
				+"&a/party list&f - ������ �����/������� � ������.\n"
				+"&a/party invite&f - ���������� ������ � ������.\n"
				+"&a/party tp &e���&f - ����������������� � ������.\n"
				+"&a/pc&f - ��������� ���.\n"
				+"&a--- &e������� ������ ������ &a----------------------\n"
				+"&a/party disband&f - �������������� ������.\n"
				+"&a/party kick &e���&f - ������� ������ �� ������.\n"
				+"&a/party status - ���������� � ������.\n"
				+"&a/party setpassword &e������&f - ���������� ������.\n"
				+"&a/party setinvite&f - ��������� �����������.\n"
				+"&a/party setmotd &e�����&f - ���������� motd ������.\n"
				+"&a/party settype&f - �������/������� ������.\n"
				+"&a/party setpvp&f - ��������� PvP � ������.\n"
				+"&a/party setleader &e���&f - ��������� ������ ������.";
		return help;
	}
}