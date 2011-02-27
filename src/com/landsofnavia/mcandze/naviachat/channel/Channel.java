package com.landsofnavia.mcandze.naviachat.channel;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.World;

import com.landsofnavia.mcandze.naviachat.NaviaChat;
import com.landsofnavia.mcandze.naviachat.Settings;
import com.landsofnavia.mcandze.naviachat.chmarkup.ChatColor;

/**
 * This class represents a Channel.
 * @author Andreas
 *
 */
public class Channel {
	
	private List<World> worlds;
	private List<String> banned;
	private List<String> muted;
	private String name;
	private String shortName;
	private String shortCut;
	private ChatColor color;
	private List<String> allowedGroups;
	private List<String> allowedPlayers;
	private boolean ic;
	private boolean hidden;
	
	
	
	public Channel(String name) {
		this.name = name;
		
		this.worlds = new ArrayList<World>();
		this.banned = new ArrayList<String>();
		this.allowedGroups = new ArrayList<String>();
		this.allowedPlayers = new ArrayList<String>();
	}

	public void loadFromConfig(){
		// Worlds
		List<World> worlds = new ArrayList<World>();
		for (String s: Settings.channelsConfig.getKeys(this.getName() + ".worlds")){
			World world = NaviaChat.server.getWorld(s);
			if (s != null && !s.isEmpty()){
				worlds.add(world);
			}
		}
		if (worlds.isEmpty()){
			worlds.add(NaviaChat.server.getWorlds().get(0));
		}
		this.setWorlds(worlds);
		
		// Banned players
		List<String> banned = new ArrayList<String>();
		for (String s: Settings.channelsConfig.getKeys(this.getName() + ".banned-players")){
			banned.add(s);
		}
		this.setMuted(muted);
		
		// Muted players
		List<String> muted = new ArrayList<String>();
		for (String s: Settings.channelsConfig.getKeys(this.getName() + ".muted-players")){
			muted.add(s);
		}
		this.setBanned(banned);
		
		// ShortName
		this.setShortName(Settings.channelsConfig.getString(this.getName() + ".short-name"));
		
		// Shortcut
		this.setShortCut(Settings.channelsConfig.getString(this.getName() + ".shortcut"));
		
		// Color
		try {
			this.setColor(ChatColor.valueOf(Settings.channelsConfig.getString(this.getName() + ".color").toUpperCase()));
		} catch (Exception e){
			this.setColor(ChatColor.WHITE);
		}
		
		// Allowed groups
		List<String> groups = new ArrayList<String>();
		for (String s: Settings.channelsConfig.getKeys(this.getName() + ".allowed-groups")){
			groups.add(s);
		}
		this.setAllowedGroups(groups);
		
		// Allowed players
		List<String> players = new ArrayList<String>();
		for (String s: Settings.channelsConfig.getKeys(this.getName() + ".exempted-players")){
			players.add(s);
		}
		this.setAllowedPlayers(players);
		
		// In Character
		this.setIc(Settings.channelsConfig.getBoolean(this.getName() + ".in-character-focused", false));
		
		// Hidden
		this.setHidden(Settings.channelsConfig.getBoolean(this.getName() + ".hidden", false));
	}
	
	public void sendMessage(){
		
	}
	
	public List<String> getMuted() {
		return muted;
	}

	public void setMuted(List<String> muted) {
		this.muted = muted;
	}

	public List<String> getAllowedGroups() {
		return allowedGroups;
	}

	public void setAllowedGroups(List<String> allowedGroups) {
		this.allowedGroups = allowedGroups;
	}

	public List<String> getAllowedPlayers() {
		return allowedPlayers;
	}

