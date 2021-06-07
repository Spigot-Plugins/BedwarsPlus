package me.unldenis.bedwars.listeners;

import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import me.unldenis.bedwars.object.GamePlayer;
import me.unldenis.bedwars.util.MyScoreboard;
import me.unldenis.bedwars.object.teams.Team;
import org.bukkit.Sound;
import me.unldenis.bedwars.object.teams.BedState;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.block.Block;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import me.unldenis.bedwars.object.Game;
import org.bukkit.event.block.BlockPlaceEvent;
import me.unldenis.bedwars.util.MyChat;
import me.unldenis.bedwars.Bedwars;
import org.bukkit.event.Listener;

public class Beds implements Listener
{
    private Bedwars main;
    private MyChat myCht;
    
    public Beds(final Bedwars bedwars) {
        this.main = bedwars;
        this.myCht = new MyChat();
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlace(final BlockPlaceEvent e) {
        final Player player = e.getPlayer();
        final Game game = this.main.getGameManager().getGame(player);
        if (game != null && game.getGamePlayer(player) != null && game.getGameState().equals(Game.GameState.PLAYING)) {
            game.getPlacedBlocks().add(e.getBlock().getLocation());
        }
        if (e.getBlock().getType().equals((Object)Material.RED_BED) && e.getItemInHand().getItemMeta().getDisplayName().contains("Bedwars-")) {
            final String arena = e.getItemInHand().getItemMeta().getLore().get(0);
            final String team = e.getItemInHand().getItemMeta().getDisplayName().split("-")[1];
            this.main.getArenasData().getConfig().set("arenas." + arena + ".locations.teams." + team + ".bed-1", (Object)e.getBlock().getLocation());
            this.main.getArenasData().saveConfig();
            for (int x = -1; x <= 1; ++x) {
                for (int z = -1; z <= 1; ++z) {
                    if (x != 0 || z != 0) {
                        final Location b = e.getBlock().getLocation().add((double)x, 0.0, (double)z);
                        final Block bBlock = e.getBlock().getWorld().getBlockAt(b);
                        if (bBlock.getType().equals((Object)Material.RED_BED)) {
                            this.main.getArenasData().getConfig().set("arenas." + arena + ".locations.teams." + team + ".bed-2", (Object)b);
                            this.main.getArenasData().saveConfig();
                        }
                    }
                }
            }
            e.getPlayer().sendMessage(ChatColor.GREEN + "Red bed of arena <" + arena + "> saved.");
        }
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onDestroy(final BlockBreakEvent e) {
        final Player player = e.getPlayer();
        final Game game = this.main.getGameManager().getGame(player);
        if (game != null && game.getGamePlayer(player) != null && game.getGameState().equals(Game.GameState.PLAYING)) {
            final GamePlayer gamePlayer = game.getGamePlayer(player);
            if (game.getPlacedBlocks().contains(e.getBlock().getLocation())) {
                game.getPlacedBlocks().remove(e.getBlock().getLocation());
                return;
            }
            if (e.getBlock().getType().equals((Object)Material.RED_BED) && !gamePlayer.getTeamPlayer().equals(game.getTeamManager().getTeamByBedLocation(e.getBlock().getLocation()).getTeamType())) {
                final Team team = game.getTeamManager().getTeamByBedLocation(e.getBlock().getLocation());
                team.setBedState(BedState.BROKEN);
                String s = this.main.getMessagesData().getConfig().getString("game.bed-destroyed");
                s = s.replace("%player", String.valueOf(this.myCht.prefix(gamePlayer.getTeamPlayer())) + gamePlayer.getName() + ChatColor.AQUA);
                s = s.replace("%team", String.valueOf(this.myCht.prefix(team.getTeamType())) + team);
                game.sendMessage(s);
                player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f);
                for (int j = 0; j < game.getTeamManager().getTeams().size(); ++j) {
                    if (game.getTeamManager().getTeams().get(j).equals(team)) {
                        final MyScoreboard myScr = new MyScoreboard();
                        for (final GamePlayer p : game.getPlayers()) {
                            final Scoreboard scoreboard = p.getPlayer().getScoreboard();
                            final Objective obj = scoreboard.getObjective("Bedwars-1");
                            myScr.replaceScore(obj, 3 + j, ChatColor.RED + "\u2764 " + this.myCht.prefix(game.getTeamManager().getTeams().get(j).getTeamType()) + game.getTeamManager().getTeams().get(j).toString());
                        }
                    }
                }
                return;
            }
            e.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onItemSpawn(final ItemSpawnEvent e) {
        final Game game = this.main.getGameManager().getGame(e.getEntity().getWorld());
        if (game != null && game.getGameState().equals(Game.GameState.PLAYING)) {
            final boolean n = e.getEntity().getItemStack().getType() == Material.RED_BED;
            if (n) {
                e.getEntity().remove();
            }
        }
    }
}
