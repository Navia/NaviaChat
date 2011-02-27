package com.landsofnavia.mcandze.naviachat.chmarkup;

import org.bukkit.entity.Player;

import com.landsofnavia.mcandze.naviachat.channel.Channel;
import com.landsofnavia.mcandze.naviacore.permissions.PermissionChecker;

public class ChInterpreter {
	public static String interpretString(String format, final Channel channel, final Player player, final String message){
		
		if (format.contains(ChKey.CHANNEL.toString())){
			format = channelKey(format, channel);
		}
		if (format.contains(ChKey.COLOR.toString())){
			format = colorKey(format);
		}
		if (format.contains(ChKey.PLAYER.toString())){
			format = playerKey(format, player);
		}
		format.replaceAll("{Message}", message);
		
		return format;
	}
	
	public static String channelKey(String format, final Channel channel){
		format.replaceAll("{" + ChKey.CHANNEL.toString() + "." + ChannelArgs.COLOR.toString(), channel.getColor().getStrPresentation());
		format.replaceAll("{" + ChKey.CHANNEL.toString() + "." + ChannelArgs.NAME.toString(), channel.getName());
		format.replaceAll("{" + ChKey.CHANNEL.toString() + "." + ChannelArgs.SHORTCUT.toString(), channel.getShortCut());
		format.replaceAll("{" + ChKey.CHANNEL.toString() + "." + ChannelArgs.SHORTNAME, channel.getShortCut());
		return format;
	}
	
	public static String colorKey(String format){
		format.replaceAll("{" + ChKey.COLOR.toString() + "." + ChatColor.AQUA.toString() + "}", ChatColor.AQUA.getStrPresentation());
		format.replaceAll("{" + ChKey.COLOR.toString() + "." + ChatColor.BLACK.toString() + "}", ChatColor.BLACK.getStrPresentation());
		format.replaceAll("{" + ChKey.COLOR.toString() + "." + ChatColor.BLUE.toString() + "}", ChatColor.BLUE.getStrPresentation());
		format.replaceAll("{" + ChKey.COLOR.toString() + "." + ChatColor.DARK_AQUA.toString() + "}", ChatColor.DARK_AQUA.getStrPresentation());
		format.replaceAll("{" + ChKey.COLOR.toString() + "." + ChatColor.DARK_BLUE.toString() + "}", ChatColor.DARK_BLUE.getStrPresentation());
		format.replaceAll("{" + ChKey.COLOR.toString() + "." + ChatColor.DARK_GRAY.toString() + "}", ChatColor.DARK_GRAY.getStrPresentation());
		format.replaceAll("{" + ChKey.COLOR.toString() + "." + ChatColor.DARK_GREEN.toString() + "}", ChatColor.DARK_GREEN.getStrPresentation());
		format.replaceAll("{" + ChKey.COLOR.toString() + "." + ChatColor.DARK_PURPLE.toString() + "}", ChatColor.DARK_PURPLE.getStrPresentation());
		format.replaceAll("{" + ChKey.COLOR.toString() + "." + ChatColor.DARK_RED.toString() + "}", ChatColor.DARK_RED.getStrPresentation());
		format.replaceAll("{" + ChKey.COLOR.toString() + "." + ChatColor.GOLD.toString() + "}", ChatColor.GOLD.getStrPresentation());
		format.replaceAll("{" + ChKey.COLOR.toString() + "." + ChatColor.GRAY.toString() + "}", ChatColor.GRAY.getStrPresentation());
		format.replaceAll("{" + ChKey.COLOR.toString() + "." + ChatColor.GREEN.toString() + "}", ChatColor.GREEN.getStrPresentation());
		format.replaceAll("{" + ChKey.COLOR.toString() + "." + ChatColor.LIGHT_PURPLE.toString() + "}", ChatColor.LIGHT_PURPLE.getStrPresentation());
		format.replaceAll("{" + ChKey.COLOR.toString() + "." + ChatColor.RED.toString() + "}", ChatColor.RED.getStrPresentation());
		format.replaceAll("{" + ChKey.COLOR.toString() + "." + ChatColor.WHITE.toString() + "}", ChatColor.WHITE.getStrPresentation());
		format.replaceAll("{" + ChKey.COLOR.toString() + "." + ChatColor.YELLOW.toString() + "}", ChatColor.YELLOW.getStrPresentation());
		return format;
	}
	
	public static String playerKey(String format, Player player){
		format.replaceAll("{" + ChKey.PLAYER.toString() + "." + PlayerArgs.GROUP.toString(), PermissionChecker.getGroup(player));
		format.replaceAll("{" + ChKey.PLAYER.toString() + "." + PlayerArgs.NAME.toString(), player.getName());
		format.replaceAll("{" + ChKey.PLAYER.toString() + "." + PlayerArgs.PREFIX.toString(), PermissionChecker.getPrefix(player));
		return format;
	}
}
