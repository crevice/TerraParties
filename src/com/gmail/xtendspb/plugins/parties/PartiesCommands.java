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
			return "&c> Вы уже состоите в группе.";
		}
		if(partyName.length() > 8 || partyName.length() < 3){
			return "&c> Название должно содержать от 3 до 8 символов.";
		}
		if(!partyName.matches("^[a-zA-Z0-9]*$")){
			return "&c> Название может содержать только цифры и буквы английского алфавита.";
		}
		if(Parties.pm.getParty(partyName).isValid()){
			return "&c> Группа с таким названием уже существует.";
		}
		Parties.pm.addParty(partyName, playerName);
		Parties.doLog(playerName + " created party " + partyName);
		return "&a> Вы создали группу: " + partyName + "\n&e> Неактивные группы будут удалены через 14 дней.";
	}
	
	public String leaveParty(String playerName){
		Party playerParty = Parties.pm.getPlayerParty(playerName);
		if(!playerParty.isValid()){
			return "&c> Вы не состоите в группе.";
		}
		if(playerParty.getPartyOwner().equalsIgnoreCase(playerName)){
			if(playerParty.getPlayersNum() > 1){
				return "&c> Лидер не может покинуть группу, пока в ней есть игроки.";
			}else{
				Parties.pm.removeParty(playerParty.getPartyName());
				return "&e> Пустая группа " + playerParty.getPartyName() +" была расформирована.";
			}
		}
		playerParty.sendMessageToAll("&e> *"+ playerName +" покидает группу.");
		Parties.doLog(playerName + " leaved party " + playerParty.getPartyName());
		playerParty.delPlayer(playerName);
		return "&a> Вы покинули группу: " + playerParty.getPartyName();
	}
	
	public String joinParty(String playerName, String partyName, String partyPass){
		if(Parties.pm.getPlayerParty(playerName).isValid()){
			return "&c> Вы уже состоите в группе.";
		}
		if(partyName.length() <= 0){
			return "&c> Вы не указали название группы.";
		}
		Party joinParty = Parties.pm.getParty(partyName);
		if(!joinParty.isValid()){
			return "&c> Указанной группы не существует.";
		}
		if(partyPass.length() > 0){
			if(partyPass.equals(joinParty.getPartyPassword())){
				joinParty.sendMessageToAll("&e> *"+ playerName +" присоединяется к группе по паролю.");
				joinParty.addPlayer(playerName);
				Parties.doLog(playerName + " joined party " + partyName + " by password.");
				return "&a> Вы присоединяетесь к группе: " + partyName;
			}else{
				return "&c> Пароль указан неверно, либо не задан.";
			}
		}
		if(joinParty.isClosed()){
			return "&c> Это закрытая группа. Вам нужно приглашение.";
		}
		joinParty.sendMessageToAll("&e> *"+ playerName +" присоединяется к группе.");
		joinParty.addPlayer(playerName);
		Parties.doLog(playerName + " joined party " + partyName);
		return "&a> Вы присоединяетесь к группе: " + partyName;
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
			return "&a> Игроки в группе " + playerParty.getPartyName() + ": &f"+list;
		}else{
			String list = "";
			for(Party p : Parties.pm.getParties()){
				if(p.isClosed()){
					list +="&c"+p.getPartyName()+", ";
				}else{
					list +="&a"+p.getPartyName()+"&f, ";
				}
			}
			return "&a> Группы: &f" + list;
		}
	}
	
	/*
	 * PARTY INVITES
	 */
	public String inviteParty(String playerName, String targetName){
		Party playerParty = Parties.pm.getPlayerParty(playerName);
		if(!playerParty.isValid()){
			return "&c> Вы не состоите в группе.";
		}
		if(playerName.equalsIgnoreCase(targetName)){
			return "&c> Вы не можете пригласить в группу самого себя.";
		}
		if(!playerParty.isInvite() && !playerName.equalsIgnoreCase(playerParty.getPartyOwner())){
			return "&c> В данной группе приглашать может только владелец.";
		}
		Player target = Bukkit.getPlayerExact(targetName);
		if(target == null){
			return "&c> Указанный игрок не в игре.";
		}
		Party targetParty = Parties.pm.getPlayerParty(targetName);
		if(targetParty.isValid()){
			return "&c> Указанный игрок уже состоит в группе.";
		}
		if(Parties.pm.isInvited(targetName)){
			return "&c> Указанный игрок уже приглашён в группу.";
		}
		playerParty.sendMessageToAll("&e> *"+ playerName +" пригласил " + targetName + " в группу.");
		Parties.pm.addInvite(targetName, playerName, playerParty.getPartyName());
		Parties.sendMessage(target, 
						"&a> Приглашение в группу от " +playerName + "\n" +
						"&e> Используйте &a/party accept&e, чтобы принять \n" + 
						"&e> или &c/party decline&e чтобы отклонить его."
						);
		return "&a> Приглашение игроку " + targetName + " отправлено.";
	}
	
	public String acceptParty(String playerName){
		if(Parties.pm.getPlayerParty(playerName).isValid()){
			return "&c> Вы уже состоите в группе.";
		}
		if(!Parties.pm.isInvited(playerName)){
			return "&c> Вы не были приглашены в группу.";
		}
		Party p = Parties.pm.getParty(Parties.pm.getInvite(playerName).getPartyName());
		Parties.pm.removeInvite(playerName);
		p.addPlayer(playerName);
		p.sendMessageToAll("&e> *"+ playerName +" принял приглашение в группу.");
		return "&a> Вы приняли приглашение в группу " + p.getPartyName() + ".";
	}
	
	public String declineParty(String playerName){
		if(!Parties.pm.isInvited(playerName)){
			return "&c> Вы не были приглашены в группу.";
		}
		Parties.pm.removeInvite(playerName);		
		return "&c> Вы отказались от приглашения в группу.";
	}
	
	/*
	 * LEADER-ONLY COMMANDS
	 */
	public String disbandParty(String playerName){
		Party playerParty = Parties.pm.getPlayerParty(playerName);
		if(!playerParty.isValid()){
			return "&c> Вы не состоите в группе.";
		}
		if(!playerParty.getPartyOwner().equalsIgnoreCase(playerName)){
			return "&c> Вы не лидер группы.";
		}
		playerParty.sendMessageToAll("&c> *"+ playerName +" расформировал группу.");
		Parties.pm.removeParty(playerParty.getPartyName()); 
		Parties.doLog(playerName + " disbanded party " + playerParty.getPartyName());
		return "&a> Группа расформирована.";
	}
	
	public String kickPlayerFromParty(String playerName, String target){
		Party playerParty = Parties.pm.getPlayerParty(playerName);
		if(!playerParty.isValid()){
			return "&c> Вы не состоите в группе.";
		}
		if(!playerParty.getPartyOwner().equalsIgnoreCase(playerName)){
			return "&c> Вы не лидер группы.";
		}
		if(playerName.equalsIgnoreCase(target)){
			return "&c> Вы не можете исключить самого себя.";
		}
		if(!playerParty.isPlayerInParty(target)){
			return "&c> Указанный игрок находится не в вашей группе.";
		}
		playerParty.sendMessageToAll("&c> *" + target + " был исключен из группы.");
		playerParty.delPlayer(target);
		return "&a> Вы исключили " + target + " из группы.";
	}
	
	public String setPassword(String playerName, String password){
		Party playerParty = Parties.pm.getPlayerParty(playerName);
		if(!playerParty.isValid()){
			return "&c> Вы не состоите в группе.";
		}
		if(!playerParty.getPartyOwner().equalsIgnoreCase(playerName)){
			return "&c> Вы не лидер группы.";
		}
		if(password.length() > 16 && password.length() < 4){
			return "&c> Пароль должен содержать от 4 до 16 символов.";
		}
		playerParty.setPartyPassword(password);
		if(password.length() == 0){
			return "&a> Пароль для входа в группу удалён.";
		}else{
			return "&a> Пароль для входа в группу установлен.";
		}
	}
	
	public String setInvite(String playerName){
		Party playerParty = Parties.pm.getPlayerParty(playerName);
		if(!playerParty.isValid()){
			return "&c> Вы не состоите в группе.";
		}
		if(!playerParty.getPartyOwner().equalsIgnoreCase(playerName)){
			return "&c> Вы не лидер группы.";
		}
		if(playerParty.isInvite()){
			playerParty.setInvite(false);
			return "&e> Приглашения в группу: &cотключены";
		}else{
			playerParty.setInvite(true);
			return "&e> Приглашения в группу: &aвключены";
		}
	}
	
	public String setTopic(String playerName, String[] arr){
		Party p = Parties.pm.getPlayerParty(playerName);
		if(!p.isValid()){
			return "&c> Вы не состоите в группе.";
		}
		if(!p.getPartyOwner().equalsIgnoreCase(playerName)){
			return "&c> Вы не лидер группы.";
		}
		String motd = "";
		for(int i = 1; i < arr.length; i++){
			motd+=arr[i]+" ";
		}
		
		if(motd.length() >= 99){
			return "&c> Длина сообщения превышает 99 символов.";
		}
		p.setPartyTopic(motd);
		return "&a> Сообщение группы изменено.";
	}
	
	public String setType(String playerName){
		Party p = Parties.pm.getPlayerParty(playerName);
		if(!p.isValid()){
			return "&c> Вы не состоите в группе.";
		}
		if(!p.getPartyOwner().equalsIgnoreCase(playerName)){
			return "&c> Вы не лидер группы.";
		}
		if(p.isClosed()){
			p.setClosed(false);
			return "&e> Тип группы изменен на: &aоткрытая";
		}else{
			p.setClosed(true);
			return "&e> Тип группы изменен на: &cзакрытая";
		}
	}
	
	public String setPvp(String playerName){
		Party p = Parties.pm.getPlayerParty(playerName);
		if(!p.isValid()){
			return "&c> Вы не состоите в группе.";
		}
		if(!p.getPartyOwner().equalsIgnoreCase(playerName)){
			return "&c> Вы не лидер группы.";
		}
		if(p.isPvp()){
			p.setPvp(false);
			return "&e> Режим PVP в группе: &cотключен";
		}else{
			p.setPvp(true);
			return "&e> Режим PVP в группе: &aвключен";
		}
	}
	
	public String setLeader(String playerName, String targetName){
		Party p = Parties.pm.getPlayerParty(playerName);
		if(!p.isValid()){
			return "&c> Вы не состоите в группе.";
		}
		if(!p.getPartyOwner().equalsIgnoreCase(playerName)){
			return "&c> Вы не лидер группы.";
		}
		Player target = Bukkit.getPlayerExact(targetName);
		if(target == null){
			return "&c> Указанный игрок не в игре.";
		}
		Party targetParty = Parties.pm.getPlayerParty(targetName);
		if(!p.getPartyName().equalsIgnoreCase(targetParty.getPartyName())){
			return "&c> Указанный игрок состоит в другой группе.";
		}
		p.sendMessageToAll("&e> " + playerName + " назначил " + target.getName() + " лидером группы.");
		p.setPartyOwner(targetName);
		
		Parties.doLog(playerName + " changed leader of party " + p.getPartyName() + " to " + targetName);
		return "&a> Лидер группы изменен.";
	}
	//party tp command
	public String teleportRequest(String playerName, String targetName){
		Party playerParty = Parties.pm.getPlayerParty(playerName);
		if(!playerParty.isValid()){
			return "&c> Вы не состоите в группе.";
		}
		if(playerName.equalsIgnoreCase(targetName)){
			return "&c> Вы не можете телепортироваться к себе самому.";
		}
		Player target = Bukkit.getPlayerExact(targetName);
		if(target == null){
			return "&c> Указанный игрок не в игре.";
		}
		Party targetParty = Parties.pm.getPlayerParty(targetName);
		if(!playerParty.getPartyName().equalsIgnoreCase(targetParty.getPartyName())){
			return "&c> Указанный игрок состоит в другой группе.";
		}
		Parties.pm.addTeleport(targetName, playerName, playerParty.getPartyName());
		Parties.sendMessage(target, 
						"&a> Запрос на телепорт от " +playerName + "\n" +
						"&e> Используйте &a/party tpaccept&e, чтобы принять \n" + 
						"&e> или &c/party tpdecline&e чтобы отклонить его."
						);
		return "&a> Запрос на телепорт к игроку " + targetName + " отправлен.";
	}
	
	public String teleportAccept(String playerName){
		PartyTeleport pt = Parties.pm.getTeleport(playerName);
		if(pt == null){
			return "&c> К вам никто не телепортируется.";
		}
		
		if(pt.isTimedOut()){
			Parties.pm.removeTeleport(playerName);
			return "&c> Время ожидания телепортации истекло. Попробуйте ещё раз.";
		}
		
		Player teleFrom = Bukkit.getPlayerExact(pt.getFrom());
		if(teleFrom == null){
			Parties.pm.removeTeleport(playerName);		
			return "&c> Указанный игрок вышел из игры.";
		}
		Party playerParty = Parties.pm.getPlayerParty(playerName);
		Party targetParty = Parties.pm.getPlayerParty(pt.getFrom());
		if(!playerParty.getPartyName().equalsIgnoreCase(targetParty.getPartyName())){
			return "&c> Телепортируемый игрок состоит в другой группе.";
		}
		
		Player teleTarget = Bukkit.getPlayerExact(pt.getTo());
		teleFrom.teleport(teleTarget);
		Parties.pm.removeTeleport(playerName);		
		return "&a> Вы приняли запрос на телепортацию от " + pt.getFrom() + ".";
	}
	
	public String teleportDecline(String playerName){
		if(!Parties.pm.isTeleporting(playerName)){
			return "&c> К вам никто не телепортируется.";
		}
		Parties.pm.removeTeleport(playerName);		
		return "&c> Вы отказались от запроса на телепортацию.";
	}
	
	public String toggleChat(String playerName){
		if(Parties.pm.isPartyChatToggled(playerName)){
			Parties.pm.disablePartyChat(playerName);
			return "&a> Теперь вы общаетесь в общем чате.";
		}
		Parties.pm.enablePartyChat(playerName);
		return "&b> Теперь вы общаетесь в групповом чате.";
	}
	
	public String status(String playerName){
		Party p = Parties.pm.getPlayerParty(playerName);
		if(!p.isValid()){
			return "&c> Вы не состоите в группе.";
		}
		String status = 
				"&a--- &eИнформация о группе &a-------------------------\n"
				+"\n&aНазвание: &f"+ p.getPartyName()
				+"\n&aЛидер: &f"+ p.getPartyOwner()
				+"\n&aСоздана: &f"+ new SimpleDateFormat("dd.MM.yy HH:mm").format(new Date(p.getTimeCreated()*1000))
				+"\n&aИгроков: &f"+p.getPlayersNum()
				+"\n&aЗакрытая: &f"+ (p.isClosed() ? "&aДа" : "&cНет")
				+"\n&aПароль: &f"+ (p.getPartyPassword().equals("") ? "&cНет" : "&aДа")
				+"\n&aПриглашения: &f"+ (p.isInvite() ? "&aДа" : "&cНет")
				+"\n&aPvP разрешен: &f"+ (p.isPvp() ? "&aДа" : "&cНет")
				+"\n&aСообщение группы: &f\n"
				+p.getPartyTopic();
		return status;
	}
	
	public String help(){
		String help = 
				 "&a--- &eОбщие команды группы &a-----------------------\n"
				+"&a/party create &eназвание&f - создать группу.\n"
				+"&a/party join &eназвание пароль&f - присоединится к группе.\n"
				+"&a/party leave&f - покинуть группу.\n"
				+"&a/party list&f - список групп/игроков в группе.\n"
				+"&a/party invite&f - пригласить игрока в группу.\n"
				+"&a/party tp &eник&f - телепортироваться к игроку.\n"
				+"&a/pc&f - групповой чат.\n"
				+"&a--- &eКоманды лидера группы &a----------------------\n"
				+"&a/party disband&f - расформировать группу.\n"
				+"&a/party kick &eник&f - кикнуть игрока из группы.\n"
				+"&a/party status - информация о группе.\n"
				+"&a/party setpassword &eпароль&f - установить пароль.\n"
				+"&a/party setinvite&f - разрешить приглашения.\n"
				+"&a/party setmotd &eтекст&f - установить motd группы.\n"
				+"&a/party settype&f - закрыть/открыть группу.\n"
				+"&a/party setpvp&f - разрешить PvP в группе.\n"
				+"&a/party setleader &eник&f - назначить нового лидера.";
		return help;
	}
}