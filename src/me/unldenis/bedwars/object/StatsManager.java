
package me.unldenis.bedwars.object;

import org.bukkit.Bukkit;
import java.util.UUID;
import java.util.ArrayList;
import org.bukkit.OfflinePlayer;
import java.util.List;
import me.unldenis.bedwars.Bedwars;

public class StatsManager
{
    private Bedwars main;
    private final int KILL_POINTS;
    private final int WIN_POINTS;
    private final int DEATH_POINTS;
    
    public StatsManager(final Bedwars plugin) {
        this.main = plugin;
        this.KILL_POINTS = this.main.getStatsData().getConfig().getInt("points.kill");
        this.WIN_POINTS = this.main.getStatsData().getConfig().getInt("points.win");
        this.DEATH_POINTS = this.main.getStatsData().getConfig().getInt("points.death");
    }
    
    public int getWIN_POINTS() {
        return this.WIN_POINTS;
    }
    
    public int getKILL_POINTS() {
        return this.KILL_POINTS;
    }
    
    public int getDEATH_POINTS() {
        return this.DEATH_POINTS;
    }
    
    public void updateStats(final Game game) {
        for (final GamePlayer gm : game.getPlayers()) {
            game.getMain().getStatsData().getConfig().set("stats." + gm.getPlayer().getUniqueId() + ".score", (Object)gm.getScorePlayer());
        }
        game.getMain().getStatsData().saveConfig();
    }
    
    public List<OfflinePlayer> sortPlayers() {
        final List<OfflinePlayer> players = new ArrayList<OfflinePlayer>();
        if (this.main.getStatsData().getConfig().getConfigurationSection("stats") != null) {
            for (final String uuid : this.main.getStatsData().getConfig().getConfigurationSection("stats").getKeys(false)) {
                players.add(Bukkit.getOfflinePlayer(UUID.fromString(uuid)));
            }
        }
        players.sort((p2, p1) -> Integer.compare(this.main.getStatsData().getConfig().getInt("stats." + p1.getUniqueId() + ".score"), this.main.getStatsData().getConfig().getInt("stats." + p2.getUniqueId() + ".score")));
        return players;
    }
    
    public int getScore(final OfflinePlayer player) {
        return this.main.getStatsData().getConfig().getInt("stats." + player.getUniqueId() + ".score");
    }
}