	public void setAllowedPlayers(List<String> allowedPlayers) {
		this.allowedPlayers = allowedPlayers;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public List<World> getWorlds() {
		return worlds;
	}

	public void setWorlds(List<World> worlds) {
		this.worlds = worlds;
	}

	public List<String> getBanned() {
		return banned;
	}

	public void setBanned(List<String> banned) {
		this.banned = banned;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShortCut() {
		return shortCut;
	}

	public void setShortCut(String shortCut) {
		this.shortCut = shortCut;
	}

	public ChatColor getColor() {
		return color;
	}

	public void setColor(ChatColor color) {
		this.color = color;
	}

	public boolean isIc() {
		return ic;
	}

	public void setIc(boolean ic) {
		this.ic = ic;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
	
	
	
//	public void initialize(){
//		this.name = properties.getString("name-of-channel");
//		this.sCut = properties.getString("shortcut-name");
//		this.range = properties.getInt("range-in-blocks");
//		this.color = properties.getString("color-of-chat");
//		this.ic = properties.getBoolean("is-in-character-focused");
//		this.banned = new ArrayList<Player>(); 
//		if (properties.keyExists("banned-players")){
//			String unFormatted = properties.getString("banned-players");
//			StringTokenizer strTok = new StringTokenizer(unFormatted, ",");
//			while (strTok.hasMoreTokens()){
//				banned.add(NaviaChat.server.getPlayer(strTok.nextToken()));
//			}
//		}
//		this.worlds = new ArrayList<World>();
//		if (properties.getString("worlds").contains(",")){
//			StringTokenizer strTok = new StringTokenizer(properties.getString("worlds"), ",");
//			while (strTok.hasMoreTokens()){
//				World world = NaviaChat.server.getWorld(strTok.nextToken());
//				worlds.add(world);
//			}
//		} else {
//			worlds.add(NaviaChat
//					.server
//					.getWorld("world"));
//		}
//	}
//	
//	public void banPlayer(Player player){
//		banned.add(player);
//		if (!properties.keyExists("banned-players")){
//			properties.setString("banned-players", player.getName());
//		} else {
//			String tmpr = properties.getString("banned-players");
//			properties.setString("banned-players", tmpr + "," + player.getName());
//		}
//	}
//	
//	public boolean unbanPlayer(Player player){
//		if (banned.contains(player)){
//			banned.remove(player);
//			
//			return true;
//		}
//		return false;
//	}
//	
//	public boolean isPlayerBanned(Player player){
//		if (banned.contains(player)){
//			return true;
//		}
//		return false;
//	}
//	
//	public boolean isHidden(){
//		return hidden;
//	}
//	
//	public boolean isIc(){
//		return ic;
//	}
//	
//	public String getShortCut(){
//		return this.sCut;
//	}
//	
//	public boolean isLocal(){
//		return range != 0;
//	}
//	
//	public void sendMe(String action, Player sender){
//		Logger log = Logger.getLogger("Minecraft");
//		if (this.isLocal()){
//			if (this.isIc() && ChannelManager.playerIsIc(sender)){
//				String emote = MessageHandler.getIcEmote(sender, action);
//				log.info("[NaviaChat] [" + this.name + "] [EMOTE] " + emote);
//				for (Player p: NaviaChat.server.getOnlinePlayers()){
//					if (isInDistance(p, sender.getLocation()) && !isPlayerBanned(p)){
//						p.sendMessage(emote);
//					}
//				}
//			} else {
//				
//			}
//		} else {
//			
//		}
//	}
//	
//	public void sendMessage(String message, Player sender, boolean ic){
//		Logger log = Logger.getLogger("Minecraft");
//		if (this.isLocal()){
//			String newMessage=MessageHandler.getLocalMessage(sender, message, this, ic);
//			log.info("[NaviaChat] " + newMessage);
//			int anyone = 0;
//			for (Player p: NaviaChat.server.getOnlinePlayers()){
//				if (isInDistance(p, sender.getLocation()) && ChannelManager.playerIsInChannel(p, this) && !isPlayerBanned(p) && isInWorld(p)){
//					p.sendMessage(newMessage);
//					anyone++;
//				}
//			}
//			if (anyone == 1){
//				noOneIsNear(sender);
//			}
//		} else {
//			if (ChannelManager.playerIsInChannel(sender, this)){
//				String newMessage=MessageHandler.encodeGlobalMessage(sender, message, this);
//				log.info("[NaviaChat]" + newMessage);
//				for (Player p: NaviaChat.server.getOnlinePlayers()){
//					if (!isPlayerBanned(p) && ChannelManager.playerIsInChannel(p, this) && isInWorld(p)){
//						p.sendMessage(newMessage);
//					}
//				}
//			}
//			
//		}
//	}
//	
//	public boolean isInWorld(Player p){
//		for (World w: worlds){
//			if (p.getWorld().equals(w)){
//				return true;
//			}
//		}
//		return false;
//	}
//	
//	public static ArrayList<String> messages = new ArrayList<String>();
//	
//	public static void noOneIsNear(Player p){
//		Random test = new Random();
//		if (messages.isEmpty()){
//			p.sendMessage(ChatColor.GREEN + "No one can hear you.");
//			return;
//		}
//		p.sendMessage(ChatColor.GREEN + messages.get(test.nextInt(messages.size())));
//	}
//
//	public boolean isInDistance(Player receiver, Location sender){
//		double xP = 
//			Math.pow(sender.getX() - receiver.getLocation().getX(), 2);
//		double yP = 
//			Math.pow(sender.getY() - receiver.getLocation().getY(), 2);
//		double zP = 
//			Math.pow(sender.getZ() - receiver.getLocation().getZ(), 2);
//		if (Math.sqrt(xP + yP + zP) <= range){
//			return true;
//		}
//		return false;
//	}
//
//	/**
//	 * @return the range
//	 */
//	public int getRange() {
//		return range;
//	}
//
//	/**
//	 * @param range the range to set
//	 */
//	public void setRange(int range) {
//		this.range = range;
//	}
//
//	/**
//	 * @return the name
//	 */
//	public String getName() {
//		return name;
//	}
//
//	/**
//	 * @param name the name to set
//	 */
//	public void setName(String name) {
//		this.name = name;
//	}
//
//	/**
//	 * @return the sCut
//	 */
//	public String getsCut() {
//		return sCut;
//	}
//
//	/**
//	 * @param sCut the sCut to set
//	 */
//	public void setsCut(String sCut) {
//		this.sCut = sCut;
//	}
//
//	/**
//	 * @return the color
//	 */
//	public String getColor() {
//		return color;
//	}
//
//	/**
//	 * @param color the color to set
//	 */
//	public void setColor(String color) {
//		this.color = color;
//	}
//
//	/**
//	 * @param ic the ic to set
//	 */
//	public void setIc(boolean ic) {
//		this.ic = ic;
//	}
//	/**
//	 * @return the world
//	 */
//	public ArrayList<World> getWorlds() {
//		return worlds;
//	}
//
//	/**
//	 * @param world the world to set
//	 */
//	public void setWorlds(ArrayList<World> worlds) {
//		this.worlds = worlds;
//	}
}
