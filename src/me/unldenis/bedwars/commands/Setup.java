package me.unldenis.bedwars.commands;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import me.unldenis.bedwars.object.Game;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import me.unldenis.bedwars.object.GamePlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import me.unldenis.bedwars.util.MyItems;
import me.unldenis.bedwars.util.MyChat;
import me.unldenis.bedwars.Bedwars;
import org.bukkit.command.CommandExecutor;

public class Setup implements CommandExecutor
{
    private Bedwars main;
    private MyChat myCht;
    private MyItems myItm;
    
    public Setup(final Bedwars main) {
        this.main = main;
        this.myCht = new MyChat();
        this.myItm = new MyItems();
    }
    
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use that command!");
            return true;
        }
        final Player player = (Player)sender;
        if (cmd.getName().equalsIgnoreCase("bw")) {
            if (args.length == 3) {
                if (!player.hasPermission("bw.admin")) {
                    player.sendMessage(ChatColor.RED + "You are not authorized to use this command");
                    return true;
                }
                if (args[0].equalsIgnoreCase("create")) {
                    if (this.arenaExist(args[1])) {
                        player.sendMessage(ChatColor.RED + "This arena already exist");
                        return true;
                    }
                    this.main.getArenasData().getConfig().set("arenas." + args[1] + ".madeBy", (Object)args[2]);
                    this.main.getArenasData().getConfig().set("arenas." + args[1] + ".minPlayers", (Object)2);
                    this.main.getArenasData().getConfig().set("arenas." + args[1] + ".maxPlayers", (Object)8);
                    this.main.getArenasData().saveConfig();
                    player.sendMessage(ChatColor.GREEN + args[1] + " arena created successfully");
                    return true;
                }
                else if (args[0].equalsIgnoreCase("setmax")) {
                    if (!this.arenaExist(args[1])) {
                        player.sendMessage(ChatColor.RED + "This arena doesn't exist");
                        return true;
                    }
                    this.main.getArenasData().getConfig().set("arenas." + args[1] + ".maxPlayers", (Object)Integer.parseInt(args[2]));
                    this.main.getArenasData().saveConfig();
                    player.sendMessage(ChatColor.GREEN + "Max Players set successfully");
                    return true;
                }
                else if (args[0].equalsIgnoreCase("setmin")) {
                    if (!this.arenaExist(args[1])) {
                        player.sendMessage(ChatColor.RED + "This arena doesn't exist");
                        return true;
                    }
                    this.main.getArenasData().getConfig().set("arenas." + args[1] + ".minPlayers", (Object)Integer.parseInt(args[2]));
                    this.main.getArenasData().saveConfig();
                    player.sendMessage(ChatColor.GREEN + "Min players set successfully");
                    return true;
                }
                else if (args[0].equalsIgnoreCase("setspawn")) {
                    if (!this.arenaExist(args[1])) {
                        player.sendMessage(ChatColor.RED + "This arena doesn't exist");
                        return true;
                    }
                    this.main.getArenasData().getConfig().set("arenas." + args[1] + ".locations.teams." + args[2] + ".spawn", (Object)player.getLocation());
                    this.main.getArenasData().saveConfig();
                    player.sendMessage(ChatColor.GREEN + "Spawn set successfully");
                    return true;
                }
            }
            else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("join")) {
                    if (!this.arenaExist(args[1])) {
                        player.sendMessage(ChatColor.RED + "This arena doesn't exist");
                        return true;
                    }
                    if (this.main.getGameManager().isInGame(player)) {
                        player.sendMessage(this.myCht.format("&cYou're in a game."));
                        return true;
                    }
                    final Game game = this.main.getGameManager().getGame(args[1]);
                    game.joinGame(new GamePlayer(player, this.main));
                    return true;
                }
                else if (args[0].equalsIgnoreCase("getbeds")) {
                    if (!player.hasPermission("bw.admin")) {
                        player.sendMessage(ChatColor.RED + "You are not authorized to use this command");
                        return true;
                    }
                    if (!this.arenaExist(args[1])) {
                        player.sendMessage(ChatColor.RED + "This arena doesn't exist");
                        return true;
                    }
                    for (final String gameName : this.main.getArenasData().getConfig().getConfigurationSection("arenas." + args[1] + ".locations.teams").getKeys(false)) {
                        player.getInventory().addItem(new ItemStack[] { this.myItm.nameItem(new ItemStack(Material.RED_BED), ChatColor.DARK_RED + "Bedwars-" + gameName, args[1]) });
                    }
                    player.sendMessage(ChatColor.GREEN + "Place them everywhere in the world");
                    return true;
                }
                else if (args[0].equalsIgnoreCase("setlobby")) {
                    if (!player.hasPermission("bw.admin")) {
                        player.sendMessage(ChatColor.RED + "You are not authorized to use this command");
                        return true;
                    }
                    if (!this.arenaExist(args[1])) {
                        player.sendMessage(ChatColor.RED + "This arena doesn't exist");
                        return true;
                    }
                    this.main.getArenasData().getConfig().set("arenas." + args[1] + ".locations.lobby", (Object)player.getLocation());
                    this.main.getArenasData().saveConfig();
                    player.sendMessage(ChatColor.GREEN + "Lobby set successfully");
                    return true;
                }
                else if (args[0].equalsIgnoreCase("save")) {
                    if (!player.hasPermission("bw.admin")) {
                        player.sendMessage(ChatColor.RED + "You are not authorized to use this command");
                        return true;
                    }
                    if (!this.arenaExist(args[1])) {
                        player.sendMessage(ChatColor.RED + "This arena doesn't exist");
                        return true;
                    }
                    this.main.getGameManager().getGames().add(new Game(this.main, args[1]));
                    this.main.getGameManager().getGame(args[1]).saveMap();
                    player.sendMessage(ChatColor.GREEN + "Arena save successfully");
                    return true;
                }
                else if (args[0].equalsIgnoreCase("addbronze")) {
                    if (!player.hasPermission("bw.admin")) {
                        player.sendMessage(ChatColor.RED + "You are not authorized to use this command");
                        return true;
                    }
                    if (!this.arenaExist(args[1])) {
                        player.sendMessage(ChatColor.RED + "This arena doesn't exist");
                        return true;
                    }
                    final Block b = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
                    try {
                        final int savedspawns = this.main.getArenasData().getConfig().getInt("arenas." + args[1] + ".locations.bronzespawns.amount");
                        this.main.getArenasData().getConfig().set("arenas." + args[1] + ".locations.bronzespawns.amount", (Object)(savedspawns + 1));
                        this.main.getArenasData().getConfig().set("arenas." + args[1] + ".locations.bronzespawns." + (savedspawns + 1), (Object)b.getLocation());
                        player.sendMessage(ChatColor.GREEN + "Bronze n°" + (savedspawns + 1) + " add successfully");
                    }
                    catch (Exception e) {
                        this.main.getArenasData().getConfig().set("arenas." + args[1] + ".locations.bronzespawns.amount", (Object)1);
                        this.main.getArenasData().getConfig().set("arenas." + args[1] + ".locations.bronzespawns." + 0, (Object)b.getLocation());
                        player.sendMessage(ChatColor.GREEN + "(Exc) Bronze n°1 add successfully");
                    }
                    this.main.getArenasData().saveConfig();
                    return true;
                }
                else if (args[0].equalsIgnoreCase("addiron")) {
                    if (!player.hasPermission("bw.admin")) {
                        player.sendMessage(ChatColor.RED + "You are not authorized to use this command");
                        return true;
                    }
                    if (!this.arenaExist(args[1])) {
                        player.sendMessage(ChatColor.RED + "This arena doesn't exist");
                        return true;
                    }
                    final Block b = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
                    try {
                        final int savedspawns = this.main.getArenasData().getConfig().getInt("arenas." + args[1] + ".locations.ironspawns.amount");
                        this.main.getArenasData().getConfig().set("arenas." + args[1] + ".locations.ironspawns.amount", (Object)(savedspawns + 1));
                        this.main.getArenasData().getConfig().set("arenas." + args[1] + ".locations.ironspawns." + (savedspawns + 1), (Object)b.getLocation());
                        player.sendMessage(ChatColor.GREEN + "Iron n°" + (savedspawns + 1) + " add successfully");
                    }
                    catch (Exception e) {
                        this.main.getArenasData().getConfig().set("arenas." + args[1] + ".locations.ironspawns.amount", (Object)1);
                        this.main.getArenasData().getConfig().set("arenas." + args[1] + ".locations.ironspawns." + 0, (Object)b.getLocation());
                        player.sendMessage(ChatColor.GREEN + "(Exc) Iron n°1 add successfully");
                    }
                    this.main.getArenasData().saveConfig();
                    return true;
                }
                else if (args[0].equalsIgnoreCase("addgold")) {
                    if (!player.hasPermission("bw.admin")) {
                        player.sendMessage(ChatColor.RED + "You are not authorized to use this command");
                        return true;
                    }
                    if (!this.arenaExist(args[1])) {
                        player.sendMessage(ChatColor.RED + "This arena doesn't exist");
                        return true;
                    }
                    final Block b = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
                    try {
                        final int savedspawns = this.main.getArenasData().getConfig().getInt("arenas." + args[1] + ".locations.goldspawns.amount");
                        this.main.getArenasData().getConfig().set("arenas." + args[1] + ".locations.goldspawns.amount", (Object)(savedspawns + 1));
                        this.main.getArenasData().getConfig().set("arenas." + args[1] + ".locations.goldspawns." + (savedspawns + 1), (Object)b.getLocation());
                        player.sendMessage(ChatColor.GREEN + "Gold n°" + (savedspawns + 1) + " add successfully");
                    }
                    catch (Exception e) {
                        this.main.getArenasData().getConfig().set("arenas." + args[1] + ".locations.goldspawns.amount", (Object)1);
                        this.main.getArenasData().getConfig().set("arenas." + args[1] + ".locations.goldspawns." + 0, (Object)b.getLocation());
                        player.sendMessage(ChatColor.GREEN + "(Exc) Gold n°1 add successfully");
                    }
                    this.main.getArenasData().saveConfig();
                    return true;
                }
            }
            else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("help")) {
                    player.sendMessage(ChatColor.GOLD + "Bedwars Arena's Help");
                    player.sendMessage(ChatColor.DARK_GRAY + "/bw join <arenaName>");
                    if (player.hasPermission("bw.admin")) {
                        player.sendMessage(ChatColor.GOLD + "Admin");
                        player.sendMessage(ChatColor.DARK_GRAY + "/bw setmainlobby");
                        player.sendMessage(ChatColor.DARK_GRAY + "/bw create <arenaName> <made by...>");
                        player.sendMessage(ChatColor.DARK_GRAY + "/bw setmax <arenaName> <maxPlayers>");
                        player.sendMessage(ChatColor.DARK_GRAY + "/bw setmin <arenaName> <minPlayersToStart>");
                        player.sendMessage(ChatColor.DARK_GRAY + "/bw setlobby <arenaName>");
                        player.sendMessage(ChatColor.DARK_GRAY + "/bw setspawn <arenaName> <team>");
                        player.sendMessage(ChatColor.DARK_GRAY + "/bw getbeds <arenaName>");
                        player.sendMessage(ChatColor.DARK_GRAY + "/bw addbronze <arenaName>");
                        player.sendMessage(ChatColor.DARK_GRAY + "/bw addiron <arenaName>");
                        player.sendMessage(ChatColor.DARK_GRAY + "/bw addgold <arenaName>");
                        player.sendMessage(ChatColor.DARK_GRAY + "/bw save <arenaName>");
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("setmainlobby")) {
                    this.main.getArenasData().getConfig().set("main-lobby", (Object)player.getLocation());
                    this.main.getArenasData().saveConfig();
                    player.sendMessage(ChatColor.GREEN + "Main Lobby set successfully");
                    return true;
                }
            }
            player.sendMessage(ChatColor.RED + "/bw help");
        }
        return true;
    }
    
    public boolean arenaExist(final String arena) {
        return this.main.getArenasData().getConfig().contains("arenas." + arena + ".madeBy");
    }
}
