package me.unldenis.bedwars.tasks;

import org.bukkit.GameMode;
import me.unldenis.bedwars.object.Game;
import org.bukkit.scheduler.BukkitRunnable;

public class BackToLobbyTask extends BukkitRunnable
{
    private Game game;
    private int startIn;
    
    public BackToLobbyTask(final Game game) {
        this.startIn = 10;
        (this.game = game).setGameState(Game.GameState.ENDING);
        game.getPlayers().forEach(p -> {
            p.getPlayer().setGameMode(GameMode.SPECTATOR);
            p.getPlayer().getInventory().clear();
            return;
        });
        this.game.sendMessage("&6[!] You've been teleported.");
        this.game.sendMessage("&a[*] The game will end in " + this.startIn + " seconds...");
    }
    
    public void run() {
        if (this.startIn <= 1) {
            this.cancel();
            this.game.sendMessage("&a[!] The game has ended.");
            this.game.endGame();
        }
        else {
            --this.startIn;
            if (this.startIn <= 5) {
                this.game.sendMessage("&c[*] The game will end in " + this.startIn + " second" + ((this.startIn == 1) ? "" : "s"));
            }
        }
    }
}
