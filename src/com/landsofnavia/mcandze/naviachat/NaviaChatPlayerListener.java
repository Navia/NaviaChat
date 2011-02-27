package com.landsofnavia.mcandze.naviachat;

import java.util.StringTokenizer;

import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerListener;

import com.landsofnavia.mcandze.naviachat.channel.Channel;
import com.landsofnavia.mcandze.naviachat.plugins.ExtensionManager;
import com.landsofnavia.mcandze.naviachat.plugins.DerpPermissionChecker;
/**
 * The Player Listener.
 * @author Andreas
 *
 */
public class NaviaChatPlayerListener extends PlayerListener{
	NaviaChat plugin;
	
	public NaviaChatPlayerListener(NaviaChat instance){
		plugin = instance;
	}
	
	public void onPlayerChat(PlayerChatEvent event){
		Channel c = ChannelManager.getFocusedChannel(event.getPlayer());
		String message;
		
		if (!DerpPermissionChecker.playerCanTalk(event.getPlayer())){
				event.setCancelled(true);
				return;
		}
		
		if (event.getMessage().startsWith(".")){
			message = event.getMessage().substring(1);
			ChannelManager.setPlayerState(event.getPlayer(), false);
		} else {
			message = event.getMessage();
			ChannelManager.setPlayerState(event.getPlayer(), true);
		}
		
		if (c != null){
//			c.sendMessage(message, event.getPlayer(), ChannelManager.playerIsIc(event.getPlayer()));
		} else {
			event.getPlayer().sendMessage("Something went wrong - (Ask your Admin to report to McAndze)");
		}
		
		ChannelManager.setPlayerState(event.getPlayer(), !ChannelManager.playerIsIc(event.getPlayer()));
		event.setCancelled(true);
		
	}
	
	public void onPlayerJoin(PlayerEvent event){
		ChannelManager.initializePlayerChannels(event.getPlayer());
		
	}
	
}
