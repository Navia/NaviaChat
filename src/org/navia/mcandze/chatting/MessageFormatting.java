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
	
	public String encodeLocalMessage(Player player, Chatting instance, String message, Channel channel){
		// TODO: Add multi-coloured lines support.
		
	}
	
	public String encodeGlobalMessage(Player player, Chatting instance, String message, Channel channel){
		
		return null;
	}
	
	public boolean playerCanTalk(Player player, Chatting instance){
		if ((instance).permissions.has(player, "chatting.chat.cantalk")){
			return true;
		} else {
			player.sendMessage(ChatColor.RED + "You are muted by an administrator.");
			return false;
		}
	}
}
