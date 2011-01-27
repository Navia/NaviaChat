package org.navia.mcandze.chatting;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

/**
 * This program is free software. It comes without any warranty, to
 * the extent permitted by applicable law. You can redistribute it
 * and/or modify it under the terms of the Do What The Fuck You Want
 * To Public License, Version 2, as published by Sam Hocevar. See
 * underneath for more details.
 */

/**
 * DO WHAT THE FUCK YOU WANT TO PUBLIC LICENSE
 *                   Version 2, December 2004
 *
 * Copyright (C) 2004 Sam Hocevar <sam@hocevar.net>
 *
 * Everyone is permitted to copy and distribute verbatim or modified
 * copies of this license document, and changing it is allowed as long
 * as the name is changed.
 *
 *          DO WHAT THE FUCK YOU WANT TO PUBLIC LICENSE
 * TERMS AND CONDITIONS FOR COPYING, DISTRIBUTION AND MODIFICATION
 *
 * 0. You just DO WHAT THE FUCK YOU WANT TO.
 */

/**
 * The above license applies to all classes, but iProperty.java
 * which is made by Nijikokun. The license is included in the class.
 */
/**
 * 
 * @author McAndze
 *
 */
public class Chatting extends JavaPlugin{
	// Player Listener
	private final ChattingPlayerListener playerListener = new ChattingPlayerListener(this);
	// The Logger
	private Logger log;
	// 
	private Plugin Characterizationing;
	
	public boolean usingCharacterizationing;
	
	private HashMap<Player, List<Channel>> playerChannels;
	
	private HashMap<Player, Channel> playerFocused;
	
	private List<Channel> channels;
	
	private List<Player> playerState;
	
	private HashMap<Player, String> icnames;
	
	public PermissionHandler permissions = null;
	
	public ChattingDataSource dataSource;
	
	/**
	 * Default constructor for a plugin.
	 * @param pluginLoader
	 * @param instance
	 * @param desc
	 * @param folder
	 * @param plugin
	 * @param cLoader
	 */
	public Chatting(PluginLoader pluginLoader, Server instance, PluginDescriptionFile desc, File folder, File plugin, ClassLoader cLoader){
		super(pluginLoader, instance, desc, folder, plugin, cLoader);
		
		log = Logger.getLogger("Minecraft");
		PluginDescriptionFile pdfFile = this.getDescription();
		log.info("[Chatting] " + pdfFile.getName() + " is enabled.");
	}
	
	/**
	 * Default method
	 */
	public void onEnable(){
		dataSource = new ChattingDataSource(this);
		dataSource.initialize();
		channels = dataSource.getChannels();
		playerState = new ArrayList<Player>();
		loadChannels();
		loadPlugin();
		PluginManager pm = getServer().getPluginManager();
		
		pm.registerEvent(Event.Type.PLAYER_CHAT, playerListener, Priority.Normal, this);
	}
	
	/**
	 * Default method
	 */
	public void onDisable(){
		
	}
	
	/**
	 * Loads channels from a SQLite database, and loads new channels from a folder.
	 */
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
	
	/**
	 * Checks if the Characterizationing plugin is installed.
	 * @return
	 */
	public boolean isUsingCharacterPlugin(){
		if (Characterizationing == null){
			return false;
		}
		return true;
	}
	
	/**
	 * Loads Characterizationing. 
	 */
	public void loadPlugin(){
		try {
			Characterizationing = getServer().getPluginManager().getPlugin("Characterizationing");
			if (Characterizationing != null){
				log.info("[Chatting] " + "Established connection to Characterizationing");
				usingCharacterizationing = true;
				return;
			}
			Characterizationing = null;
			usingCharacterizationing = false;
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Loads the permissions plugin.
	 */
	public void loadPermissions(){
		Plugin test = getServer().getPluginManager().getPlugin("Permissions");
		if (this.permissions == null){
			if (test != null){
				this.permissions = ((Permissions)test).getHandler();
			} else {
				log.warning("[Chatting] " + " Running WITHOUT the Permissions plugin! All commands are unrestricted.");
				
			}
		}
		
	}
	
	/**
	 * Checks if using Permissions.
	 * @return
	 */
	public boolean isUsingPermissions(){
		return this.permissions != null;
	}
	
	/**
	 * Gets the In Character names of the player.
	 * @param player
	 * @return
	 */
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
	
	public void setFocusedChannel(String channel, Player player){
		for (Channel c: channels){
			if (c.getShortCut().equals("channel")){
				playerFocused.put(player, c);
			}
		}
	}
}
