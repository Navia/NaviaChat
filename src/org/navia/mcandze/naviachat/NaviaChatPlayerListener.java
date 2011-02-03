package org.navia.mcandze.naviachat;

import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerListener;
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
		ChannelManager cm = plugin.getChManager();
		Channel c = cm.getFocusedChannel(event.getPlayer());
		String message;
		if (!plugin.getPluginCommunicationManager().permissions.has(event.getPlayer(), "naviachat.chat.cantalk")){
			event.setCancelled(true);
			return;
		}
		if (!plugin.settings.isUsingCommands()){
			if (event.getMessage().startsWith(".")){
				message = event.getMessage().substring(0);
				plugin.getChManager().setPlayerState(event.getPlayer(), false);
			} else {
				message = event.getMessage();
				plugin.getChManager().setPlayerState(event.getPlayer(), true);
			}
			
		} else {
			message = event.getMessage();
		}
		if (c != null){
			c.sendMessage(message, event.getPlayer(), cm.playerIsIc(event.getPlayer()));
		}
		if (!plugin.settings.isUsingCommands()){
			plugin.getChManager().setPlayerState(event.getPlayer(), !plugin.getChManager().playerIsIc(event.getPlayer()));
		}
		
		event.setCancelled(true);
		
	}
	
	public void onPlayerLogin(PlayerEvent event){
		plugin.getChManager().playerChangeChannel(plugin.getChManager().getFirstDefaultChannel().getsCut(), event.getPlayer());
		for (Channel c: plugin.getChManager().channels){
			plugin.getChManager().playerAddChannel(c, event.getPlayer());
		}
		
	}
	
}
