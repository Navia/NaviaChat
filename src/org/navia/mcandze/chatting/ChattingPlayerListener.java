package org.navia.mcandze.chatting;

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
		ChannelManager cm = plugin.getChManager();
		Channel c = cm.getFocusedChannel(event.getPlayer());
		if (c != null){
			c.sendMessage(event.getMessage(), event.getPlayer(), cm.playerIsIc(event.getPlayer()));
		}
		
		event.setCancelled(true);
		
	}
	
	public void onPlayerLogin(PlayerEvent event){
		plugin.getChManager().setFocusedChannel(plugin.getChManager().getFirstDefaultChannel(), event.getPlayer());
	}
	
}
