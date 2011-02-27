package com.landsofnavia.mcandze.naviachat.command;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.landsofnavia.mcandze.naviachat.ChannelManager;
import com.landsofnavia.mcandze.naviachat.MessageHandler;
import com.landsofnavia.mcandze.naviachat.NaviaChat;
import com.landsofnavia.mcandze.naviachat.channel.Channel;
import com.landsofnavia.mcandze.naviachat.plugins.ExtensionManager;
import com.landsofnavia.mcandze.naviachat.plugins.DerpPermissionChecker;

public class CommandHandler {
	public static boolean doCommand(String command, Player player, String[] args){
		Commands c;
		try {
			c = Commands.valueOf(command.toUpperCase());
		} catch (Exception e){
			return false;
		}
		
		switch(c){
		case CH: return doChangeChannel(player, args);
		case LEAVECHANNEL: return doLeaveChannel(player, args);
		case CHANNEL: return doChannelCommand(player, args);
		case T: return doTell(player, args);
		case ME: return sendMe(player, args);
		}
		
		return false;
	}
	
	// CH
	public static boolean isChValid(Player player, String[] args){
		if (!DerpPermissionChecker.playerCanChangeChannel(player)){
			player.sendMessage(ChatColor.RED + "Unknown command.");
			return true;
		}
		if (args.length != 1){
			return false;
		}
		return true;
	}
	
	public static boolean doChangeChannel(Player player, String[] args){
		
		if (!DerpPermissionChecker.playerCanChangeChannel(player)){
			player.sendMessage(ChatColor.RED + "Unknown command.");
			return true;
		}
		
		if (args.length != 1){
			return false;
		}
		
		return ChannelManager.playerChangeChannel(args[0], player);
	}
	// /CH
	
	// LEAVECHANNEL
	public static boolean doLeaveChannel(Player player, String[] args){
		// Start validation
		if (!DerpPermissionChecker.playerCanChangeChannel(player)){
			player.sendMessage(ChatColor.RED + "Unknown command");
			return false;
		}
		
		if (args.length == 0){
			return false;
		}
		// End validation.
		Channel c;
		if ((c=ChannelManager.getChannelWithShortcut(args[0])) == null){
			player.sendMessage(ChatColor.RED + "Channel does not exist.");
			return true;
		}
		
		if (!ChannelManager.playerIsInChannel(player, c)){
			player.sendMessage(ChatColor.RED + "You are not in that channel!");
		}
		
		ChannelManager.playerLeaveChannel(c, player);
		return true;
	}
	// /LEAVECHANNEL
	
	// START CHANNEL
	
	public static boolean doChannelCommand(Player player, String[] args){
		if (args.length < 1){
			return false;
		}
		
		ChannelArgs ca;
		try {
			ca = ChannelArgs.valueOf(args[0].toUpperCase());
		} catch (Exception e){
			return false;
		}
		
		switch (ca){
		case BAN: return banPlayer(player, args);
		case UNBAN: return unbanPlayer(player, args);
		case LIST: return listChannels(player);
		default: return false;
		}
	}
	
	// <CHANNEL BAN>
	public static boolean banPlayer(Player player, String[] args){
		if (!DerpPermissionChecker.playerCanBan(player)){
			player.sendMessage(ChatColor.RED + "Unknown command.");
			return true;
		}
		
		if (args.length == 2){
			Player victim = NaviaChat.server.getPlayer(args[1]);
			Channel c = ChannelManager.getFocusedChannel(player);
//			c.banPlayer(victim);
			if (victim.isOnline()){
				victim.sendMessage(ChatColor.RED + "You have been banned from channel: §" + c.getColor() + c.getName());
			}
			return true;
		} else {
			return false;
		}
	}
	// END <CHANNEL BAN>
	
	// START <CHANNEL UNBAN>
	public static boolean unbanPlayer(Player player, String[] args){
		if(!DerpPermissionChecker.playerCanUnban(player)){
			player.sendMessage(ChatColor.RED + "Unknown command.");
			return true;
		}
		if (args.length == 2){
			Player victim = NaviaChat.server.getPlayer(args[1]);
			Channel c = ChannelManager.getFocusedChannel(player);
//			c.unbanPlayer(victim);
			if (victim.isOnline()){
				victim.sendMessage(ChatColor.GREEN + "You have been unbanned from channel: §" + c.getColor() + c.getName());
			}
			return true;
		} else {
			return false;
		}
	}
	// END <CHANNEL UNBAN>
	
	// START <CHANNEL LIST>
	public static boolean listChannels(Player player){
		
		if (!DerpPermissionChecker.playerCanList(player)){
			player.sendMessage(ChatColor.RED + "Unknown command.");
			return true;
		}
		
		player.sendMessage(ChatColor.GREEN + "Syntax: " + ChatColor.GRAY + "<ChannelName> <Channel Shortcut> |");
		String list = ChatColor.YELLOW + "Channels:";

		for (Channel c: ChannelManager.channels){
			if (!c.isHidden()){
				list += " �" + c.getColor() + c.getName() + " " + c.getShortCut() + " |";
			}
		}
		player.sendMessage(list);
		return true;
	}
	// END <CHANNEL LIST>
	// END <CHANNEL>
	
	// START <T>
	public static boolean doTell(Player player, String[] args){
		if (!DerpPermissionChecker.playerCanTell(player)){
			player.sendMessage(ChatColor.RED + "You can not use that command.");
			return true;
		}
		if (args.length < 1){
			return false;
		}
		
		Player p;
		if ((p = NaviaChat.server.getPlayer(args[0])) != null){
			player.sendMessage(ChatColor.RED + "Player does not exist.");
			return true;
		}
		
		if (!(p = NaviaChat.server.getPlayer(args[0])).isOnline()){
			player.sendMessage(ChatColor.RED + "That player isn't online.");
			return true;
		}
		int len = args.length;
		String message = "";
		for (int i = 1; i < len; i++){
			message = message + " " + args[i];
		}
		p.sendMessage(ChatColor.GREEN + "From " + player.getName() + ": " + ChatColor.WHITE + message);
		player.sendMessage(ChatColor.YELLOW + "To " + p.getName() + ": " + message);
		return true;
	}
	// END <T>
	
	// START <ME>
	public static boolean sendMe(Player player, String[] args){
		if (!DerpPermissionChecker.playerCanMe(player)){
			player.sendMessage(ChatColor.RED + "You can not use that command.");
			return true;
		}
		String action = "";
		for (String s: args){
			action += s + " ";
		}
		if (ChannelManager.playerChannels.get(player).isEmpty()){
			player.sendMessage(ChatColor.RED + "You are not in a channel.");
			return true;
		}
		
//  		if (ChannelManager.getFocusedChannel(player).isLocal()){
//		    	MessageHandler.sendIndependentLocalMessage(
//					player.getLocation(), 
//					MessageHandler.getIcEmote(player, action), 
//					ChannelManager.getFocusedChannel(player).getRange());
//		} else {
//			NaviaChat.server.broadcastMessage(ChatColor.AQUA + "* " + player.getDisplayName() + " " + action);
//		}
		return true;
	}
}
