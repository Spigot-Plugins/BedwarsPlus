package me.unldenis.bedwars.tasks;

import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.Sound;
import org.bukkit.plugin.Plugin;
import me.unldenis.bedwars.object.GamePlayer;
import me.unldenis.bedwars.util.MyScoreboard;
import me.unldenis.bedwars.object.Game;
import org.bukkit.scheduler.BukkitRunnable;

public class GameCountdownTask extends BukkitRunnable
{
    private int time;
    private Game game;
    private MyScoreboard myScr;
    
    public GameCountdownTask(final Game game) {
        this.time = 30;
        this.game = game;
        this.myScr = new MyScoreboard();
    }
    
    public void run() {
        if (this.game.getPlayers().size() < this.game.getMinPlayers()) {
            this.cancel();
            this.game.setGameState(Game.GameState.LOBBY);
            this.game.sendMessage("&cThere are no longer enough players to start the game");
            for (final GamePlayer p2 : this.game.getPlayers()) {
                final Scoreboard scoreboard = p2.getPlayer().getScoreboard();
                final Objective obj = scoreboard.getObjective("Bedwars-1");
                this.myScr.replaceScore(obj, 3, "Waiting...");
            }
            return;
        }
        --this.time;
        if (this.time == 0) {
            this.cancel();
            new GameRunTask(this.game).runTaskTimer((Plugin)this.game.getMain(), 0L, 20L);
        }
        else {
            if (this.time == 20 || this.time == 15 || this.time == 10 || this.time == 5) {
                this.game.sendMessage("&a[*] You'll be teleported to the game in " + this.time + " seconds");
                this.game.getPlayers().forEach(p -> p.getPlayer().playSound(p.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 2.0f));
            }
            for (final GamePlayer p2 : this.game.getPlayers()) {
                final Scoreboard scoreboard = p2.getPlayer().getScoreboard();
                final Objective obj = scoreboard.getObjective("Bedwars-1");
                this.myScr.replaceScore(obj, 3, "Starting in " + this.time);
            }
        }
    }
}
