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
		// TODO: Some cool message formatting. Suggestions?
		String name = instance.getChannelManager().getIcName(player);
		
		return name + ": " + message; 
	}
	
	public String encodeGlobalMessage(Player player, Chatting instance, String message, Channel channel){
		// ['Color''ChannelName']<'PlayerColor''PlayerName'> 'Message'
		return "[§"
		+ channel.getColor()
		+ "]<"
		+ instance.getPermissions().getGroupPrefix(instance.getPermissions().getGroup(player.getName()))
		+ player.getName()
		+ "> "
		+ message;
	}
	
	public boolean playerCanTalk(Player player, Chatting instance){
		if ((instance).getPermissions().has(player, "chatting.chat.cantalk")){
			return true;
		} else {
			player.sendMessage(ChatColor.RED + "You are muted by an administrator.");
			return false;
		}
	}
}
