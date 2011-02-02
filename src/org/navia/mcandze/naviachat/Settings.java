package org.navia.mcandze.naviachat;

import com.nijikokun.bukkit.iProperty;

public class Settings {
	private iProperty prop = null;
	private boolean commandsOnIcOOC;
	
	public Settings(iProperty prop){
		this.prop = prop;
	}
	
	public void initialize(){
		if (prop != null){
			if (!prop.keyExists("commands-for-toggling-rp-state")){
				prop.setBoolean("commands-for-toggling-rp-state", true);
				commandsOnIcOOC = true;
			} else {
				commandsOnIcOOC = prop.getBoolean("commands-for-toggling-rp-state");
			}
		}
	}
	
	public boolean isUsingCommands(){
		return commandsOnIcOOC;
	}
	
	public void setUsingCommands(boolean cmds){
		this.commandsOnIcOOC = cmds;
		prop.setBoolean("commands-for-toggling-rp-state", cmds);
	}
}
