
package me.unldenis.bedwars.listeners;

import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import me.unldenis.bedwars.object.teams.Team;
import me.unldenis.bedwars.object.mapvoting.MVInventory;
import me.unldenis.bedwars.villager.Villager;
import org.bukkit.Material;
import me.unldenis.bedwars.object.teams.TeamManager;
import me.unldenis.bedwars.inventories.SelectTeam;
import me.unldenis.bedwars.inventories.TopPlayers;
import me.unldenis.bedwars.object.GamePlayer;
import me.unldenis.bedwars.object.Game;
import me.unldenis.bedwars.inventories.GamesList;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import me.unldenis.bedwars.Bedwars;
import org.bukkit.event.Listener;

public class InventoryEvents implements Listener
{
    private Bedwars main;
    
    public InventoryEvents(final Bedwars bedwars) {
        this.main = bedwars;
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onClick(final InventoryClickEvent e) {
        final Player player = (Player)e.getWhoClicked();
        if (e.getClickedInventory().getHolder() instanceof GamesList) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null) {
                return;
            }
            this.main.getGameManager().getGames().get(e.getSlot()).joinGame(new GamePlayer(player, this.main));
            player.closeInventory();
        }
        if (e.getClickedInventory().getHolder() instanceof TopPlayers) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null) {
                return;
            }
        }
        final Game game = this.main.getGameManager().getGame(player);
        if (game != null && game.getGamePlayer(player) != null) {
            final GamePlayer gamePlayer = game.getGamePlayer(player);
            if (e.getClickedInventory() == null) {
                return;
            }
            if (e.getClickedInventory().getHolder() instanceof SelectTeam) {
                e.setCancelled(true);
                if (e.getCurrentItem() == null) {
                    return;
                }
                final Team team = game.getTeamManager().getTeamFromColor(TeamManager.teamFromMaterial(e.getCurrentItem().getType()));
                game.getTeamManager().TSelect(team, gamePlayer);
                gamePlayer.getPlayer().closeInventory();
            }
            if (game.isState(Game.GameState.LOBBY) || game.isState(Game.GameState.STARTING)) {
                e.setCancelled(true);
                if (e.getCurrentItem().getType().equals((Object)Material.NETHER_STAR)) {
                    final SelectTeam screen = new SelectTeam(game);
                    player.openInventory(screen.getInventory());
                }
            }
            if (e.getClickedInventory().getHolder() instanceof Villager) {
                e.setCancelled(true);
                if (e.getCurrentItem() == null) {
                    return;
                }
                Villager.instance.buyItemFromMaterial(e.getSlot(), gamePlayer, e.getCurrentItem().getType());
            }
            if (e.getClickedInventory().getHolder() instanceof MVInventory) {
                e.setCancelled(true);
                if (e.getCurrentItem() == null) {
                    return;
                }
                if (e.getSlot() == 11) {
                    game.getMapVoting().MSelect(true, gamePlayer);
                    gamePlayer.getPlayer().closeInventory();
                }
                else if (e.getSlot() == 15) {
                    game.getMapVoting().MSelect(false, gamePlayer);
                    gamePlayer.getPlayer().closeInventory();
                }
            }
        }
    }
}
