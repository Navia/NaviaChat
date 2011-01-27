package org.navia.mcandze.chatting;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.nijikokun.bukkit.iProperty;

public class ChattingDataSource {
	public static Chatting plugin;
	public final static String CHANNELS_DATABASE = "jdbc:sqlite:channels.db";
	
	public final static String CHANNELS_TABLE = "CREATE TABLE `channelsTable` ("
		+ "`range` tinyint NOT NULL DEFAULT '0',"
		+ "`name` varchar(32) NOT NULL DEFAULT '',"
		+ "`shortcut` varchar(32) NOT NULL DEFAULT '',"
		+ "`color` tinyint NOT NULL DEFAULT 'F',"
		+ "`ic` boolean NOT NULL DEFAULT '0',"
		+ "`joinonlogin` boolean NOT NULL DEFAULT '0',"
		+ "`focusondefault` boolean NOT NULL DEFAULT '0'"
		+ ");";
	
	public ChattingDataSource(Chatting instance){
		this.plugin = instance;
	}
	
	public static void initialize(){
		if (!chTableExists()){
			createChTable();
		}
	}
	
	public static boolean chTableExists(){
		ResultSet rs = null;
		
		try {
			Connection conn = DriverManager.getConnection(CHANNELS_DATABASE);
			DatabaseMetaData dbm = conn.getMetaData();
			rs = dbm.getTables(null, null, "channelsTable", null);
			
			if (!rs.next()){
				return false;
			}
			return true;
		} catch(SQLException e){
			Logger log = Logger.getLogger("Minecraft");
			log.log(Level.SEVERE, "[Chatting] Exception while checking table.", e);
			return false;
		} finally {
			try {
				if (rs != null){
					rs.close();
				}
			} catch (SQLException e){
				Logger log = Logger.getLogger("Minecraft");
				log.log(Level.SEVERE, "[Chatting] Exception on table check close.", e);
			}
		}
	}
	
	public static void createChTable(){
		Statement st = null;
		try {
			Connection conn = DriverManager.getConnection(CHANNELS_DATABASE);
			st = conn.createStatement();
			st.executeUpdate(CHANNELS_TABLE);
			conn.commit();
		} catch (SQLException e){
			Logger log = Logger.getLogger("Minecraft");
			log.log(Level.SEVERE, "[Chatting] Exception while creating table.", e);
			
		} finally {
			try {
				if (st != null){
					st.close();
				}
			} catch (SQLException e){
				Logger log = Logger.getLogger("Minecraft");
				log.log(Level.SEVERE, "[Chatting] Could not create table. Exception on close.", e);
			}
		}
	}
	
	public static List<Channel> getChannels(){
		List<Channel> channels = new ArrayList<Channel>();
		Statement st = null;
		ResultSet set = null;
		Logger log = Logger.getLogger("Minecraft");
		try {
			Connection conn = DriverManager.getConnection(CHANNELS_DATABASE);
			
			st = conn.createStatement();
			set = st.executeQuery("SELECT * FROM channelsTable");
			int size = 0;
			while (set.next()) {
				size++;
				String name = set.getString("name");
				int range = set.getInt("range");
				String sCut = set.getString("shortcut");
				int color = set.getInt("color");
				boolean ic = set.getBoolean("ic");
				boolean joinOnLogin = set.getBoolean("joinonlogin");
				boolean focusOnDefault = set.getBoolean("focusondefault");
				Channel channel = new Channel(plugin, range, name, sCut, color, ic, joinOnLogin, focusOnDefault);
				channels.add(channel);
			}
			log.info("[Chatting] " + size + " channels loaded.");
		} catch (SQLException e){
			log.log(Level.SEVERE, "[Chatting] Exception while loading table.", e);
		} finally {
			try {
				if (st != null){
					st.close();
				}
				if (set != null){
					set.close();
				}
			} catch (SQLException e){
				log.log(Level.SEVERE, "[Chatting] Channels loading exception, while closing.", e);
			}
		}
		return channels;
	}
	
	public static List<Channel> loadNewChannels(){
		File folder = new File("Chatting" + File.pathSeparator + "Data" + File.pathSeparator + "Config" + File.pathSeparator + "NewChannels");
		File[] listOfFiles = folder.listFiles();
		Logger log = Logger.getLogger("Minecraft");
		List<Channel> channels = new ArrayList<Channel>();
		
		int index = 0;
		
		for (File f: listOfFiles){
			if (canConvertPropertyToChannel(f.getName())){
				Channel c = convertPropertyToChannel(f.getName());
				channels.add(c);
				f.delete();
			}
			
		}
		
		return channels;
	}
	
	public static boolean canConvertPropertyToChannel(String iPropertyfile){
		iProperty prop = new iProperty(iPropertyfile);
		Logger log = Logger.getLogger("Minecraft");
		if ((!prop.keyExists("name-of-channel")) 
				|| (!prop.keyExists("shortcut-name")) 
				|| (!prop.keyExists("range-in-blocks"))
				|| (!prop.keyExists("color-of-chat"))
				|| (!prop.keyExists("is-in-character-focused"))
				|| (!prop.keyExists("join-on-login"))
				|| (!prop.keyExists("focus-on-default"))){
			log.warning(Level.SEVERE + "[Chatting] Could not load " + iPropertyfile + ".properties - Invalid setup.");
			return false;
		}
		return true;
	}
	
	public static Channel convertPropertyToChannel(String iPropertyfile){
		iProperty prop = new iProperty(iPropertyfile);
		String name = prop.getString("name-of-channel");
		String sCut = prop.getString("shortcut-name");
		int range = prop.getInt("range-in-blocks");
		int color = prop.getInt("color-of-chat");
		boolean ic = prop.getBoolean("is-in-character-focused");
		boolean join = prop.getBoolean("join-on-login");
		boolean focus = prop.getBoolean("focus-on-default");
		
		Channel c = new Channel(plugin, range, name, sCut, color, ic, join, focus);
		
		return c;
	}
	
	/*public static boolean plTableExists(){
		ResultSet rs = null;
		
		try {
			Connection conn = DriverManager.getConnection(CHANNELS_DATABASE);
			DatabaseMetaData dbm = conn.getMetaData();
			rs = dbm.getTables(null, null, "playersTable", null);
			
			if (!rs.next()){
				return false;
			}
			return true;
		} catch(SQLException e){
			Logger log = Logger.getLogger("Minecraft");
			log.log(Level.SEVERE, "[Chatting] Exception while checking table.", e);
			return false;
		} finally {
			try {
				if (rs != null){
					rs.close();
				}
			} catch (SQLException e){
				Logger log = Logger.getLogger("Minecraft");
				log.log(Level.SEVERE, "[Chatting] Exception on table check close.", e);
			}
		}
	}*/
	
	/*public static void createPlTable(){
		Statement st = null;
		try {
			Connection conn = DriverManager.getConnection(CHANNELS_DATABASE);
			st = conn.createStatement();
			st.executeUpdate(PLAYER_TABLE);
			conn.commit();
		} catch (SQLException e){
			Logger log = Logger.getLogger("Minecraft");
			log.log(Level.SEVERE, "[Chatting] Exception while creating table.", e);
			
		} finally {
			try {
				if (st != null){
					st.close();
				}
			} catch (SQLException e){
				Logger log = Logger.getLogger("Minecraft");
				log.log(Level.SEVERE, "[Chatting] Could not create table. Exception on close.", e);
			}
		}
	}*/
}
