package org.navia.mcandze.naviachat;

import java.io.File;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.navia.mcandze.naviachat.plugins.PluginCommunicationManager;

import com.nijikokun.bukkit.iProperty;

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
	// Player Listener
	private final NaviaChatPlayerListener playerListener = new NaviaChatPlayerListener(this);
	// The Logger
	private Logger log;
	// Handles channels, and the players that are in them.
	private ChannelManager chManager;
	
	private PluginCommunicationManager pluginCommunicationManager;
	
	private NaviaChatDataSource dataSource;
	
	public Settings settings;
	
	private CommandHandler commands;
	
	/**
	 * Default constructor for a plugin.
	 * @param pluginLoader
	 * @param instance
	 * @param desc
	 * @param folder
	 * @param plugin
	 * @param cLoader
	 */
	public NaviaChat(PluginLoader pluginLoader, Server instance, PluginDescriptionFile desc, File folder, File plugin, ClassLoader cLoader){
		super(pluginLoader, instance, desc, folder, plugin, cLoader);
		
		log = Logger.getLogger("Minecraft");
		PluginDescriptionFile pdfFile = this.getDescription();
		log.info("[NaviaChat] " + pdfFile.getName() + " " + pdfFile.getVersion() + ", by Mcandze, is enabled.");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args){
		String commandName = command.getName();
		Player player = null;
		
		if (!sender.isPlayer()){
			return false;
		} else {
			player = (Player)sender;
		}
		
		if (commandName.equalsIgnoreCase("ch")){
			if (args.length < 1){
				return false;
			}
			return commands.changeChannel(player, args);
		} else if(commandName.equalsIgnoreCase("leavechannel")){
			if (args.length < 1){
				return false;
			}
			return commands.leaveChannel(player, args);
		} else if (commandName.equalsIgnoreCase("channel")){
			if (args.length < 1){
				return false;
			}
			
			commands.executeChannelCmd(player, args);
		}
		return false;
	}
	
	/**
	 * Default method
	 */
	public void onEnable(){
		pluginCommunicationManager = new PluginCommunicationManager(this);
		pluginCommunicationManager.initialize();	
		
		settings = new Settings(new iProperty(/*"plugins" + File.separator + "Data" + File.separator + "Config" + File.separator + "Settings" + File.separator +*/ "settings.properties"));
		settings.initialize();	
		
		commands = new CommandHandler(this);
		
		dataSource = new NaviaChatDataSource(this);
		//dataSource.initialize();
		
		chManager = new ChannelManager(this, dataSource);
		chManager.initialize();
		for (Player p: getServer().getOnlinePlayers()){
			chManager.playerChangeChannel(chManager.getFirstDefaultChannel().getsCut(), p);
		}
		PluginManager pm = getServer().getPluginManager();
		
		pm.registerEvent(Event.Type.PLAYER_CHAT, playerListener, Priority.Normal, this);
	}

	/**
	 * @return the chManager
	 */
	public ChannelManager getChManager() {
		return chManager;
	}

	/**
	 * @param chManager the chManager to set
	 */
	public void setChManager(ChannelManager chManager) {
		this.chManager = chManager;
	}
	
	

	/**
	 * @return the pluginCommunicationManager
	 */
	public PluginCommunicationManager getPluginCommunicationManager() {
		return pluginCommunicationManager;
	}

	/**
	 * @param pluginCommunicationManager the pluginCommunicationManager to set
	 */
	public void setPluginCommunicationManager(
			PluginCommunicationManager pluginCommunicationManager) {
		this.pluginCommunicationManager = pluginCommunicationManager;
	}

	/**
	 * @return the dataSource
	 */
	public NaviaChatDataSource getDataSource() {
		return dataSource;
	}

	/**
	 * @param dataSource the dataSource to set
	 */
	public void setDataSource(NaviaChatDataSource dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * Default method
	 */
	public void onDisable(){
		
	}

}
