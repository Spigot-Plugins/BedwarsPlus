
package me.unldenis.bedwars.listeners;

import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import me.unldenis.bedwars.object.GamePlayer;
import me.unldenis.bedwars.object.Game;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import me.unldenis.bedwars.Bedwars;
import org.bukkit.event.Listener;

public class Damage implements Listener
{
    private Bedwars main;
    
    public Damage(final Bedwars plugin) {
        this.main = plugin;
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onHit(final EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            final Player player = (Player)event.getEntity();
            final Game game = this.main.getGameManager().getGame(player);
            if (game != null && game.getGamePlayer(player) != null) {
                if (game.isState(Game.GameState.LOBBY) || game.isState(Game.GameState.STARTING)) {
                    event.setCancelled(true);
                    return;
                }
                final GamePlayer gamePlayer = game.getGamePlayer(player);
                final Player enemy = (Player)event.getDamager();
                final GamePlayer enemyPlayer = game.getGamePlayer(enemy);
                if (gamePlayer.getTeamPlayer().equals(enemyPlayer.getTeamPlayer())) {
                    event.setCancelled(true);
                }
            }
            else {
                event.setCancelled(true);
            }
        }
    }
}
