package me.unldenis.bedwars.util;

import me.unldenis.bedwars.object.teams.TeamType;
import org.bukkit.ChatColor;

public class MyChat
{
    public String format(String string) {
        string = "&9Bedwars &7>> " + string;
        return ChatColor.translateAlternateColorCodes('&', string);
    }
    
    public String prefix(final TeamType type) {
        switch (type) {
            case Red: {
                return "§c";
            }
            case Blue: {
                return "§9";
            }
            case Green: {
                return "§a";
            }
            case Yellow: {
                return "§e";
            }
            case White: {
                return "§f";
            }
            case Black: {
                return "§0";
            }
            case Pink: {
                return "§d";
            }
            case Cyan: {
                return "§b";
            }
            default: {
                return null;
            }
        }
    }
    
    public String getColoredText(final String text) {
        return text.replace("&", "§");
    }
    
    public String getStrippedText(String text) {
        final String[] split = text.split("");
        for (int x = 0; x < split.length; ++x) {
            if (split[x].equals("§") && !split[x + 1].equals(" ")) {
                text = text.replace(String.valueOf(split[x]) + split[x + 1], "");
            }
        }
        return text;
    }
    
    public ChatColor colorFromString(String color) {
        color = color.toLowerCase();
        switch (color) {
            case "yellow": {
                return ChatColor.YELLOW;
            }
            case "red": {
                return ChatColor.RED;
            }
            case "blue": {
                return ChatColor.BLUE;
            }
            case "cyan": {
                return ChatColor.AQUA;
            }
            case "pink": {
                return ChatColor.LIGHT_PURPLE;
            }
            case "black": {
                return ChatColor.BLACK;
            }
            case "green": {
                return ChatColor.GREEN;
            }
            case "white": {
                return ChatColor.WHITE;
            }
            default:
                break;
        }
        return null;
    }
}
