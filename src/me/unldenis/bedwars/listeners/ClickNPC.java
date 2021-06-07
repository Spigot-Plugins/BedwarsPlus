
package me.unldenis.bedwars.listeners;

import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import me.unldenis.bedwars.object.GamePlayer;
import me.unldenis.bedwars.npc.RightClickNPC;
import me.unldenis.bedwars.Bedwars;
import org.bukkit.event.Listener;

public class ClickNPC implements Listener
{
    private Bedwars main;
    
    public ClickNPC(final Bedwars bedwars) {
        this.main = bedwars;
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onClick(final RightClickNPC event) {
        final Player player = event.getPlayer();
        if (event.getNPC().getName().equals("Random")) {
            final GamePlayer gm = new GamePlayer(player, this.main);
            this.main.getGameManager().getRandomGame(gm).joinGame(gm);
            return;
        }
        if (event.getNPC().getName().equals("Squad")) {
            try {
                final GamePlayer gm = new GamePlayer(player, this.main);
                this.main.getGameManager().getTeamSizeGame(4, gm).joinGame(gm);
            }
            catch (NullPointerException e) {
                player.sendMessage(ChatColor.RED + "There aren't squad games available.");
            }
            return;
        }
        if (event.getNPC().getName().equals("Duo")) {
            try {
                final GamePlayer gm = new GamePlayer(player, this.main);
                this.main.getGameManager().getTeamSizeGame(2, gm).joinGame(gm);
            }
            catch (NullPointerException e) {
                player.sendMessage(ChatColor.RED + "There aren't squad games available.");
            }
            return;
        }
        if (event.getNPC().getName().equals("Solo")) {
            try {
                final GamePlayer gm = new GamePlayer(player, this.main);
                this.main.getGameManager().getTeamSizeGame(1, gm).joinGame(gm);
            }
            catch (NullPointerException e) {
                player.sendMessage(ChatColor.RED + "There aren't squad games available.");
            }
        }
    }
}
