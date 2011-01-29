package org.navia.mcandze.chatting;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * This class represents a Channel.
 * @author Andreas
 *
 */
public class Channel {
	public static Chatting plugin;
	private int range;
	private String name;
	private String sCut;
	private String color;
	private boolean ic;
	private boolean joinOnLogin;
	private boolean focusOnDefault;
	
	public Channel(Chatting plugin, int range, String name, String sCut, String color, boolean ic, boolean joinOnLogin, boolean focusOnDefault){
		this.plugin = plugin;
		this.range = range;
		this.name = name;
		this.sCut = sCut;
		this.color = color;
		this.ic = ic;
		this.joinOnLogin = joinOnLogin;
		this.focusOnDefault = focusOnDefault;
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
		if (plugin.isUsingPermissions()){
			if (!new MessageFormatting().playerCanTalk(sender, plugin)){
				return;
			}
		}
		if (this.isLocal()){
			for (Player p: plugin.getServer().getOnlinePlayers()){
				if (isInDistance(p, sender.getLocation()) && plugin.getChannelManager().playerIsInChannel(p, this)){
					sendLocally(message, sender, p, ic);
				}
			}
		} else {
			if (plugin.getChannelManager().playerIsInChannel(sender, this)){
				sendGlobally(message, sender);
			}
			
		}
	}
	
	public void sendLocally(String message, Player sender, Player receiver, boolean ic){
		String newMessage = (new MessageFormatting()).encodeLocalMessage(sender, plugin, message, this, ic);
		receiver.sendMessage(newMessage);
	}
	
	public void sendGlobally(String message, Player sender){
		for (Player p: plugin.getServer().getOnlinePlayers()){
			p.sendMessage(new MessageFormatting().encodeGlobalMessage(sender, plugin, message, this));
		}
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
	 * @return the plugin
	 */
	public static Chatting getPlugin() {
		return plugin;
	}

	/**
	 * @param plugin the plugin to set
	 */
	public static void setPlugin(Chatting plugin) {
		Channel.plugin = plugin;
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
}
