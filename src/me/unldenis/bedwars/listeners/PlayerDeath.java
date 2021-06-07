
package me.unldenis.bedwars.listeners;

import me.unldenis.bedwars.object.clans.Clan;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import me.unldenis.bedwars.tasks.BackToLobbyTask;
import me.unldenis.bedwars.object.teams.Team;
import org.bukkit.GameMode;
import me.unldenis.bedwars.object.teams.BedState;
import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import me.unldenis.bedwars.object.GamePlayer;
import me.unldenis.bedwars.object.Game;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import me.unldenis.bedwars.util.MyChat;
import me.unldenis.bedwars.Bedwars;
import org.bukkit.event.Listener;

public class PlayerDeath implements Listener
{
    private Bedwars main;
    private MyChat myCht;
    
    public PlayerDeath(final Bedwars bedwars) {
        this.main = bedwars;
        this.myCht = new MyChat();
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onDeath(final PlayerDeathEvent event) {
        final Player player = event.getEntity();
        final Game game = this.main.getGameManager().getGame(player);
        if (game != null && game.getGamePlayer(player) != null) {
            final GamePlayer gamePlayer = game.getGamePlayer(player);
            if (gamePlayer.getPlayer() == player) {
                this.handle(event, game, gamePlayer);
            }
        }
    }
    
    private void handle(final PlayerDeathEvent event, final Game game, final GamePlayer gamePlayer) {
        if (!game.isState(Game.GameState.PLAYING)) {
            return;
        }
        event.setDeathMessage((String)null);
        gamePlayer.addScore(this.main.getStatsManager().getDEATH_POINTS());
        if (game.getTeamManager().getTeamFromPlayer(gamePlayer).getBedState().equals(BedState.BROKEN)) {
            gamePlayer.getPlayer().spigot().respawn();
            final Team tm = game.getTeamManager().getTeamFromPlayer(gamePlayer);
            String s = this.main.getMessagesData().getConfig().getString("game.player-death");
            s = s.replace("%player", String.valueOf(this.myCht.prefix(tm.getTeamType())) + gamePlayer.getName() + "&6");
            game.sendMessage(s);
            tm.removePlayer(gamePlayer);
            gamePlayer.getPlayer().setGameMode(GameMode.SPECTATOR);
            if (tm.getPlayers().size() == 0 && tm.isPrivateTeam()) {
                final Clan clan = this.main.getClanManager().getClan(gamePlayer.getPlayer());
                clan.addLose();
            }
            if (game.getTeamManager().remainsOneTeam().size() == 1) {
                String win_message = "";
                final Team tmWinner = game.getTeamManager().remainsOneTeam().get(0);
                for (int j = 0; j < tmWinner.getPlayers().size(); ++j) {
                    final GamePlayer tmWinnerPlayer = tmWinner.getPlayers().get(j);
                    win_message = String.valueOf(win_message) + this.myCht.prefix(tmWinner.getTeamType()) + tmWinnerPlayer.getName();
                    if (j != tmWinner.getPlayers().size() - 1) {
                        win_message = String.valueOf(win_message) + "&7, ";
                    }
                    tmWinnerPlayer.addScore(this.main.getStatsManager().getWIN_POINTS());
                }
                if (tmWinner.isPrivateTeam()) {
                    this.main.getClanManager().getClan(tmWinner.getPlayers().get(0).getPlayer()).addWin();
                }
                win_message = String.valueOf(win_message) + " &a " + this.main.getMessagesData().getConfig().getString("game.won");
                game.sendMessage(win_message);
                game.sendTitle(String.valueOf(this.myCht.prefix(tmWinner.getTeamType())) + tmWinner + " &7team won", win_message);
                new BackToLobbyTask(game).runTaskTimer((Plugin)this.main, 0L, 20L);
            }
        }
        else {
            final Team tm = game.getTeamManager().getTeamFromPlayer(gamePlayer);
            String s = this.main.getMessagesData().getConfig().getString("game.player-death");
            s = s.replace("%player", String.valueOf(this.myCht.prefix(tm.getTeamType())) + gamePlayer.getName() + "&6");
            game.sendMessage(s);
            gamePlayer.getPlayer().spigot().respawn();
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((Plugin)this.main, (Runnable)new Runnable() {
                @Override
                public void run() {
                    gamePlayer.getPlayer().setGameMode(GameMode.SURVIVAL);
                    gamePlayer.teleport(tm.getSpawn());
                }
            }, 1L);
        }
    }
}
