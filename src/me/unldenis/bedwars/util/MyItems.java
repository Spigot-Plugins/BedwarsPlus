package me.unldenis.bedwars.util;

import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.Color;
import org.bukkit.Material;
import me.unldenis.bedwars.object.teams.TeamType;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.ArrayList;
import org.bukkit.inventory.ItemStack;

public class MyItems
{
    public ItemStack nameItem(final ItemStack item, final String name, final String lore) {
        final ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        final ArrayList<String> l = new ArrayList<String>();
        l.add(lore);
        meta.setLore(l);
        item.setItemMeta(meta);
        return item;
    }
    
    public Material materialFromTeam(final TeamType type) {
        switch (type) {
            case Red: {
                return Material.RED_WOOL;
            }
            case Blue: {
                return Material.BLUE_WOOL;
            }
            case Green: {
                return Material.GREEN_WOOL;
            }
            case Yellow: {
                return Material.YELLOW_WOOL;
            }
            case White: {
                return Material.WHITE_WOOL;
            }
            case Black: {
                return Material.BLACK_WOOL;
            }
            case Pink: {
                return Material.PINK_WOOL;
            }
            case Cyan: {
                return Material.CYAN_WOOL;
            }
            default: {
                return null;
            }
        }
    }
    
    public Color colorFromString(String color) {
        color = color.toLowerCase();
        switch (color) {
            case "yellow": {
                return Color.YELLOW;
            }
            case "red": {
                return Color.RED;
            }
            case "blue": {
                return Color.BLUE;
            }
            case "cyan": {
                return Color.AQUA;
            }
            case "pink": {
                return Color.PURPLE;
            }
            case "black": {
                return Color.BLACK;
            }
            case "green": {
                return Color.GREEN;
            }
            case "white": {
                return Color.WHITE;
            }
            default:
                break;
        }
        return null;
    }
    
    @SuppressWarnings("deprecation")
	public ItemStack getHead(final String player) {
        final ItemStack item = new ItemStack(Material.LEGACY_SKULL_ITEM, 1, (short)3);
        final SkullMeta meta = (SkullMeta)item.getItemMeta();
        meta.setOwner(player);
        item.setItemMeta((ItemMeta)meta);
        return item;
    }
}
