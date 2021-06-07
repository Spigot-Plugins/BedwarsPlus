
package me.unldenis.bedwars.listeners;

import org.bukkit.event.EventHandler;
import me.unldenis.bedwars.object.teams.Team;
import me.unldenis.bedwars.object.teams.TeamType;
import me.unldenis.bedwars.object.GamePlayer;
import me.unldenis.bedwars.object.Game;
import org.bukkit.entity.Player;
import me.unldenis.bedwars.object.teams.TeamManager;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import me.unldenis.bedwars.Bedwars;
import org.bukkit.event.Listener;

public class SelectArmor implements Listener
{
    private Bedwars main;
    
    public SelectArmor(final Bedwars plugin) {
        this.main = plugin;
    }
    
    @EventHandler
    public void onPlayerInteractAtEntity(final PlayerInteractAtEntityEvent event) {
        final Player player = event.getPlayer();
        if (event.getRightClicked().getType().equals((Object)EntityType.ARMOR_STAND)) {
            event.setCancelled(true);
            final Game game = this.main.getGameManager().getGame(player);
            if (game != null && game.getGamePlayer(player) != null) {
                final GamePlayer gamePlayer = game.getGamePlayer(player);
                final TeamType tt = TeamManager.teamFromArmorstand((ArmorStand)event.getRightClicked());
                System.out.println("Teamtype: " + tt.toString());
                final Team team = game.getTeamManager().getTeamFromColor(tt);
                System.out.println("Size: " + team.getPlayers().size());
                game.getTeamManager().TSelect(team, gamePlayer);
            }
        }
    }
}
