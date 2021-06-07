package me.unldenis.bedwars.object;

import org.bukkit.Location;
import me.unldenis.bedwars.Bedwars;
import me.unldenis.bedwars.util.MyChat;
import me.unldenis.bedwars.object.teams.TeamType;
import org.bukkit.entity.Player;

public class GamePlayer
{
    private Player player;
    private TeamType teamPlayer;
    private int scorePlayer;
    private MyChat myCht;
    
    public GamePlayer(final Player player, final Bedwars plugin) {
        this.player = player;
        this.scorePlayer = plugin.getStatsData().getConfig().getInt("stats." + player.getUniqueId() + ".score");
        this.myCht = new MyChat();
    }
    
    public Player getPlayer() {
        return this.player;
    }
    
    public void sendMessage(final String message) {
        this.player.sendMessage(this.myCht.format(message));
    }
    
    public void teleport(final Location loc) {
        if (loc == null) {
            return;
        }
        this.getPlayer().teleport(loc);
    }
    
    public String getName() {
        return this.player.getDisplayName();
    }
    
    public TeamType getTeamPlayer() {
        return this.teamPlayer;
    }
    
    public void setTeamPlayer(final TeamType teamPlayer) {
        this.teamPlayer = teamPlayer;
    }
    
    public int getScorePlayer() {
        return this.scorePlayer;
    }
    
    public void setScorePlayer(final int scorePlayer) {
        this.scorePlayer = scorePlayer;
    }
    
    public void addScore(final int score) {
        this.scorePlayer += score;
    }
}
