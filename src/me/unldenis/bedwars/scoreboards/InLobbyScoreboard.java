package me.unldenis.bedwars.scoreboards;

import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.ChatColor;
import org.bukkit.Bukkit;
import me.unldenis.bedwars.object.Game;
import me.unldenis.bedwars.object.GamePlayer;

public class InLobbyScoreboard
{
    public void createBoard(final GamePlayer player, final Game game) {
        final ScoreboardManager manager = Bukkit.getScoreboardManager();
        final Scoreboard board = manager.getNewScoreboard();
        final Objective obj = board.registerNewObjective("Bedwars-1", "dummy", ChatColor.translateAlternateColorCodes('&', "&7&l<< &e&lBedWars &7&l>>"));
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        final Score score = obj.getScore("");
        score.setScore(7);
        if (game.getMapVoting() == null) {
            final Score score2 = obj.getScore("Map: " + ChatColor.GREEN + game.getDisplayName());
            score2.setScore(6);
        }
        else {
            final Score score2 = obj.getScore("Map: " + ChatColor.GREEN + "MapVoting");
            score2.setScore(6);
        }
        final Score score3 = obj.getScore("Players: " + ChatColor.GREEN + game.getPlayers().size() + "/" + game.getMaxPlayers());
        score3.setScore(5);
        final Score score4 = obj.getScore(" ");
        score4.setScore(4);
        final Score score5 = obj.getScore("Waiting...");
        score5.setScore(3);
        final Score score6 = obj.getScore("  ");
        score6.setScore(2);
        final Score score7 = obj.getScore(ChatColor.YELLOW + game.getMain().getMessagesData().getConfig().getString("scoreboards.server"));
        score7.setScore(1);
        player.getPlayer().setScoreboard(board);
    }
}
