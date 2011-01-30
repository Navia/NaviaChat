package org.navia.mcandze.chatting;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
/**
 * Holds methods for encoding messages, for specific channels.
 * @author Andreas
 *
 */
public class MessageFormatting {
	
	
	private int line = 54;
	
	public String encodeLocalMessage(Player player, Chatting instance, String message, Channel channel, boolean ic){
		// TODO: Characterizationing support, once that's done.
		String name;
		if (!instance.getPluginCommunicationManager().isUsingCharacterPlugin()){
			name = player.getDisplayName();
		}
		// Temporary
		name = player.getDisplayName();
		//
		if (ic){
			name = "[OOC]" + name;
		}
		
		return name + ": " + message; 
	}
	
	public String encodeGlobalMessage(Player player, Chatting instance, String message, Channel channel){
		// ['Color''ChannelName']<'PlayerColor''PlayerName'> 'Message'
		String prefix;
		String finalString;
		String playerColor = "f";
		if (instance.getPluginCommunicationManager().isUsingPermissions()){
			prefix = instance.getPluginCommunicationManager().permissions.getGroupPrefix(instance.getPluginCommunicationManager().permissions.getGroup(player.getName())) + "_";
		} else {
			prefix = "";
		}
		
		// TODO: Playercolor, from Characterizationing plugin.
		
		finalString =
			"§" + channel.getColor()
			+ "[" + channel.getName() + "]"
			+ "§" + playerColor + "<" + prefix + player.getDisplayName() + "> "
			+ message;
		
		// Final String
		
		return finalString;
	}
	
	public boolean playerCanTalk(Player player, Chatting instance){
		if (instance.getPluginCommunicationManager().permissions.has(player, "chatting.chat.cantalk")){
			return true;
		} else {
			player.sendMessage(ChatColor.RED + "You are muted by an administrator.");
			return false;
		}
	}
}
