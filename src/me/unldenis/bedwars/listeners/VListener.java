
package me.unldenis.bedwars.listeners;

import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import me.unldenis.bedwars.villager.Villager;
import me.unldenis.bedwars.object.Game;
import org.bukkit.entity.EntityType;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import me.unldenis.bedwars.Bedwars;
import org.bukkit.event.Listener;

public class VListener implements Listener
{
    private Bedwars main;
    
    public VListener(final Bedwars bedwars) {
        this.main = bedwars;
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onInteract(final PlayerInteractEntityEvent e) {
        if (e.getRightClicked().getType() == EntityType.VILLAGER && this.main.getGameManager().isInGame(e.getPlayer())) {
            final Player player = e.getPlayer();
            final Game game = this.main.getGameManager().getGame(player);
            if (game != null && game.getGamePlayer(player) != null && game.isState(Game.GameState.PLAYING)) {
                e.setCancelled(true);
                final Villager screen = new Villager(game.getGamePlayer(player).getTeamPlayer());
                player.openInventory(screen.getInventory());
            }
        }
    }
}
