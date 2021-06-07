package me.unldenis.bedwars.commands;

import java.util.List;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import java.util.UUID;
import org.bukkit.entity.ArmorStand;
import org.bukkit.Bukkit;
import java.util.ArrayList;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import me.unldenis.bedwars.util.MyItems;
import me.unldenis.bedwars.Bedwars;
import org.bukkit.command.CommandExecutor;

public class Clan implements CommandExecutor
{
    private Bedwars main;
    private MyItems myItm;
    
    public Clan(final Bedwars main) {
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
        if (cmd.getName().equalsIgnoreCase("clan")) {
            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("create")) {
                    if (this.main.getClanManager().hasClan(player)) {
                        player.sendMessage(ChatColor.RED + this.main.getMessagesData().getConfig().getString("clan.already-a-clan"));
                        return true;
                    }
                    if (this.clanExist(args[1])) {
                        player.sendMessage(ChatColor.RED + this.main.getMessagesData().getConfig().getString("clan.already-exist"));
                        return true;
                    }
                    this.main.getClanManager().createClan(player.getUniqueId(), args[1]);
                    this.main.getClansData().getConfig().set("clans." + args[1] + ".score", (Object)0);
                    this.main.getClansData().getConfig().set("clans." + args[1] + ".founder", (Object)player.getUniqueId().toString());
                    final List<String> l = new ArrayList<String>();
                    l.add(player.getUniqueId().toString());
                    this.main.getClansData().getConfig().set("clans." + args[1] + ".members", (Object)l);
                    this.main.getClansData().saveConfig();
                    player.sendMessage(ChatColor.GREEN + this.main.getMessagesData().getConfig().getString("clan.created"));
                    return true;
                }
                else if (args[0].equalsIgnoreCase("invite")) {
                    if (!this.clanExist(this.main.getClanManager().getClan(player).getNameClan())) {
                        player.sendMessage(ChatColor.RED + this.main.getMessagesData().getConfig().getString("clan.dont-have-clan"));
                        return true;
                    }
                    if (this.main.getClanManager().hasClan(Bukkit.getPlayer(args[1]))) {
                        String s = this.main.getMessagesData().getConfig().getString("clan.player-has-already-a-clan");
                        s = s.replace("%playerclan", this.main.getClanManager().getClan(Bukkit.getPlayer(args[1])).getNameClan());
                        player.sendMessage(ChatColor.RED + s);
                        return true;
                    }
                    if (this.main.getClanManager().hasClanRequest(Bukkit.getPlayer(args[1]))) {
                        String s = this.main.getMessagesData().getConfig().getString("clan.player-has-already-a-clan-request");
                        s = s.replace("%playerclanrequest", this.main.getClanManager().getClanRequest(Bukkit.getPlayer(args[1])).getNameClan());
                        player.sendMessage(ChatColor.RED + s);
                        return true;
                    }
                    this.main.getClanManager().getClan(player).sendRequestToPlayer(player, Bukkit.getPlayer(args[1]));
                    String s = this.main.getMessagesData().getConfig().getString("clan.invited");
                    s = s.replace("%player", args[1]);
                    player.sendMessage(ChatColor.GREEN + s);
                    return true;
                }
                else if (args[0].equalsIgnoreCase("kick")) {
                    final me.unldenis.bedwars.object.clans.Clan cl = this.main.getClanManager().getClan(player);
                    if (!this.clanExist(cl.getNameClan())) {
                        player.sendMessage(ChatColor.RED + this.main.getMessagesData().getConfig().getString("clan.dont-have-clan"));
                        return true;
                    }
                    if (!cl.getFounder().equals(player.getUniqueId())) {
                        player.sendMessage(ChatColor.RED + this.main.getMessagesData().getConfig().getString("clan.not-founder"));
                        return true;
                    }
                    if (!this.main.getClanManager().hasClan(Bukkit.getPlayer(args[1]))) {
                        player.sendMessage(ChatColor.RED + this.main.getMessagesData().getConfig().getString("clan.player-has-not-a-clan"));
                        return true;
                    }
                    if (cl.equals(this.main.getClanManager().getClan(Bukkit.getPlayer(args[1])))) {
                        cl.kickPlayer(Bukkit.getPlayer(args[1]));
                        return true;
                    }
                }
                else if (args[0].equalsIgnoreCase("top")) {
                    if (Integer.parseInt(args[1]) <= 0) {
                        player.sendMessage(ChatColor.RED + "The number must be positive");
                        return true;
                    }
                    final List<me.unldenis.bedwars.object.clans.Clan> tops = this.main.getClanManager().sortClans();
                    for (int j = 0; j < ((tops.size() >= Integer.parseInt(args[1])) ? Integer.parseInt(args[1]) : tops.size()); ++j) {
                        player.sendMessage(new StringBuilder().append(ChatColor.GOLD).append(ChatColor.BOLD).append(j + 1).append("° ").append(ChatColor.GRAY).append(ChatColor.BOLD).append(tops.get(j).getNameClan()).append(ChatColor.RESET).append(" founded by ").append(ChatColor.UNDERLINE).append(Bukkit.getOfflinePlayer(tops.get(j).getFounder()).getName()).toString());
                    }
                    return true;
                }
                else if (args[0].equalsIgnoreCase("set")) {
                    if (!player.hasPermission("clan.admin")) {
                        player.sendMessage(ChatColor.RED + "You are not authorized to use this command");
                        return true;
                    }
                    final int n = Integer.parseInt(args[1]);
                    if (n != 1 && n != 2 && n != 3) {
                        player.sendMessage(ChatColor.RED + "The number must be <1/2/3>");
                        return true;
                    }
                    final List<me.unldenis.bedwars.object.clans.Clan> tops2 = this.main.getClanManager().sortClans();
                    final ArmorStand stand = (ArmorStand)player.getLocation().getWorld().spawn(player.getLocation(), ArmorStand.class);
                    stand.setArms(true);
                    final UUID rand = UUID.randomUUID();
                    this.main.getClansData().getConfig().set("armorstand." + rand + ".location", (Object)player.getLocation());
                    this.main.getClansData().getConfig().set("armorstand." + rand + ".rank", (Object)n);
                    this.main.getClansData().saveConfig();
                    switch (n) {
                        case 1: {
                            stand.setHelmet(this.myItm.getHead(Bukkit.getOfflinePlayer(tops2.get(0).getFounder()).getName()));
                            stand.setCustomName("+" + ChatColor.GREEN + tops2.get(0).getScore() + ChatColor.GOLD + " " + ChatColor.BOLD + tops2.get(0).getNameClan());
                            stand.setCustomNameVisible(true);
                            stand.setChestplate(new ItemStack(Material.GOLDEN_CHESTPLATE));
                            stand.setLeggings(new ItemStack(Material.GOLDEN_LEGGINGS));
                            stand.setBoots(new ItemStack(Material.GOLDEN_BOOTS));
                            break;
                        }
                        case 2: {
                            stand.setHelmet(this.myItm.getHead(Bukkit.getOfflinePlayer(tops2.get(1).getFounder()).getName()));
                            stand.setCustomName("+" + ChatColor.GREEN + tops2.get(1).getScore() + ChatColor.WHITE + " " + ChatColor.BOLD + tops2.get(1).getNameClan());
                            stand.setCustomNameVisible(true);
                            stand.setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
                            stand.setLeggings(new ItemStack(Material.IRON_LEGGINGS));
                            stand.setBoots(new ItemStack(Material.IRON_BOOTS));
                            break;
                        }
                        case 3: {
                            stand.setHelmet(this.myItm.getHead(Bukkit.getOfflinePlayer(tops2.get(2).getFounder()).getName()));
                            stand.setCustomName("+" + ChatColor.GREEN + tops2.get(2).getScore() + ChatColor.RED + " " + ChatColor.BOLD + tops2.get(2).getNameClan());
                            stand.setCustomNameVisible(true);
                            stand.setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
                            stand.setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
                            stand.setBoots(new ItemStack(Material.LEATHER_BOOTS));
                            break;
                        }
                    }
                    return true;
                }
            }
            else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("help")) {
                    this.help(player);
                    return true;
                }
                if (args[0].equalsIgnoreCase("accept")) {
                    if (!this.main.getClanManager().hasClanRequest(player)) {
                        player.sendMessage(ChatColor.RED + this.main.getMessagesData().getConfig().getString("clan.dont-have-clan-request"));
                        return true;
                    }
                    this.main.getClanManager().getClanRequest(player).acceptRequest(player);
                    return true;
                }
                else if (args[0].equalsIgnoreCase("find")) {
                    final me.unldenis.bedwars.object.clans.Clan cl = this.main.getClanManager().getClan(player);
                    if (!this.clanExist(cl.getNameClan())) {
                        player.sendMessage(ChatColor.RED + "Your clan doesn't exist");
                        return true;
                    }
                    if (!cl.getFounder().equals(player.getUniqueId())) {
                        player.sendMessage(ChatColor.RED + "You aren't the founder of your clan");
                        return true;
                    }
                    return true;
                }
                else if (args[0].equalsIgnoreCase("list")) {
                    final me.unldenis.bedwars.object.clans.Clan cl = this.main.getClanManager().getClan(player);
                    if (!this.clanExist(cl.getNameClan())) {
                        player.sendMessage(ChatColor.RED + this.main.getMessagesData().getConfig().getString("clan.dont-have-clan"));
                        return true;
                    }
                    cl.listMembers(player);
                    return true;
                }
            }
        }
        player.sendMessage(ChatColor.RED + "/clan help");
        return true;
    }
    
    private void help(final Player player) {
        player.sendMessage(ChatColor.GOLD + "Bedwars Clan's Help");
        player.sendMessage(ChatColor.DARK_GRAY + "/clan create <clanName>");
        player.sendMessage(ChatColor.DARK_GRAY + "/clan invite <playerName>");
        player.sendMessage(ChatColor.DARK_GRAY + "/clan kick <playerName>");
        player.sendMessage(ChatColor.DARK_GRAY + "/clan accept");
        player.sendMessage(ChatColor.DARK_GRAY + "/clan top <numberTop>");
        player.sendMessage(ChatColor.DARK_GRAY + "/clan list");
        if (player.hasPermission("clan.admin")) {
            player.sendMessage(ChatColor.GOLD + "Admin");
            player.sendMessage(ChatColor.DARK_GRAY + "/clan set <1-2-3>");
            player.sendMessage(ChatColor.DARK_GRAY + "/clan clear tops");
        }
    }
    
    public boolean clanExist(final String clan) {
        return this.main.getClansData().getConfig().contains("clans." + clan + ".founder");
    }
}
