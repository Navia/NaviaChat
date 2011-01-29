package org.navia.mcandze.chatting;

import java.io.File;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
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
	// Is null if we don't use the Characterizationing plugin.
	private Plugin Characterizationing;
	// Handles channels, and the players that are in them.
	private ChannelManager chManager;
	
	private String permissionsNode = "chatting.chat";
	
	private ChattingDataSource dataSource;
	
	private PermissionHandler permissions;
	
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
	
	@Override
	public boolean onCommand(Player player, Command command, String commandLabel, String[] args){
		String commandName = command.getName();
		
		if (commandName.equalsIgnoreCase("ch")){
			if (!(this).permissions.has(player, permissionsNode + "ch")){
				player.sendMessage(ChatColor.RED + "You can not use that command.");
				return true;
			}
			if (args.length != 1){
				return false;
			}
			chManager.setFocusedChannel(chManager.getChannelWithShortcut(args[0]), player);
			return true;
		}
		
		if (commandName.equalsIgnoreCase("leavechannel")){
			if (!(this).permissions.has(player, permissionsNode + "leavechannel")){
				player.sendMessage(ChatColor.RED + "You can not use that command.");
				return true;
			}
			if (args.length != 1){
				return false;
			}
			chManager.playerLeaveChannel(args[0], player);
		}
		return false;
	}
	
	/**
	 * Default method
	 */
	public void onEnable(){
		permissions = null;
		loadPlugin();
		loadPermissions();
		dataSource = new ChattingDataSource(this);
		dataSource.initialize();
		chManager = new ChannelManager(this, dataSource);
		chManager.initialize();
		PluginManager pm = getServer().getPluginManager();
		
		pm.registerEvent(Event.Type.PLAYER_CHAT, playerListener, Priority.Normal, this);
	}
	
	/**
	 * @return the permissions
	 */
	public PermissionHandler getPermissions() {
		return permissions;
	}

	/**
	 * @param permissions the permissions to set
	 */
	public void setPermissions(PermissionHandler permissions) {
		this.permissions = permissions;
	}

	/**
	 * Default method
	 */
	public void onDisable(){
		
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
				return;
			}
			Characterizationing = null;
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
	
	public ChannelManager getChannelManager(){
		return chManager;
	}

}
