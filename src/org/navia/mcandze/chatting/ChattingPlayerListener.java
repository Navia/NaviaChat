package org.navia.mcandze.chatting;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerListener;
/**
 * The Player Listener.
 * @author Andreas
 *
 */
public class ChattingPlayerListener extends PlayerListener{
	Chatting plugin;
	
	public ChattingPlayerListener(Chatting instance){
		plugin = instance;
	}
	
	public void onPlayerChat(PlayerChatEvent event){
		if (plugin.getChannelManager().playerIsIc(event.getPlayer())){
			if (plugin.getChannelManager().getFocusedChannel(event.getPlayer()).isIc()){
				plugin.getChannelManager().getFocusedChannel(event.getPlayer()).sendMessage(event.getMessage(), event.getPlayer(), true);
			}
		}
		
	}
	
	public void onPlayerLogin(PlayerEvent event){
		plugin.getChannelManager().setFocusedChannel(plugin.getChannelManager().getFirstDefaultChannel(), event.getPlayer());
	}
	
}
