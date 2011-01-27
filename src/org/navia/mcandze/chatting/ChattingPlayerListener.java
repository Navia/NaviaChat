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
	
	public void onPlayerCommand(PlayerChatEvent event){
		String[] args = event.getMessage().split(" ");
		if (args[0].equalsIgnoreCase("/c")){
			if (args.length >= 2){
				if (playerCanUseCommand(event.getPlayer(), args[1])){
					if (args.length >= 3){
						
					}
				}
			} else {
				event.getPlayer().sendMessage(ChatColor.GREEN + "Correct usage: /c <Channel> - Joins <Channel>");
			}
		} else if (args[0].equalsIgnoreCase("/ic")){
			
		}
	}
	
	public void onPlayerChat(PlayerChatEvent event){
		Channel focus = plugin.getFocusedChannel(event.getPlayer());
		if (focus.isIc()){
			focus.sendMessage(event.getMessage(), event.getPlayer(), true);
		}
	}
	
	public void onPlayerLogin(PlayerEvent event){
		
	}
	
	public void execute(PlayerChatEvent event, String exe){
		
	}
	
	public boolean playerCanUseCommand(Player player, String arg){
		if (plugin.isUsingPermissions()){
			if ((plugin).permissions.has(player, arg)){
				return true;
			}
			return false;
		}
		return true;
	}
	
}
