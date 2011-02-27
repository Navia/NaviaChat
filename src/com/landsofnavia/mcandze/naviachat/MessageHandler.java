package com.landsofnavia.mcandze.naviachat;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.navia.java.plugins.mcandze.CharHandler;

import com.landsofnavia.mcandze.naviachat.channel.Channel;
import com.landsofnavia.mcandze.naviachat.chmarkup.ChInterpreter;
import com.landsofnavia.mcandze.naviachat.plugins.ExtensionManager;
/**
 * Holds methods for encoding messages, for specific channels.
 * @author Andreas
 *
 */
public class MessageHandler {
	
	private static final int LINEBREAK = 53;
	
	public static ArrayList<String> formatMessage(Channel c, Player sender, String originalMessage){
		String format = Settings.channelsConfig.getString(c.getName() + ".format");
		
		return breakMessage(ChInterpreter.interpretString(format, c, sender, originalMessage));
	}
	
	public static void sendIndependentLocalMessage(Location sender, String message, int range){
		for (Player p: NaviaChat.server.getOnlinePlayers()){
			if (isPlayerInDistanceOf(p,sender,range)){
				p.sendMessage(message);
			}
		}
	}
	
	public static boolean isPlayerInDistanceOf(Player receiver, Location sender, int range){
		double xP = 
			Math.pow(sender.getX() - receiver.getLocation().getX(), 2);
		double yP = 
			Math.pow(sender.getY() - receiver.getLocation().getY(), 2);
		double zP = 
			Math.pow(sender.getZ() - receiver.getLocation().getZ(), 2);
		if (Math.sqrt(xP + yP + zP) <= range){
			return true;
		}
		return false;
	}
	
	public void noOneIsNear(Player p){
		ArrayList<String> messages = new ArrayList<String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(Settings.mainDirectory + Settings.settingsDirectory + "messages.txt"));
			String sCurLine;
			while ((sCurLine = br.readLine()) != null){
				messages.add(sCurLine);
			}
			br.close();
		} catch (Exception e){
			e.printStackTrace();
		}
		Random test = new Random();
		if (!messages.isEmpty()){
			p.sendMessage(ChatColor.GREEN + messages.get(test.nextInt(messages.size())));
		} else {
			p.sendMessage(ChatColor.GREEN + "No one is in range.");
		}
		
	}
	
	public static String getLocalMessage(Player player, String message, Channel channel, boolean ic){
		String name;
		
		if (ic){
			if (ExtensionManager.isUsingNaviaChar){
				if (CharHandler.playerHasACharacter(player)){
					name = CharHandler.getCharacterByPlayerName(player.getName()).getCharacterName().split(" ")[0];
				} else {
					name = player.getDisplayName();
				}
			} else {
				name = player.getDisplayName();
			}
		} else {
			if (ExtensionManager.isUsingNaviaChar){
				if (CharHandler.playerHasACharacter(player)){
					name = player.getDisplayName() + ChatColor.GOLD + "(" + CharHandler.getCharacterByPlayerName(player.getName()).getCharacterName().split(" ")[0] + ")" + ChatColor.WHITE + "[OOC]";
				} else {
					name = player.getDisplayName();
				}
			} else {
				name = player.getDisplayName() + "[OOC]";
			}
			
		}
		
		return name + ": " + message; 
	}
	
	public static String encodeGlobalMessage(Player player, String message, Channel channel){
		// ['Color''ChannelName']<'PlayerColor''PlayerName'> 'Message'
		String prefix;
		String finalString;
		String playerColor = "f";
		prefix = ExtensionManager.permissions.getGroupPrefix(ExtensionManager.permissions.getGroup(player.getName()));
		// TODO: Playercolor, from Characterizationing plugin.
		
		finalString =
			"§" + channel.getColor()
			+ "[" + channel.getName() + "]"
			+ "§" + playerColor + "<" + "§" + prefix + player.getDisplayName() + "§f> "
			+ message;
		
		// Final String
		
		return finalString;
	}
	
	public static boolean playerCanTalk(Player player, NaviaChat instance){
		if (ExtensionManager.permissions.has(player, "naviachat.chat.cantalk")){
			return true;
		} else {
			player.sendMessage(ChatColor.RED + "You are muted by an administrator.");
			return false;
		}
	}
	
	public static String getIcEmote(Player sender, String action){
		String name;
		
		if (ExtensionManager.isUsingNaviaChar){
			if (CharHandler.playerHasACharacter(sender)){
				name = CharHandler.getCharacterByPlayerName(sender.getName()).getCharacterName().split(" ")[0];
			} else {
				name = sender.getDisplayName();
			}
		} else {
			name = sender.getDisplayName();
		}
		
		String message = ChatColor.AQUA
			+ "* "
			+ name
			+ " "
			+ ChatColor.WHITE
			+ action;
		
		return message;
	}
	
	public static ArrayList<String> breakMessage(String message){
		ArrayList<String> lines = new ArrayList<String>();
		
		if (message.length() < LINEBREAK){
			lines.add(message);
			return lines;
		}
			
		String[] split = message.split(" ");
		
		String curLine = "";
		for (int i = 0; i < split.length; i++){
			String s = split[i];
			if (curLine.length() < LINEBREAK){
				curLine += s + " ";
			} else {
				lines.add(curLine);
				curLine = "";
			}
		}
		
		return lines;
		
	}
}
