package org.navia.mcandze.naviachat;

import java.util.ArrayList;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.nijikokun.bukkit.iProperty;

/**
 * This class represents a Channel.
 * @author Andreas
 *
 */
public class Channel {
	// Indexes are temporarily disabled. Focusing on flatfiles, and only SQL uses the index.
	
	private NaviaChat plugin;
	private ArrayList<Player> banned;
	//private int index;
	private int range;
	private String name;
	private String sCut;
	private String color;
	private boolean ic;
	private boolean hidden;
	private boolean joinOnLogin;
	private boolean focusOnDefault;
	private iProperty properties;
	
	//public static int newIndex = 1;
	
	public Channel(NaviaChat instance, iProperty properties){
		this.properties = properties;
		this.plugin = instance;
	}
	
	/*public Channel(NaviaChat plugin, int range, String name, String sCut, String color, boolean ic, boolean joinOnLogin, boolean focusOnDefault){
		this.plugin = plugin;
		//this.index = newIndex;
		//newIndex++;
		this.range = range;
		this.name = name;
		this.sCut = sCut;
		this.color = color;
		this.ic = ic;
		this.joinOnLogin = joinOnLogin;
		this.focusOnDefault = focusOnDefault;
		banned = new ArrayList<Player>();
		hidden = false;
		if (index > newIndex){
			newIndex = index;
		}
		newIndex++;
	}*/
	
	/*public Channel(NaviaChat plugin, int range, String name, String sCut, String color, boolean ic, boolean joinOnLogin, boolean focusOnDefault, ArrayList<Player> banned){
		this.plugin = plugin;
		//this.index = newIndex;
		//newIndex++;
		this.range = range;
		this.name = name;
		this.sCut = sCut;
		this.color = color;
		this.ic = ic;
		this.joinOnLogin = joinOnLogin;
		this.focusOnDefault = focusOnDefault;
		this.banned = banned;
		hidden = false;
		if (index > newIndex){
			newIndex = index;
		}
		newIndex++;
	}*/
	
	public void initialize(){
		this.name = properties.getString("name-of-channel");
		this.sCut = properties.getString("shortcut-name");
		this.range = properties.getInt("range-in-blocks");
		this.color = properties.getString("color-of-chat");
		this.ic = properties.getBoolean("is-in-character-focused");
		this.joinOnLogin = properties.getBoolean("join-on-login");
		this.focusOnDefault = properties.getBoolean("focus-on-default");
		this.banned = new ArrayList<Player>(); 
		if (properties.keyExists("banned-players")){
			String unFormatted = properties.getString("banned-players");
			StringTokenizer strTok = new StringTokenizer(unFormatted, ",");
			while (strTok.hasMoreTokens()){
				banned.add(plugin.getServer().getPlayer(strTok.nextToken()));
			}
		}
	}
	
	public void banPlayer(Player player){
		banned.add(player);
		if (!properties.keyExists("banned-players")){
			properties.setString("banned-players", player.getName());
		} else {
			String tmpr = properties.getString("banned-players");
			properties.setString("banned-players", tmpr + "," + player.getName());
		}
	}
	
	public boolean unbanPlayer(Player player){
		if (banned.contains(player)){
			banned.remove(player);
			
			return true;
		}
		return false;
	}
	
	public boolean isPlayerBanned(Player player){
		if (banned.contains(player)){
			return true;
		}
		return false;
	}
	
	public boolean isHidden(){
		return hidden;
	}
	
	public boolean isDefaultOnLogin(){
		return joinOnLogin;
	}
	
	public boolean isFocusedOnDefault(){
		return focusOnDefault;
	}
	
	public boolean isIc(){
		return ic;
	}
	
	public String getShortCut(){
		return this.sCut;
	}
	
	public boolean isLocal(){
		return range != 0;
	}
	
	public void sendMessage(String message, Player sender, boolean ic){
		Logger log = Logger.getLogger("Minecraft");
		if (this.isLocal()){
			String newMessage=MessageFormatting.encodeLocalMessage(sender, plugin, message, this, ic);
			log.info("[NaviaChat] " + newMessage);
			int anyone = 0;
			for (Player p: plugin.getServer().getOnlinePlayers()){
				if (isInDistance(p, sender.getLocation()) && plugin.getChManager().playerIsInChannel(p, this) && !isPlayerBanned(p)){
					p.sendMessage(newMessage);
					anyone++;
				}
			}
			if (anyone == 0){
				noOneIsNear(sender);
			}
		} else {
			if (plugin.getChManager().playerIsInChannel(sender, this)){
				String newMessage=MessageFormatting.encodeGlobalMessage(sender, plugin, message, this);
				log.info("[NaviaChat]" + newMessage);
				for (Player p: plugin.getServer().getOnlinePlayers()){
					if (!isPlayerBanned(p)){
						p.sendMessage(newMessage);
					}
				}
			}
			
		}
	}
	
	public void noOneIsNear(Player p){
		String[] messages = {"You look around, but can't see anyone in sight.", "No one is around to hear you", "Your voice gets lost among the natural ambience."};
		Random test = new Random();
		p.sendMessage(ChatColor.GREEN + messages[test.nextInt(messages.length)]);
	}

	public boolean isInDistance(Player receiver, Location sender){
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

	/**
	 * @return the range
	 */
	public int getRange() {
		return range;
	}

	/**
	 * @param range the range to set
	 */
	public void setRange(int range) {
		this.range = range;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the sCut
	 */
	public String getsCut() {
		return sCut;
	}

	/**
	 * @param sCut the sCut to set
	 */
	public void setsCut(String sCut) {
		this.sCut = sCut;
	}

	/**
	 * @return the color
	 */
	public String getColor() {
		return color;
	}

	/**
	 * @param color the color to set
	 */
	public void setColor(String color) {
		this.color = color;
	}

	/**
	 * @return the joinOnLogin
	 */
	public boolean isJoinOnLogin() {
		return joinOnLogin;
	}

	/**
	 * @param joinOnLogin the joinOnLogin to set
	 */
	public void setJoinOnLogin(boolean joinOnLogin) {
		this.joinOnLogin = joinOnLogin;
	}

	/**
	 * @return the focusOnDefault
	 */
	public boolean isFocusOnDefault() {
		return focusOnDefault;
	}

	/**
	 * @param focusOnDefault the focusOnDefault to set
	 */
	public void setFocusOnDefault(boolean focusOnDefault) {
		this.focusOnDefault = focusOnDefault;
	}

	/**
	 * @param ic the ic to set
	 */
	public void setIc(boolean ic) {
		this.ic = ic;
	}
	
	/**
	 * @return the index
	 */
	/*public int getIndex() {
		return index;
	}*/

	/**
	 * @param index the index to set
	 */
	/*public void setIndex(int index) {
		this.index = index;
	}*/
}
