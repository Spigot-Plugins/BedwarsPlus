package me.unldenis.bedwars.scoreboards;

import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import me.unldenis.bedwars.util.MyChat;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.ChatColor;
import org.bukkit.Bukkit;
import me.unldenis.bedwars.object.Game;
import me.unldenis.bedwars.object.GamePlayer;

public class InGameScoreboard
{
    public void createBoard(final GamePlayer player, final Game game) {
        final ScoreboardManager manager = Bukkit.getScoreboardManager();
        final Scoreboard board = manager.getNewScoreboard();
        final Objective obj = board.registerNewObjective("Bedwars-1", "dummy", ChatColor.translateAlternateColorCodes('&', "&7&l<< &e&lBedWars &7&l>>"));
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        final int teamSize = game.getTeamManager().getTeams().size();
        final Score score = obj.getScore("");
        score.setScore(7 + (teamSize - 1));
        final Score score2 = obj.getScore("Map: " + ChatColor.GREEN + game.getDisplayName());
        score2.setScore(6 + (teamSize - 1));
        final Score score3 = obj.getScore("Players: " + ChatColor.GREEN + game.getPlayers().size() + "/" + game.getMaxPlayers());
        score3.setScore(5 + (teamSize - 1));
        final Score score4 = obj.getScore(" ");
        score4.setScore(4 + (teamSize - 1));
        final MyChat myCh = new MyChat();
        for (int j = 0; j < teamSize; ++j) {
            if (game.getTeamManager().getTeams().get(j).getPlayers().contains(player)) {
                final Score score5 = obj.getScore(ChatColor.GREEN + "\u2764 " + myCh.prefix(player.getTeamPlayer()) + player.getTeamPlayer().toString() + ChatColor.GRAY + " YOU");
                score5.setScore(3 + j);
            }
            else {
                final Score score5 = obj.getScore(ChatColor.GREEN + "\u2764 " + myCh.prefix(game.getTeamManager().getTeams().get(j).getTeamType()) + game.getTeamManager().getTeams().get(j).toString());
                score5.setScore(3 + j);
            }
        }
        final Score score6 = obj.getScore("  ");
        score6.setScore(2);
        final Score score7 = obj.getScore(ChatColor.YELLOW + game.getMain().getMessagesData().getConfig().getString("scoreboards.server"));
        score7.setScore(1);
        player.getPlayer().setScoreboard(board);
    }
}
