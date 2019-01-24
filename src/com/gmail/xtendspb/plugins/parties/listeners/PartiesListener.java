package com.gmail.xtendspb.plugins.parties.listeners;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.world.WorldSaveEvent;

import ru.tehkode.permissions.bukkit.PermissionsEx;

import com.gmail.xtendspb.plugins.parties.Parties;
import com.gmail.xtendspb.plugins.parties.utils.Party;

public class PartiesListener implements Listener{
	DateFormat dateFormat;

	public PartiesListener(){
		dateFormat = new SimpleDateFormat("HH:mm");
		dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Yakutsk"));
	}
	
	@EventHandler
	public void playerHitPlayerEvent(EntityDamageByEntityEvent e) {
		if(e.isCancelled()) return;
		if(!(e.getEntity() instanceof Player && e.getDamager() instanceof Player)) return;
		String victimParty = Parties.pm.getPlayerPartyName(e.getEntity().getName());
		String damagerParty = Parties.pm.getPlayerPartyName(e.getDamager().getName());
		if(!victimParty.equals("") && !damagerParty.equals("") && victimParty.equalsIgnoreCase(damagerParty)){
			if(!Parties.pm.getPlayerParty(e.getEntity().getName()).isPvp()){
				e.setCancelled(true);
				Parties.sendMessage((Player)e.getDamager(),"&c> PvP в группе отключен.");
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncPlayerChatEvent e) {
		if (e.isCancelled()) return;
		String sender = e.getPlayer().getName();
		Party senderParty = Parties.pm.getPlayerParty(sender);
		if(senderParty.isValid() && Parties.pm.isPartyChatToggled(sender)){
			senderParty.sendMessageToAll("[&7" + dateFormat.format(new Date()) + "&f][&3" + senderParty.getPartyName() + "&f]<" + PermissionsEx.getUser(sender).getPrefix() + sender + PermissionsEx.getUser(sender).getSuffix() + "&f> " +e.getMessage());
			Parties.doLog("["+ senderParty.getPartyName() +"] "+sender+": "+e.getMessage());
			e.setCancelled(true);
		}
    }
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e){
		Party p = Parties.pm.getPlayerParty(e.getPlayer().getName());
		if(p.isValid() && !p.getPartyTopic().equalsIgnoreCase("")){
			Parties.sendMessage(e.getPlayer(), "&e> Сообщение группы: " + p.getPartyTopic());
		}
	}
	
	@EventHandler
	public void onWorldSave(WorldSaveEvent event)
    {
		if(event.getWorld().getEnvironment() == Environment.NORMAL){
			Parties.pm.saveParties();
		}
    }
}
