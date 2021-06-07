
package me.unldenis.bedwars.listeners;

import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.meta.ItemMeta;
import me.unldenis.bedwars.object.clans.Clan;
import me.unldenis.bedwars.object.teams.Team;
import me.unldenis.bedwars.object.GamePlayer;
import me.unldenis.bedwars.inventories.TopClans;
import me.unldenis.bedwars.inventories.TopPlayers;
import me.unldenis.bedwars.inventories.GamesList;
import me.unldenis.bedwars.object.mapvoting.MVInventory;
import me.unldenis.bedwars.scoreboards.MainLobbyScoreboard;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import me.unldenis.bedwars.scoreboards.Board;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.attribute.Attribute;
import org.bukkit.inventory.ItemStack;
import me.unldenis.bedwars.inventories.SelectTeam;
import me.unldenis.bedwars.object.Game;
import org.bukkit.Material;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import me.unldenis.bedwars.util.MyItems;
import me.unldenis.bedwars.Bedwars;
import org.bukkit.event.Listener;

public class Interacts implements Listener
{
    private Bedwars main;
    private MyItems myItm;
    
    public Interacts(final Bedwars bedwars) {
        this.main = bedwars;
        this.myItm = new MyItems();
    }
    
    @SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.HIGH)
    public void onSalvage(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (player.getItemInHand().getType() == Material.NETHER_STAR) {
                final Game game = this.main.getGameManager().getGame(player);
                if (game != null && game.getGamePlayer(player) != null) {
                    final GamePlayer gamePlayer = game.getGamePlayer(player);
                    if (game.isState(Game.GameState.LOBBY) || game.isState(Game.GameState.STARTING)) {
                        event.setCancelled(true);
                        final SelectTeam screen = new SelectTeam(game);
                        gamePlayer.getPlayer().openInventory(screen.getInventory());
                    }
                }
            }
            if (player.getItemInHand().getType() == Material.SLIME_BALL) {
                final Game game = this.main.getGameManager().getGame(player);
                if (game != null && game.getGamePlayer(player) != null) {
                    final GamePlayer gamePlayer = game.getGamePlayer(player);
                    if (game.isState(Game.GameState.LOBBY) || game.isState(Game.GameState.STARTING)) {
                        event.setCancelled(true);
                        game.getPlayers().remove(gamePlayer);
                        if (game.getMapVoting() != null) {
                            game.getMapVoting().getVote_1().remove(gamePlayer);
                            game.getMapVoting().getVote_2().remove(gamePlayer);
                        }
                        final Team tm = game.getTeamManager().getTeamFromPlayer(gamePlayer);
                        if (tm != null) {
                            tm.removePlayer(gamePlayer);
                            if (tm.isPrivateTeam()) {
                                final Clan cl = this.main.getClanManager().getClan(player);
                                if (cl.isFounder(player)) {
                                    tm.setPrivateTeam(false);
                                }
                            }
                        }
                        String s = this.main.getMessagesData().getConfig().getString("game.left-lobby");
                        s = s.replace("%player", "&7" + gamePlayer.getName() + "&6");
                        game.sendMessage(s);
                        player.teleport(this.main.getArenasData().getConfig().getLocation("main-lobby"));
                        player.getInventory().clear();
                        player.getInventory().setArmorContents((ItemStack[])null);
                        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20.0);
                        player.setGameMode(GameMode.ADVENTURE);
                        player.getWorld().setDifficulty(Difficulty.PEACEFUL);
                        ItemStack testEnchant = new ItemStack(Material.COMPASS, 1);
                        ItemMeta meta = testEnchant.getItemMeta();
                        meta.addEnchant(Enchantment.LURE, 1, false);
                        meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ENCHANTS });
                        testEnchant.setItemMeta(meta);
                        player.getInventory().setItem(0, this.myItm.nameItem(testEnchant, "§e§l" + this.main.getMessagesData().getConfig().getString("items.play"), ""));
                        testEnchant = this.myItm.getHead(player.getName());
                        meta = testEnchant.getItemMeta();
                        meta.addEnchant(Enchantment.LURE, 1, false);
                        meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ENCHANTS });
                        testEnchant.setItemMeta(meta);
                        player.getPlayer().getInventory().setItem(1, this.myItm.nameItem(testEnchant, "§3§l" + this.main.getMessagesData().getConfig().getString("items.top-players"), ""));
                        testEnchant = new ItemStack(Material.EMERALD, 1);
                        meta = testEnchant.getItemMeta();
                        meta.addEnchant(Enchantment.LURE, 1, false);
                        meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ENCHANTS });
                        testEnchant.setItemMeta(meta);
                        player.getPlayer().getInventory().setItem(2, this.myItm.nameItem(testEnchant, "§5§l" + this.main.getMessagesData().getConfig().getString("items.top-clans"), ""));
                        final Board board = new Board(player.getUniqueId());
                        if (board.hasID()) {
                            board.stop();
                        }
                        player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
                        for (final Player p : this.main.getGameManager().getPlayersInGame()) {
                            player.showPlayer(p);
                            p.showPlayer(player);
                        }
                        player.getPlayer().setDisplayName(gamePlayer.getName());
                        this.main.getGameManager().setGame(player, null);
                        final MainLobbyScoreboard score = new MainLobbyScoreboard(this.main);
                        score.createBoard(player);
                        score.startScoreboard_Lobby(player);
                    }
                }
            }
            if (player.getItemInHand().getType() == Material.PAPER) {
                final Game game = this.main.getGameManager().getGame(player);
                if (game != null && game.getGamePlayer(player) != null) {
                    final GamePlayer gamePlayer = game.getGamePlayer(player);
                    if (game.isState(Game.GameState.LOBBY) || game.isState(Game.GameState.STARTING)) {
                        event.setCancelled(true);
                        final MVInventory screen2 = new MVInventory(game.getMapVoting());
                        gamePlayer.getPlayer().openInventory(screen2.getInventory());
                    }
                }
            }
            if (player.getItemInHand().getType() == Material.COMPASS && player.getItemInHand().getItemMeta().getDisplayName().equals("§e§l" + this.main.getMessagesData().getConfig().getString("items.play"))) {
                event.setCancelled(true);
                final GamesList screen3 = new GamesList(this.main);
                player.openInventory(screen3.getInventory());
            }
            if (player.getItemInHand().getType() == Material.PLAYER_HEAD && player.getItemInHand().getItemMeta().getDisplayName().equals("§3§l" + this.main.getMessagesData().getConfig().getString("items.top-players"))) {
                event.setCancelled(true);
                final TopPlayers screen4 = new TopPlayers(this.main);
                player.openInventory(screen4.getInventory());
            }
            if (player.getItemInHand().getType() == Material.EMERALD && player.getItemInHand().getItemMeta().getDisplayName().equals("§5§l" + this.main.getMessagesData().getConfig().getString("items.top-clans"))) {
                event.setCancelled(true);
                final TopClans screen5 = new TopClans(this.main);
                player.openInventory(screen5.getInventory());
            }
        }
    }
}
