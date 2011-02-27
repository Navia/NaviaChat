package com.landsofnavia.mcandze.naviachat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import com.landsofnavia.mcandze.naviachat.channel.Channel;
import com.landsofnavia.mcandze.naviachat.plugins.ExtensionManager;
import com.landsofnavia.mcandze.naviacore.LogIt;
public class ChannelManager {
	public static HashMap<Player, List<Channel>> playerChannels;
	
	private static HashMap<Player, Channel> playerFocused;
	
	public static List<Channel> channels;
	
	private static HashMap<Player, Boolean> playerState;

	
	
	public static boolean playerChangeChannel(String channel, Player player){
		Channel c;
		if (!channelExists((c = getChannelWithShortcut(channel)))){
			player.sendMessage(ChatColor.RED + "Channel does not exist: " + channel);
			return false;
		}
		setFocusedChannel(c, player);
		playerAddChannel(c, player);
		return true;
	}
	
	public static boolean playerChangeChannel(Channel channel, Player player){
		if (!channelExists(channel)){
			player.sendMessage(ChatColor.RED + "Channel does not exist: " + channel);
			return false;
		}
		
		if (!playerIsInChannel(player, channel)){
			playerAddChannel(channel, player);
		}
		setFocusedChannel(channel, player);
		return true;
	}
	
	public static void initialize(){
		playerChannels = new HashMap<Player, List<Channel>>();
		playerFocused = new HashMap<Player, Channel>();
		channels = new ArrayList<Channel>();
		playerState = new HashMap<Player, Boolean>();
		
		loadChannels();
		for (Player p: NaviaChat.server.getOnlinePlayers()){
			initializePlayerChannels(p);
		}
	}
	
	public static void playerLeaveChannel(Channel c, Player p){
		List<Channel> channels;
		(channels = playerChannels.get(p)).remove(c);
		playerChannels.remove(p);
		playerChannels.put(p, channels);
		if (playerChannels.get(p).isEmpty()){
			p.sendMessage(ChatColor.RED + "Could not find any channels. You are muted, until you join another channel.");
			return;
		}
		playerChangeChannel(playerChannels.get(p).get(0), p);
	}
	
	public static Channel getChannelWithShortcut(String name){
		for (Channel c: channels){
			if (c.getShortCut().equals(name)){
				return c;
			}
		}
		return null;
	}
	
	public static void initializePlayerChannels(Player player){
		StringTokenizer strTok = new StringTokenizer(ExtensionManager
				.permissions
				.getGroupPermissionString(ExtensionManager.permissions.getGroup(player.getName()), "default-channels"),
				",");
		while (strTok.hasMoreTokens()){
			playerChangeChannel(getChannelWithShortcut(strTok.nextToken()), player);
		}
		
		playerChangeChannel(getFirstFocusedChannel(player), player);
	}
	
	public static Channel getFirstFocusedChannel(Player player){
		Channel c;
		
		if ((c = getChannelWithShortcut(ExtensionManager
		.permissions
		.getGroupPermissionString(ExtensionManager.permissions.getGroup(player.getName()), "focused-on-login"))) != null){
			return c;
		}
		return null;
	}
	
	public static void loadChannels(){
		channels = Settings.getChannels();

		
		LogIt.logInfo(NaviaChat.sPlugin, "Loaded " + channels.size() + " channels.");
	}
	
	public static void setPlayerState(Player player, boolean ic){
		if (playerState.containsKey(player)){
			playerState.remove(player);
		}
		playerState.put(player, ic);
	}
	
	public static boolean playerIsIc(Player player){
		if (playerState.containsKey(player)){
			return playerState.get(player);
		}
		return false;
		
	}
	
	/**
	 * Checks if <player> is in channel.
	 * @param player
	 * @param channel
	 * @return
	 */
	public static boolean playerIsInChannel(Player player, Channel channel){
		if (!playerChannels.containsKey(player)){
			return false;
		}
		if (playerChannels.get(player).contains(channel)){
			return true;
		}
		return false;
	}
	
	
	/**
	 * Gets this player's focused channel.
	 * @param player
	 * @return
	 */
	public static Channel getFocusedChannel(Player player){
		if (!playerChannels.containsKey(player))
			return null;
		if (!playerFocused.containsKey(player))
			return null;
		return playerFocused.get(player);
	}
	
	public static void setFocusedChannel(Channel channel, Player player){
		if (playerFocused.containsKey(player)){
			playerFocused.remove(player);
		}
		playerFocused.put(player, channel);
		
	}
	
	public static void playerAddChannel(Channel c, Player p){
		if (!playerChannels.containsKey(p)){
			List<Channel> tmpr = new ArrayList<Channel>();
			tmpr.add(c);
			playerChannels.put(p, tmpr);
		} else {
			if (!playerChannels.get(p).contains(c)){
				playerChannels.get(p).add(c);
			}
		}
	}
	
	public static boolean channelExists(Channel c){
		if (channels.contains(c)){
			return true;
		}
		return false;
	}
}
