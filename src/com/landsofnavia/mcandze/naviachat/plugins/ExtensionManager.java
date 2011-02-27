package com.landsofnavia.mcandze.naviachat.plugins;

import org.bukkit.plugin.Plugin;

import com.landsofnavia.mcandze.naviachat.NaviaChat;
import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

public class ExtensionManager {
	
	public static PermissionHandler permissions = null;
	public static boolean isUsingNaviaChar = false;
	
	public static boolean isUsingPermissions(){
		return permissions != null;
	}
	
	public static void loadNaviaChar(){
		if (NaviaChat.server.getPluginManager().getPlugin("NaviaChar") != null){
			isUsingNaviaChar = true;
		}
	}
	
	public static void loadPermissions(){
		Plugin test = NaviaChat.server.getPluginManager().getPlugin("Permissions");
		if (test != null){
			permissions = ((Permissions)test).Security;
		}
	}
}
