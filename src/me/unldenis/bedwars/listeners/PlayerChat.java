
package me.unldenis.bedwars.listeners;

import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import me.unldenis.bedwars.object.GamePlayer;
import me.unldenis.bedwars.object.Game;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import me.unldenis.bedwars.util.MyChat;
import me.unldenis.bedwars.Bedwars;
import org.bukkit.event.Listener;

public class PlayerChat implements Listener
{
    private Bedwars main;
    private MyChat myCht;
    
    public PlayerChat(final Bedwars bedwars) {
        this.main = bedwars;
        this.myCht = new MyChat();
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerChatEvent(final AsyncPlayerChatEvent event) {
        final Player player = event.getPlayer();
        final Game game = this.main.getGameManager().getGame(player);
        if (game != null && game.getGamePlayer(player) != null) {
            final GamePlayer gamePlayer = game.getGamePlayer(player);
            if (gamePlayer.getPlayer() == player && game.getTeamManager().getTeamFromPlayer(gamePlayer) != null) {
                if (this.main.getClanManager().hasClan(player)) {
                    event.setFormat(new StringBuilder().append(ChatColor.GRAY).append(ChatColor.ITALIC).append(this.main.getClanManager().getClan(player).getNameClan()).append(ChatColor.WHITE).append(">> ").append(ChatColor.DARK_GRAY).append("[").append(this.myCht.prefix(gamePlayer.getTeamPlayer())).append(gamePlayer.getTeamPlayer()).append(ChatColor.DARK_GRAY).append("] ").append(ChatColor.WHITE).append(gamePlayer.getName()).append(": ").append(event.getMessage()).toString());
                }
                else {
                    event.setFormat(ChatColor.DARK_GRAY + "[" + this.myCht.prefix(gamePlayer.getTeamPlayer()) + gamePlayer.getTeamPlayer() + ChatColor.DARK_GRAY + "] " + ChatColor.WHITE + gamePlayer.getName() + ": " + event.getMessage());
                }
                return;
            }
        }
        if (this.main.getClanManager().hasClan(player)) {
            event.setFormat(new StringBuilder().append(ChatColor.GRAY).append(ChatColor.ITALIC).append(this.main.getClanManager().getClan(player).getNameClan()).append(ChatColor.WHITE).append(">> ").append(player.getName()).append(": ").append(event.getMessage()).toString());
        }
        else {
            event.setFormat(ChatColor.WHITE + player.getName() + ": " + event.getMessage());
        }
    }
}
