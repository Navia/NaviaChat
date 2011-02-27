package com.landsofnavia.mcandze.naviachat;

/**
 * Will hold methods for writing and loading to and from different data sources. 
 * Main focus now, is SQLite/Flatfile.
 * It will later support Flatfile-only + MySQL/Flatfile.
 * 
 * Flatfiles are used for configuration, if used with SQL.
 * @author andreas
 *
 */
public class NaviaChatDataSource {
	
//	public static List<Channel> getChannels(){
//		List<Channel> tmpr = new ArrayList<Channel>();
//		File folder = new File("plugins" + File.separator + "NaviaChat" + File.separator + "Data" + File.separator + "Config" + File.separator + "NewChannels");
//		File[] listOfFiles = folder.listFiles();
//		
//		if (listOfFiles == null){
//			return tmpr;
//		}
//		for (File f: listOfFiles){
//			if (canConvertPropertyToChannel(f.getPath())){
//				Channel c = convertPropertyToChannel(f);
//				tmpr.add(c);
//			}
//			
//		}
//		return tmpr;
//	}
//	
//	public static boolean canConvertPropertyToChannel(String iPropertyfile){
//		iProperty prop = new iProperty(iPropertyfile);
//		Logger log = Logger.getLogger("Minecraft");
//		if ((!prop.keyExists("name-of-channel")) 
//				|| (!prop.keyExists("shortcut-name")) 
//				|| (!prop.keyExists("range-in-blocks"))
//				|| (!prop.keyExists("color-of-chat"))
//				|| (!prop.keyExists("is-in-character-focused"))
//				|| (!prop.keyExists("worlds"))){
//			log.warning(Level.SEVERE + "[NaviaChat] Could not load " + iPropertyfile + ".properties - Invalid setup.");
//			return false;
//		}
//		return true;
//	}
//	
//	public static Channel convertPropertyToChannel(File iPropertyfile){
//		iProperty prop = new iProperty(iPropertyfile.getPath());
//		Channel c = new Channel(prop);
//		c.initialize();
//		return c;
//	}
//	
//	public static void loadSilenceMessages(){
//		File file;
//		if (!(file = new File(Settings.mainDirectory + Settings.settingsDirectory + "messages.txt")).exists() && !file.isDirectory()){
//			file.mkdirs();
//		}
//		try {
//			BufferedReader br = new BufferedReader(new FileReader(Settings.mainDirectory + Settings.settingsDirectory + "messages.txt"));
//			String curLine;
//			while ((curLine = br.readLine()) != null){
//				Channel.messages.add(curLine);
//			}
//		} catch (Exception e){
//			(Logger.getLogger("Minecraft")).warning("[NaviaChat] Error while loading silence messages.");
//		}
//	}
}