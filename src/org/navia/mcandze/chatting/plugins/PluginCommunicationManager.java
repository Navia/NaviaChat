package org.navia.mcandze.chatting.plugins;

import java.util.logging.Logger;

import org.bukkit.plugin.Plugin;
import org.navia.mcandze.chatting.Chatting;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

public class PluginCommunicationManager {
	
	private Chatting mainPlugin;
	
	public static PermissionHandler permissions;
	// Is null if we don't use the Characterizationing plugin.
	private Plugin Characterizationing;
	
	private Logger log;
	
	public PluginCommunicationManager(Chatting instance){
		this.mainPlugin = instance;
	}
	
	public void initialize(){
		loadPermissions();
		loadCharacterPlugin();
	}
	
	/**
	 * Loads the permissions plugin.
	 */
	public void loadPermissions(){
		Plugin test = mainPlugin.getServer().getPluginManager().getPlugin("Permissions");
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
	 * Loads Characterizationing. 
	 */
	public void loadCharacterPlugin(){
		try {
			Characterizationing = mainPlugin.getServer().getPluginManager().getPlugin("Characterizationing");
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
	 * Checks if the Characterizationing plugin is installed.
	 * @return
	 */
	public boolean isUsingCharacterPlugin(){
		if (Characterizationing == null){
			return false;
		}
		return true;
	}
}
