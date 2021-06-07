
package me.unldenis.bedwars.tasks;

import org.bukkit.Sound;
import me.unldenis.bedwars.scoreboards.InGameScoreboard;
import me.unldenis.bedwars.object.GamePlayer;
import me.unldenis.bedwars.object.Game;
import org.bukkit.scheduler.BukkitRunnable;

public class GameRunTask extends BukkitRunnable
{
    private Game game;
    private int startIn;
    
    public GameRunTask(final Game game) {
        this.startIn = 10;
        (this.game = game).setGameState(Game.GameState.PLAYING);
        if (game.getMapVoting() != null) {
            game.getMapVoting().loadWinMap();
        }
        this.game.getTeamManager().autoTeam();
        for (final GamePlayer p : game.getPlayers()) {
            final InGameScoreboard score = new InGameScoreboard();
            score.createBoard(p, game);
        }
        this.game.getTeamManager().assignSpawnPositions();
        this.game.sendMessage("&6[!] You've been teleported.");
        this.game.sendMessage("&a[*] The game will begin in " + this.startIn + " seconds...");
        this.game.setMovementFrozen(true);
    }
    
    public void run() {
        if (this.startIn <= 1) {
            this.cancel();
            this.game.sendMessage("&a[!] The game has started.");
            this.game.setMovementFrozen(false);
            this.game.startGame();
            this.game.sendTitle("&4" + this.game.getDisplayName(), "&7by " + this.game.getMadeBy());
        }
        else {
            --this.startIn;
            if (this.startIn <= 5) {
                this.game.sendMessage("&c[*] The game will begin in " + this.startIn + " second" + ((this.startIn == 1) ? "" : "s"));
                this.game.getPlayers().forEach(p -> p.getPlayer().playSound(p.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 2.0f));
            }
        }
    }
}
