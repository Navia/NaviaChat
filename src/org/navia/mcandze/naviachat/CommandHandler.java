package org.navia.mcandze.naviachat;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class CommandHandler {
	
	private NaviaChat plugin;
	private String permissionsNode = "naviachat.chat.";
	
	public CommandHandler(NaviaChat instance){
		this.plugin = instance;
	}
	
	// CH COMMAND START
	public boolean changeChannel(Player player, String[] args){
		if (!plugin.getPluginCommunicationManager().permissions.has(player, permissionsNode + "ch")){
			player.sendMessage(ChatColor.RED + "You can not use that command.");
			return true;
		}
		if (args.length != 1){
			return false;
		}
		plugin.getChManager().playerChangeChannel(args[0], player);
		return true;
	}
	// CH COMMAND END
	
	// LEAVECHANNEL COMMAND START
	public boolean leaveChannel(Player player, String[] args){
		plugin.getChManager().playerLeaveChannel(args[0], player);
		return false;
	}
	// LEAVECHANNEL COMMAND END
	
	// CHANNEL COMMAND START
	public boolean executeChannelCmd(Player player, String[] args){
		CommandHandler.ChannelCommands arg = null;
		try {
			arg = CommandHandler.ChannelCommands.valueOf(args[0].toUpperCase());
		} catch (Exception e){
			player.sendMessage(ChatColor.GREEN + "Correct usage: /channel [ban|unban|settings] <Values>");
			return false;
		}
		try {
			switch(arg){
			case BAN:
				return banPlayer(player, args);
				
			case UNBAN:
				return unbanPlayer(player, args);
				
			case SETTINGS:
				return settings(player, args);
				
			case LIST:
				return listChannels(player);
				
			default: return false;
			}
		} catch (Exception e){
			player.sendMessage(ChatColor.GREEN + "Correct usage: /channel [ban|unban|settings] <Values>");
			return false;
		}
	}
	
	public boolean banPlayer(Player player, String[] args){
		if (!plugin.getPluginCommunicationManager().permissions.has(player, permissionsNode + "ch.ban")){
			player.sendMessage(ChatColor.RED + "You can not use that command.");
			return true;
		}
		
		if (args.length == 2){
			Player victim = plugin.getServer().getPlayer(args[1]);
			Channel c = plugin.getChManager().getFocusedChannel(player);
			c.banPlayer(victim);
			if (victim.isOnline()){
				victim.sendMessage(ChatColor.RED + "You have been banned from channel: " + c.getColor() + c.getName());
				return true;
			}
			return true;
		} else {
			player.sendMessage(ChatColor.BLACK + "Correct use:");
			player.sendMessage(ChatColor.YELLOW + "/channel ban <Player>");
			return true;
		}
	}
	
	public boolean unbanPlayer(Player player, String[] args){
		if (!plugin.getPluginCommunicationManager().permissions.has(player, permissionsNode + "ch.unban")){
			player.sendMessage(ChatColor.RED + "You can not use that command.");
			return true;
		}
		if (args.length == 2){
			Player victim = plugin.getServer().getPlayer(args[1]);
			Channel c = plugin.getChManager().getFocusedChannel(player);
			if (!c.isPlayerBanned(victim)){
				player.sendMessage(ChatColor.RED + "Player is not banned!");
				return true;
			}
			c.unbanPlayer(victim);
			if (victim.isOnline()){
				victim.sendMessage(ChatColor.RED + "You have been banned from channel: " + c.getColor() + c.getName());
				return true;
			}
			return true;
		} else {
			player.sendMessage(ChatColor.BLACK + "Correct use:");
			player.sendMessage(ChatColor.YELLOW + "/channel ban <Player>");
			return true;
		}
	}
	
	public boolean listChannels(Player p){
		if (!plugin.getPluginCommunicationManager().permissions.has(p, permissionsNode + "ch.list")){
			p.sendMessage(ChatColor.RED + "You can not use that command.");
			return true;
		}
		String list = ChatColor.YELLOW + "";
		for (Channel c: plugin.getChManager().getChannels()){
			if ((!c.isHidden()) && (!c.isPlayerBanned(p))){
				list = list + " §" + c.getColor() + c.getName() + " - " + c.getShortCut() + "|";
			}
		}
		p.sendMessage(list);
		return true;
	}
	
	/**
	 * Does nothing, until more settings commands are set.
	 * @param player
	 * @param args
	 * @return
	 */
	public boolean settings(Player player, String[] args){
		return settingsCommand(player, args);
	}
	
	public boolean settingsCommand(Player player, String[] args){
		if (!plugin.getPluginCommunicationManager().permissions.has(player, permissionsNode + "ch.settings")){
			player.sendMessage(ChatColor.RED + "You can not use that command.");
			return true;
		}
		if (args.length < 3){
			return false;
		}
		if (args[1].equalsIgnoreCase("cmd")){
			if (args[2].equalsIgnoreCase("true")){
				plugin.settings.setUsingCommands(true);
				player.sendMessage("Enabled.");
				return true;
			} else if (args[2].equalsIgnoreCase("false")){
				plugin.settings.setUsingCommands(false);
				player.sendMessage("Disabled!");
				return true;
			} else {
				player.sendMessage(ChatColor.RED + "Not valid argument: " + args[2] + ". Only 'true' and 'false' is allowed.");
				return true;
			}
		} else {
			return false;
		}
	}
	
	// CHANNEL COMMAND END
	
	public enum ChannelCommands{BAN, UNBAN, LIST, SETTINGS}
}
