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
	
	private HashMap<Player, String> icnames;
	
	private Chatting plugin;
	
	private ChattingDataSource dataSource;
	
	public ChannelManager(Chatting instance, ChattingDataSource dsInstance){
		this.plugin = instance;
		this.dataSource = dsInstance;
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
		playerChannels.get(player).remove(getChannelWithShortcut(channel));
		if (playerChannels.get(player).isEmpty()){
			Channel newC = getFirstDefaultChannel();
			if (newC == null){
				player.sendMessage(ChatColor.RED + "ERROR: You have been put back in the last channel you were in. REASON: No default channels. Contact the server administrator.");
			}
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
		channels = dataSource.getChannels();
		dataSource.loadNewChannels();
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
	
	public String getIcName(Player player){
		return icnames.get(player);
	}
	
	public boolean playerIsIc(Player player){
		/*if (playerState.containsKey(player)){
			return playerState.get(player).booleanValue();
		} else {
			return false;
		}*/
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
		return playerFocused.get(player);
	}
	
	public void setFocusedChannel(Channel channel, Player player){
		if (channels.contains(channel)){
			playerFocused.remove(player);
			playerFocused.put(player, channel);
			return;
		}
		player.sendMessage(ChatColor.RED + "That channel does not exist!");
		
	}
	
	public void playerAddChannel(Channel c, Player p){
		if (!playerChannels.get(p).contains(p)){
			playerChannels.get(p).add(c);
		}
	}
}
