package org.navia.mcandze.chatting;

import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ChannelManager {
	private HashMap<Player, List<Channel>> playerChannels;
	
	private HashMap<Player, Channel> playerFocused;
	
	private List<Channel> channels;
	
	private List<Player> playerState;
	
	private HashMap<Player, String> icnames;
	
	public ChattingDataSource dataSource;
	
	public void playerChangeChannel(String channel, Player player){
		Channel c;
		if ((c = getChannelWithShortcut(channel)) == null){
			player.sendMessage(ChatColor.RED + "Channel does not exist: " + channel);
		}
		setFocusedChannel(c, player);
		playerAddChannel(c, player);
		
	}
	
	public Channel getChannelWithShortcut(String name){
		for (Channel c: channels){
			if (c.getShortCut().equals("channel")){
				return c;
			}
		}
		return null;
	}
	
	public void loadChannels(){
		channels = dataSource.getChannels();
		
		for (Channel c: dataSource.loadNewChannels()){
			channels.add(c);
		}
		for (Channel c: channels){
			for (Player p: getServer().getOnlinePlayers()){
				
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
		return playerState.contains(player);
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
		playerFocused.remove(player);
		playerFocused.put(player, channel);
	}
	
	public void playerAddChannel(Channel c, Player p){
		if (!playerChannels.get(p).contains(p)){
			playerChannels.get(p).add(c);
		}
	}
}
