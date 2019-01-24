package com.gmail.xtendspb.plugins.parties.listeners;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gmail.xtendspb.plugins.parties.Parties;

public class PartiesCommandListener implements CommandExecutor{
	public static String getArg(String[] data, int index){
	    try{
	      data[index].length();
	      return data[index];
	    } catch(ArrayIndexOutOfBoundsException e){
	      return "";
	    }
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		if(!(sender instanceof Player)) return true;
		String playerName = sender.getName();
	    if (cmd.getName().equalsIgnoreCase("party")){
	    	switch(getArg(args,0).toLowerCase()){
	    	//Creation or join commands
	    		case("create"): 	
	    			Parties.cmdOutput(playerName, Parties.cmd.createParty(playerName, getArg(args,1)));
	    		break;
	    		case("join"):
	    			Parties.cmdOutput(playerName, Parties.cmd.joinParty(playerName, getArg(args,1), getArg(args,2)));
	    		break;
	    		case("leave"): 		
	    			Parties.cmdOutput(playerName, Parties.cmd.leaveParty(playerName));
	    		break;
	    		case("list"):
	    			Parties.cmdOutput(playerName, Parties.cmd.listParty(playerName));
	    		break;
	    		case("invite"): 	
	    			Parties.cmdOutput(playerName, Parties.cmd.inviteParty(playerName, getArg(args,1))); 
	    		break;
	    		case("accept"):
	    			Parties.cmdOutput(playerName, Parties.cmd.acceptParty(playerName)); 
	    		break;
	    		case("decline"):
	    			Parties.cmdOutput(playerName, Parties.cmd.declineParty(playerName)); 
	    		break;
	    	//Teleportation commands
	    		case("tp"): Parties.cmdOutput(playerName, Parties.cmd.teleportRequest(playerName, getArg(args,1))); break;
	    		case("tpaccept"): Parties.cmdOutput(playerName, Parties.cmd.teleportAccept(playerName)); break;
	    		case("tpdecline"): Parties.cmdOutput(playerName, Parties.cmd.teleportDecline(playerName)); break;
	    	//Leader Commands
	    		case("disband"): 
	    			Parties.cmdOutput(playerName, Parties.cmd.disbandParty(playerName));
	    		break;
	    		case("kick"):
	    			Parties.cmdOutput(playerName, Parties.cmd.kickPlayerFromParty(playerName, getArg(args,1)));
	    		break;
	    		case("setpassword"):
	    			Parties.cmdOutput(playerName, Parties.cmd.setPassword(playerName, getArg(args,1)));
	    		break;
	    		case("setinvite"):
	    			Parties.cmdOutput(playerName, Parties.cmd.setInvite(playerName));
	    		break;
	    		case("setmotd"):
	    			Parties.cmdOutput(playerName, Parties.cmd.setTopic(playerName, args));
	    		break;
	    		case("settype"): 
	    			Parties.cmdOutput(playerName, Parties.cmd.setType(playerName));
	    		break;
	    		case("setpvp"): 
	    			Parties.cmdOutput(playerName, Parties.cmd.setPvp(playerName));
	    		break;
	    		case("setleader"): 
	    			Parties.cmdOutput(playerName, Parties.cmd.setLeader(playerName, getArg(args,1)));
	    		break;
	    		case("status"): 
	    			Parties.cmdOutput(playerName, Parties.cmd.status(playerName));
	    		break;
	    		default: 
	    			Parties.cmdOutput(playerName, Parties.cmd.help());
	    		break;
	    	}
	    }else if(cmd.getName().equalsIgnoreCase("pc")){
	    	Parties.cmdOutput(playerName, Parties.cmd.toggleChat(playerName));
	    }
		return true;
	}
}
