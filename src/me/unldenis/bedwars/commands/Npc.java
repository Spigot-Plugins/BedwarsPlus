package me.unldenis.bedwars.commands;

import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.Bukkit;
import java.util.Arrays;
import me.unldenis.bedwars.npc.NPC;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import me.unldenis.bedwars.util.MyItems;
import me.unldenis.bedwars.Bedwars;
import org.bukkit.command.CommandExecutor;

public class Npc implements CommandExecutor
{
    private Bedwars main;
    private MyItems myItm;
    
    public Npc(final Bedwars main) {
        this.main = main;
        this.myItm = new MyItems();
    }
    
    @SuppressWarnings("deprecation")
	public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use that command!");
            return true;
        }
        final Player player = (Player)sender;
        if (cmd.getName().equalsIgnoreCase("bwextra")) {
            if (!player.hasPermission("bwnpc.admin")) {
                player.sendMessage(ChatColor.RED + "You are not authorized to use this command");
                return true;
            }
            if (args.length == 4) {
                if (args[0].equalsIgnoreCase("npc")) {
                    if (!args[1].equalsIgnoreCase("add")) {
                        this.help(player);
                        return true;
                    }
                    if (args[2].equalsIgnoreCase("random") || args[2].equalsIgnoreCase("solo") || args[2].equalsIgnoreCase("solo") || args[2].equalsIgnoreCase("squad")) {
                        final int savedNpcs = this.main.getNpcsData().getConfig().getInt("amount");
                        this.main.getNpcsData().getConfig().set("amount", (Object)(savedNpcs + 1));
                        this.main.getNpcsData().getConfig().set("npcs." + (savedNpcs + 1) + ".location", (Object)player.getLocation());
                        this.main.getNpcsData().getConfig().set("npcs." + (savedNpcs + 1) + ".type", (Object)args[2].toLowerCase());
                        this.main.getNpcsData().getConfig().set("npcs." + (savedNpcs + 1) + ".skin", (Object)args[3]);
                        this.main.getNpcsData().saveConfig();
                        player.sendMessage(ChatColor.GREEN + "NPC of type " + args[2].toLowerCase() + " n°" + (savedNpcs + 1) + " added successfully");
                        final NPC npc = new NPC(this.main, String.valueOf(args[2].toLowerCase().substring(0, 1).toUpperCase()) + args[2].toLowerCase().substring(1), player.getLocation(), args[3]);
                        this.main.getHoloManager().createHologram(new StringBuilder().append(ChatColor.YELLOW).append(ChatColor.BOLD).append("CLICK TO PLAY").toString(), Arrays.asList("[" + args[2].toLowerCase().substring(0, 1).toUpperCase() + args[2].toLowerCase().substring(1) + "]", new StringBuilder().append(ChatColor.YELLOW).append(ChatColor.BOLD).append("0 Players").toString()), player.getLocation().add(0.0, 0.28, 0.0));
                        if (!Bukkit.getOnlinePlayers().isEmpty()) {
                            for (final Player p : Bukkit.getOnlinePlayers()) {
                                if (npc.hasSkin()) {
                                    npc.addNPCPacket(p);
                                }
                                else {
                                    npc.sendSetNPCSkinPacket(p);
                                }
                            }
                        }
                        return true;
                    }
                    this.help(player);
                    return true;
                }
            }
            else if (args.length == 3) {
                if (args[0].equalsIgnoreCase("npc")) {
                    if (args[1].equalsIgnoreCase("add")) {
                        if (args[2].equalsIgnoreCase("random") || args[2].equalsIgnoreCase("solo") || args[2].equalsIgnoreCase("duo") || args[2].equalsIgnoreCase("squad")) {
                            final int savedNpcs = this.main.getNpcsData().getConfig().getInt("amount");
                            this.main.getNpcsData().getConfig().set("amount", (Object)(savedNpcs + 1));
                            this.main.getNpcsData().getConfig().set("npcs." + (savedNpcs + 1) + ".location", (Object)player.getLocation());
                            this.main.getNpcsData().getConfig().set("npcs." + (savedNpcs + 1) + ".type", (Object)args[2].toLowerCase());
                            this.main.getNpcsData().getConfig().set("npcs." + (savedNpcs + 1) + ".skin", (Object)null);
                            this.main.getNpcsData().saveConfig();
                            player.sendMessage(ChatColor.GREEN + "NPC of type " + args[2].toLowerCase() + " n°" + (savedNpcs + 1) + " added successfully");
                            final NPC npc = new NPC(this.main, String.valueOf(args[2].toLowerCase().substring(0, 1).toUpperCase()) + args[2].toLowerCase().substring(1), player.getLocation(), null);
                            this.main.getHoloManager().createHologram(new StringBuilder().append(ChatColor.YELLOW).append(ChatColor.BOLD).append("CLICK TO PLAY").toString(), Arrays.asList("[" + args[2].toLowerCase().substring(0, 1).toUpperCase() + args[2].toLowerCase().substring(1) + "]", new StringBuilder().append(ChatColor.YELLOW).append(ChatColor.BOLD).append("0 Players").toString()), player.getLocation().add(0.0, 0.28, 0.0));
                            if (!Bukkit.getOnlinePlayers().isEmpty()) {
                                for (final Player p : Bukkit.getOnlinePlayers()) {
                                    if (npc.hasSkin()) {
                                        npc.addNPCPacket(p);
                                    }
                                    else {
                                        npc.sendSetNPCSkinPacket(p);
                                    }
                                }
                            }
                            return true;
                        }
                        this.help(player);
                        return true;
                    }
                    else {
                        if (!args[1].equalsIgnoreCase("addteam")) {
                            this.help(player);
                            return true;
                        }
                        final String color = args[2];
                        final ArmorStand stand = (ArmorStand)player.getLocation().getWorld().spawn(player.getLocation(), ArmorStand.class);
                        stand.setCustomName(new StringBuilder().append(this.myItm.colorFromString(color)).append(ChatColor.ITALIC).append(this.capitalize(color)).toString());
                        stand.setCustomNameVisible(true);
                        stand.setArms(true);
                        final ItemStack l1 = new ItemStack(Material.LEATHER_HELMET, 1);
                        final LeatherArmorMeta lch = (LeatherArmorMeta)l1.getItemMeta();
                        lch.setColor(this.myItm.colorFromString(color));
                        l1.setItemMeta((ItemMeta)lch);
                        stand.setHelmet(l1);
                        final ItemStack l2 = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
                        final LeatherArmorMeta lch2 = (LeatherArmorMeta)l2.getItemMeta();
                        lch2.setColor(this.myItm.colorFromString(color));
                        l2.setItemMeta((ItemMeta)lch2);
                        stand.setChestplate(l2);
                        final ItemStack l3 = new ItemStack(Material.LEATHER_LEGGINGS, 1);
                        final LeatherArmorMeta lch3 = (LeatherArmorMeta)l3.getItemMeta();
                        lch3.setColor(this.myItm.colorFromString(color));
                        l3.setItemMeta((ItemMeta)lch3);
                        stand.setLeggings(l3);
                        final ItemStack l4 = new ItemStack(Material.LEATHER_BOOTS, 1);
                        final LeatherArmorMeta lch4 = (LeatherArmorMeta)l4.getItemMeta();
                        lch4.setColor(this.myItm.colorFromString(color));
                        l4.setItemMeta((ItemMeta)lch4);
                        stand.setBoots(l4);
                    }
                }
            }
            else if (args.length == 2 && args[0].equalsIgnoreCase("npc")) {
                if (args[1].equalsIgnoreCase("help")) {
                    this.help(player);
                    return true;
                }
                if (args[1].equalsIgnoreCase("clear")) {
                    this.main.getNpcsData().getConfig().set("amount", (Object)0);
                    this.main.getNpcsData().getConfig().set("npcs", (Object)null);
                    this.main.getNpcsData().saveConfig();
                    player.sendMessage(ChatColor.GREEN + "NPCs removed successfully");
                    this.main.getNPCsManager().removeNPCs();
                    return true;
                }
            }
        }
        player.sendMessage(ChatColor.RED + "/bwextra npc help");
        return true;
    }
    
    private void help(final Player player) {
        player.sendMessage(ChatColor.GOLD + "Bedwars NPC's Help");
        player.sendMessage(ChatColor.DARK_GRAY + "/bwextra npc add <random, solo, duo, squad>");
        player.sendMessage(ChatColor.DARK_GRAY + "/bwextra npc add <random, solo, duo, squad> <playerSkin>");
        player.sendMessage(ChatColor.DARK_GRAY + "/bwextra npc clear");
        player.sendMessage(ChatColor.DARK_GRAY + "/bwextra npc addteam <teamColor>");
    }
    
    public String capitalize(final String str) {
        if (str == null) {
            return str;
        }
        return String.valueOf(str.substring(0, 1).toUpperCase()) + str.substring(1).toLowerCase();
    }
}
