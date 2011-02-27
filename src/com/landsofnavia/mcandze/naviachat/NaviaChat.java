package com.landsofnavia.mcandze.naviachat;

import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

import com.landsofnavia.mcandze.naviachat.command.CommandHandler;
import com.landsofnavia.mcandze.naviachat.plugins.ExtensionManager;
import com.landsofnavia.mcandze.naviacore.LogIt;

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
public class NaviaChat extends JavaPlugin{
	public static final String sPlugin = "Chat";
	// Player Listener
	private final NaviaChatPlayerListener playerListener = new NaviaChatPlayerListener(this);
	// The Logger
	private Logger log;
	// Handles channels, and the players that are in them.
	
	public Settings settings;
	public static Server server;
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args){
		String commandName = command.getName();
		
		if (!sender.isPlayer()){
			return true;
		}
		Player player = (Player)sender;
		
		return CommandHandler.doCommand(commandName, player, args);
	}
	
	/**
	 * Default method
	 */
	public void onEnable(){
		server = getServer();
		
		initializeStuff();
		PluginDescriptionFile pdfFile = getDescription();
		LogIt.logInfo(sPlugin, pdfFile.getName() + " " + pdfFile.getVersion() + " by Mcandze, is enabled.");	
		
		for (Player p: getServer().getOnlinePlayers()){
			ChannelManager.initializePlayerChannels(p);
		}
		
		PluginManager pm = getServer().getPluginManager();
		
		pm.registerEvent(Event.Type.PLAYER_CHAT, playerListener, Priority.High, this);
		pm.registerEvent(Event.Type.PLAYER_JOIN, playerListener, Priority.Normal, this);
	}
	

	/**
	 * Default method
	 */
	public void onDisable(){
		
	}
	
	public void initializeStuff(){
		ChannelManager.initialize();
		ExtensionManager.loadNaviaChar();
		ExtensionManager.loadPermissions();
	}
	


}
