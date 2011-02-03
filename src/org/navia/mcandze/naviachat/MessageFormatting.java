package org.navia.mcandze.naviachat;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
/**
 * Holds methods for encoding messages, for specific channels.
 * @author Andreas
 *
 */
public class MessageFormatting {
	
	
	private int line = 54;
	
	public static String encodeLocalMessage(Player player, NaviaChat instance, String message, Channel channel, boolean ic){
		// TODO: Characterizationing support, once that's done.
		String name;
		if (!instance.getPluginCommunicationManager().isUsingCharacterPlugin()){
			name = player.getDisplayName();
		}
		// Temporary
		name = player.getDisplayName();
		//
		if (!ic){
			name = name + "[OOC]";
		}
		
		return name + ": " + message; 
	}
	
	public static String encodeGlobalMessage(Player player, NaviaChat instance, String message, Channel channel){
		// ['Color''ChannelName']<'PlayerColor''PlayerName'> 'Message'
		String prefix;
		String finalString;
		String playerColor = "f";
		if (instance.getPluginCommunicationManager().isUsingPermissions()){
			prefix = instance.getPluginCommunicationManager().permissions.getGroupPrefix(instance.getPluginCommunicationManager().permissions.getGroup(player.getName()));
		} else {
			prefix = "f";
		}
		
		// TODO: Playercolor, from Characterizationing plugin.
		
		finalString =
			"§" + channel.getColor()
			+ "[" + channel.getName() + "]"
			+ "§" + playerColor + "<" + "§" + prefix + player.getDisplayName() + "§f> "
			+ message;
		
		// Final String
		
		return finalString;
	}
	
	public static boolean playerCanTalk(Player player, NaviaChat instance){
		if (instance.getPluginCommunicationManager().permissions.has(player, "naviachat.chat.cantalk")){
			return true;
		} else {
			player.sendMessage(ChatColor.RED + "You are muted by an administrator.");
			return false;
		}
	}
}
