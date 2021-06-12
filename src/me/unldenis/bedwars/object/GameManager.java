package me.unldenis.bedwars.object;


import java.util.stream.Collectors;
import java.util.Random;
import java.util.Set;
import org.bukkit.World;
import java.util.HashMap;
import java.util.ArrayList;
import me.unldenis.bedwars.Bedwars;
import me.unldenis.bedwars.object.mapvoting.MapVotingManager;
import org.bukkit.entity.Player;
import java.util.Map;
import java.util.List;

public class GameManager
{
    private List<Game> games;
    private Map<Player, Game> playerGameMap;
    private MapVotingManager mvm;
    private Bedwars main;
    
    public GameManager(final Bedwars bedwars) {
        this.games = new ArrayList<Game>();
        this.playerGameMap = new HashMap<Player, Game>();
        this.main = bedwars;
    }
    
    public void LoadGames() {
        if (this.main.getConfigData().getConfig().getBoolean("map-voting")) {
            this.mvm = new MapVotingManager(this.main);
            this.games = this.mvm.find();
            this.mvm.listGames();
            return;
        }
        if (this.main.getArenasData().getConfig().getConfigurationSection("arenas") != null) {
            for (final String gameName : this.main.getArenasData().getConfig().getConfigurationSection("arenas").getKeys(false)) {
                final Game game = new Game(this.main, gameName);
                this.games.add(game);
            }
            this.main.getLogger().warning("Games loaded: " + this.games.size());
        }
        else {
            this.main.getLogger().warning("No games have been created. Please create one using the creation command.");
        }
    }
    
    public void UnloadGames() {
        try {
            for (final Game game : this.games) {
                game.endGame();
            }
        }
        catch (Exception ex) {}
        this.games.clear();
    }
    
    public Game getGame(final String gameName) {
        for (final Game game : this.games) {
            if (game.getDisplayName().equalsIgnoreCase(gameName)) {
                return game;
            }
        }
        return null;
    }
    
    public Game getGame(final World w) {
        for (final Game game : this.games) {
            if (game.getWorld().equals(w)) {
                return game;
            }
        }
        return null;
    }
    
    public Game getGame(final Player player) {
        return this.playerGameMap.get(player);
    }
    
    public void setGame(final Player player, final Game game) {
        if (game == null) {
            this.playerGameMap.remove(player);
        }
        else {
            this.playerGameMap.put(player, game);
        }
    }
    
    public boolean isInGame(final Player p) {
        return this.playerGameMap.containsKey(p);
    }
    
    public Set<Player> getPlayersInGame() {
        return this.playerGameMap.keySet();
    }
    
    public Game getRandomGame(final GamePlayer gamePlayer) {
    	if(games.isEmpty()) return null;
        if (this.main.getConfigData().getConfig().getBoolean("matchmaking")) {
            final Game game = this.games.stream().sorted((o1, o2) -> o1.getMedPlayersScore(gamePlayer).compareTo(o2.getMedPlayersScore(gamePlayer))).findFirst().get();
            return game;
        }
        return this.games.get(new Random().nextInt(this.games.size()));
    }
    
    public Game getTeamSizeGame(final int n, final GamePlayer gamePlayer) {
        if (this.main.getConfigData().getConfig().getBoolean("matchmaking")) {
            final List<Game> temp = this.games.stream().
            		filter(g -> g.isState(Game.GameState.LOBBY) || g.isState(Game.GameState.STARTING)).
            		filter(g -> g.getMaxPlayers() / g.getTeamManager().getTeams().size() == n).
            		sorted((o1, o2) -> o1.getMedPlayersScore(gamePlayer).compareTo(o2.getMedPlayersScore(gamePlayer))).
            		collect(Collectors.toList());
            return temp.get(0);
        }
        for (final Game game : this.games) {
            if ((game.isState(Game.GameState.LOBBY) || game.isState(Game.GameState.STARTING)) && game.getMaxPlayers() / game.getTeamManager().getTeams().size() == n) {
                return game;
            }
        }
        return null;
    }
    
    public List<Game> getGames() {
        return this.games;
    }
    
    public MapVotingManager getMapVotingManager() {
        return this.mvm;
    }
}
