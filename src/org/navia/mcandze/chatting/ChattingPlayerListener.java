package org.navia.mcandze.chatting;

import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerListener;

public class ChattingPlayerListener extends PlayerListener{
	Chatting plugin;
	
	public ChattingPlayerListener(Chatting instance){
		plugin = instance;
	}
	
	public void onPlayerChat(PlayerChatEvent event){
		plugin.getFocusedChannel(event.getPlayer()).sendMessage(event.getMessage(), event.getPlayer());
	}
	
	public void onPlayerLogin(PlayerEvent event){
		
	}
}
