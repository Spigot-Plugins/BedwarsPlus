
package me.unldenis.bedwars.object.mapvoting;

import net.md_5.bungee.api.ChatColor;
import java.util.HashSet;
import me.unldenis.bedwars.object.Game;
import me.unldenis.bedwars.object.GamePlayer;
import java.util.Set;
import me.unldenis.bedwars.Bedwars;

public class MapVoting
{
    private Bedwars main;
    private final String arena_1;
    private final String arena_2;
    private Set<GamePlayer> vote_1;
    private Set<GamePlayer> vote_2;
    private Game game;
    
    public MapVoting(final Bedwars bedwars, final Game game, final String arena_1, final String arena_2) {
        this.main = bedwars;
        this.game = game;
        this.arena_1 = arena_1;
        this.arena_2 = arena_2;
        this.vote_1 = new HashSet<GamePlayer>();
        this.vote_2 = new HashSet<GamePlayer>();
    }
    
    public void loadWinMap() {
        if (this.vote_1.size() > this.vote_2.size()) {
            this.game.loadMapFromConfig(this.arena_1);
            this.main.getGameManager().getMapVotingManager().addAndUpdateGames(this.arena_2);
            String s = this.main.getMessagesData().getConfig().getString("game.map-won-mapvoting");
            s = s.replace("%arena", this.arena_1);
            s = s.replace("%diff", new StringBuilder().append(this.vote_1.size() - this.vote_2.size()).toString());
            this.game.sendMessage(s);
        }
        else if (this.vote_1.size() < this.vote_2.size()) {
            this.game.loadMapFromConfig(this.arena_2);
            this.main.getGameManager().getMapVotingManager().addAndUpdateGames(this.arena_1);
            String s = this.main.getMessagesData().getConfig().getString("game.map-won-mapvoting");
            s = s.replace("%arena", this.arena_2);
            s = s.replace("%diff", new StringBuilder().append(this.vote_2.size() - this.vote_1.size()).toString());
            this.game.sendMessage(s);
        }
        else {
            this.game.sendMessage("The vote ended in a draw");
            final int n = (int)Math.round(Math.random());
            if (n == 0) {
                this.game.loadMapFromConfig(this.arena_1);
                this.main.getGameManager().getMapVotingManager().addAndUpdateGames(this.arena_2);
                String s2 = this.main.getMessagesData().getConfig().getString("game.map-won-random-mapvoting");
                s2 = s2.replace("%arena", this.arena_1);
                this.game.sendMessage(s2);
            }
            else if (n == 1) {
                this.game.loadMapFromConfig(this.arena_2);
                this.main.getGameManager().getMapVotingManager().addAndUpdateGames(this.arena_1);
                String s2 = this.main.getMessagesData().getConfig().getString("game.map-won-random-mapvoting");
                s2 = s2.replace("%arena", this.arena_2);
                this.game.sendMessage(s2);
            }
        }
    }
    
    public void MSelect(final boolean first, final GamePlayer gamePlayer) {
        if (first) {
            try {
                if (this.vote_2.contains(gamePlayer)) {
                    this.vote_2.remove(gamePlayer);
                }
            }
            catch (NullPointerException ex) {}
            this.vote_1.add(gamePlayer);
            String s = this.main.getMessagesData().getConfig().getString("game.vote-map");
            s = s.replace("%arena", this.arena_1);
            gamePlayer.sendMessage(ChatColor.AQUA + s);
        }
        else {
            try {
                if (this.vote_1.contains(gamePlayer)) {
                    this.vote_1.remove(gamePlayer);
                }
            }
            catch (NullPointerException ex2) {}
            this.vote_2.add(gamePlayer);
            String s = this.main.getMessagesData().getConfig().getString("game.vote-map");
            s = s.replace("%arena", this.arena_2);
            gamePlayer.sendMessage(ChatColor.AQUA + s);
        }
    }
    
    public String getArena_1() {
        return this.arena_1;
    }
    
    public String getArena_2() {
        return this.arena_2;
    }
    
    public Set<GamePlayer> getVote_1() {
        return this.vote_1;
    }
    
    public Set<GamePlayer> getVote_2() {
        return this.vote_2;
    }
}
