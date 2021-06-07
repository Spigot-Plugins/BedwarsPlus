package me.unldenis.bedwars.scoreboards;

import org.bukkit.plugin.Plugin;
import me.unldenis.bedwars.object.clans.Clan;
import me.unldenis.bedwars.util.MyScoreboard;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import me.unldenis.bedwars.Bedwars;

public class MainLobbyScoreboard
{
    private Bedwars main;
    private int taskID;
    
    public MainLobbyScoreboard(final Bedwars plugin) {
        this.main = plugin;
    }
    
    public void createBoard(final Player player) {
        final ScoreboardManager manager = Bukkit.getScoreboardManager();
        final Scoreboard board = manager.getNewScoreboard();
        final Objective obj = board.registerNewObjective("Bedwars-1", "dummy", ChatColor.translateAlternateColorCodes('&', "&7&l<< &e&lBedWars &7&l>>"));
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        final Score score = obj.getScore("");
        score.setScore(7);
        final Score score2 = obj.getScore("Clan: " + ChatColor.GREEN + "loading");
        score2.setScore(6);
        final Score score3 = obj.getScore("Players: " + ChatColor.GREEN + Bukkit.getOnlinePlayers().size());
        score3.setScore(5);
        final Score score4 = obj.getScore(" ");
        score4.setScore(4);
        final Score score5 = obj.getScore("Place: " + ChatColor.GREEN + "loading");
        score5.setScore(3);
        final Score score6 = obj.getScore("  ");
        score6.setScore(2);
        final Score score7 = obj.getScore(ChatColor.YELLOW + this.main.getMessagesData().getConfig().getString("scoreboards.server"));
        score7.setScore(1);
        player.getPlayer().setScoreboard(board);
    }
    
    public void startScoreboard_Lobby(final Player player) {
        this.taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask((Plugin)this.main, (Runnable)new Runnable() {
            int count = 0;
            Board board = new Board(player.getUniqueId());
            Scoreboard scoreboard = player.getScoreboard();
            Objective obj = this.scoreboard.getObjective("Bedwars-1");
            MyScoreboard myScr = new MyScoreboard();
            
            @Override
            public void run() {
                if (!this.board.hasID()) {
                    this.board.SetID(MainLobbyScoreboard.this.taskID);
                }
                if (MainLobbyScoreboard.this.main.getClanManager().hasClan(player)) {
                    final Clan cl = MainLobbyScoreboard.this.main.getClanManager().getClan(player);
                    this.myScr.replaceScore(this.obj, 6, "Clan: " + ChatColor.GREEN + cl.getNameClan());
                    this.myScr.replaceScore(this.obj, 3, "Place: " + ChatColor.GREEN + "#" + (MainLobbyScoreboard.this.main.getClanManager().sortClans().indexOf(cl) + 1));
                }
                else {
                    this.myScr.replaceScore(this.obj, 6, "Clan: " + ChatColor.GREEN + "\u1f6aB");
                    this.myScr.replaceScore(this.obj, 3, "Place: " + ChatColor.GREEN + "\u1f6aB");
                }
                this.myScr.replaceScore(this.obj, 5, "Players: " + ChatColor.GREEN + Bukkit.getOnlinePlayers().size());
                if (this.count == 10) {
                    this.count = 0;
                }
                switch (this.count) {
                    case 0: {
                        player.getScoreboard().getObjective(DisplaySlot.SIDEBAR).setDisplayName(ChatColor.translateAlternateColorCodes('&', "&7&l<< &e&lBedWars &7&l>>"));
                        break;
                    }
                    case 1: {
                        player.getScoreboard().getObjective(DisplaySlot.SIDEBAR).setDisplayName(ChatColor.translateAlternateColorCodes('&', "&7&l<< &f&lB&e&ledWars &7&l>>"));
                        break;
                    }
                    case 2: {
                        player.getScoreboard().getObjective(DisplaySlot.SIDEBAR).setDisplayName(ChatColor.translateAlternateColorCodes('&', "&7&l<< &e&lB&f&le&e&ldWars &7&l>>"));
                        break;
                    }
                    case 3: {
                        player.getScoreboard().getObjective(DisplaySlot.SIDEBAR).setDisplayName(ChatColor.translateAlternateColorCodes('&', "&7&l<< &e&lBe&f&ld&e&lWars &7&l>>"));
                        break;
                    }
                    case 4: {
                        player.getScoreboard().getObjective(DisplaySlot.SIDEBAR).setDisplayName(ChatColor.translateAlternateColorCodes('&', "&7&l<< &e&lBed&f&lW&e&lars &7&l>>"));
                        break;
                    }
                    case 5: {
                        player.getScoreboard().getObjective(DisplaySlot.SIDEBAR).setDisplayName(ChatColor.translateAlternateColorCodes('&', "&7&l<< &e&lBedW&f&la&e&lrs &7&l>>"));
                        break;
                    }
                    case 6: {
                        player.getScoreboard().getObjective(DisplaySlot.SIDEBAR).setDisplayName(ChatColor.translateAlternateColorCodes('&', "&7&l<< &e&lBedWa&f&lr&e&ls &7&l>>"));
                        break;
                    }
                    case 7: {
                        player.getScoreboard().getObjective(DisplaySlot.SIDEBAR).setDisplayName(ChatColor.translateAlternateColorCodes('&', "&7&l<< &e&lBedWar&f&ls &7&l>>"));
                        break;
                    }
                    case 8: {
                        player.getScoreboard().getObjective(DisplaySlot.SIDEBAR).setDisplayName(ChatColor.translateAlternateColorCodes('&', "&7&l<< &f&lBedWars &7&l>>"));
                        break;
                    }
                    case 9: {
                        player.getScoreboard().getObjective(DisplaySlot.SIDEBAR).setDisplayName(ChatColor.translateAlternateColorCodes('&', "&7&l<< &e&lBedWars &7&l>>"));
                        break;
                    }
                }
                ++this.count;
            }
        }, 0L, 10L);
    }
}
