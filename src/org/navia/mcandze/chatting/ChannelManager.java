package org.navia.mcandze.chatting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ChannelManager {
	private HashMap<Player, List<Channel>> playerChannels;
	
	private HashMap<Player, Channel> playerFocused;
	
	private List<Channel> channels;
	
	private HashMap<Player, Boolean> playerState;
	
	private Chatting plugin;
	
	public ChannelManager(Chatting instance, ChattingDataSource dsInstance){
		this.plugin = instance;
	}
	
	public void playerChangeChannel(String channel, Player player){
		Channel c;
		if ((c = getChannelWithShortcut(channel)) == null){
			player.sendMessage(ChatColor.RED + "Channel does not exist: " + channel);
		}
		setFocusedChannel(c, player);
		playerAddChannel(c, player);
		
	}
	
	public void initialize(){
		playerChannels = new HashMap<Player, List<Channel>>();
		playerFocused = new HashMap<Player, Channel>();
		channels = new ArrayList<Channel>();
		playerState = new HashMap<Player, Boolean>();
		
		loadChannels();
		for (Player p: plugin.getServer().getOnlinePlayers()){
			List<Channel> tmpr = new ArrayList<Channel>();
			tmpr.add(getFirstDefaultChannel());
			playerChannels.put(p, tmpr);
		}
	}
	
	public void addChannel(Channel c){
		channels.add(c);
	}
	
	public void playerLeaveChannel(String channel, Player player){
		if (!playerChannels.containsKey(player))
			player.sendMessage(ChatColor.RED + "You are not in a channel!");
		playerChannels.get(player).remove(getChannelWithShortcut(channel));
		if (!playerChannels.get(player).isEmpty()){
			for (Channel c: playerChannels.get(player)){
				if (c.isDefaultOnLogin()){
					setFocusedChannel(c, player);
				}
			}
		} else {
			player.sendMessage("You are not in any channels. Chat has been disabled.");
		}
	}
	
	public Channel getChannelWithShortcut(String name){
		for (Channel c: channels){
			if (c.getShortCut().equals(name)){
				return c;
			}
		}
		return null;
	}
	
	public Channel getFirstDefaultChannel(){
		for (Channel c: channels){
			if (c.isDefaultOnLogin()){
				return c;
			}
		}
		return null;
	}
	
	public void loadChannels(){
		channels = plugin.getDataSource().getChannels();
		for (Channel c: plugin.getDataSource().loadNewChannels()){
			channels.add(c);
		}
		for (Channel c: channels){
			for (Player p: plugin.getServer().getOnlinePlayers()){
				
				if (c.isDefaultOnLogin()){
					playerChannels.get(p).add(c);
				}
				if (c.isFocusedOnDefault()){
					playerFocused.remove(p);
					playerFocused.put(p, c);
				}
			}
		}
	}
	
	public void setPlayerState(Player player, boolean ic){
		if (playerState.containsKey(player)){
			playerState.remove(player);
		}
		playerState.put(player, ic);
	}
	
	public boolean playerIsIc(Player player){
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
	public boolean playerIsInChannel(Player player, Channel channel){
		List<Channel> channels = playerChannels.get(player);
		if (channels.contains(channel)){return true;}
		return false;
	}
	
	/**
	 * Gets this player's focused channel.
	 * @param player
	 * @return
	 */
	public Channel getFocusedChannel(Player player){
		if (!playerChannels.containsKey(player))
			return null;
		if (!playerFocused.containsKey(player))
			return null;
		return playerFocused.get(player);
	}
	
	public void setFocusedChannel(Channel channel, Player player){
		if (channels.contains(channel)){
			if (playerFocused.containsKey(player)){
				playerFocused.remove(player);
			}
			playerFocused.put(player, channel);
			return;
		}
		player.sendMessage(ChatColor.RED + "That channel does not exist!");
		
	}
	
	public void playerAddChannel(Channel c, Player p){
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
}
