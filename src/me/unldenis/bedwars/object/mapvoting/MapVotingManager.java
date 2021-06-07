package me.unldenis.bedwars.object.mapvoting;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.HashSet;
import java.util.ArrayList;
import me.unldenis.bedwars.Bedwars;
import me.unldenis.bedwars.object.Game;
import java.util.List;

public class MapVotingManager
{
    List<Game> games;
    List<String> waitingGames;
    private Bedwars main;
    
    public MapVotingManager(final Bedwars main) {
        this.games = new ArrayList<Game>();
        this.waitingGames = new ArrayList<String>();
        this.main = main;
    }
    
    public List<Game> find() {
        this.games.clear();
        final Set<Integer> duplicates = new HashSet<Integer>();
        final Set<Integer> teamNames = new HashSet<Integer>();
        if (this.main.getArenasData().getConfig().getConfigurationSection("arenas") != null) {
            for (final String gameName : this.main.getArenasData().getConfig().getConfigurationSection("arenas").getKeys(false)) {
                duplicates.add(this.main.getArenasData().getConfig().getInt("arenas." + gameName + ".maxPlayers"));
                teamNames.add(this.main.getArenasData().getConfig().getConfigurationSection("arenas." + gameName + ".locations.teams").getKeys(false).size());
            }
            for (final Integer k : duplicates) {
                for (final Integer tn : teamNames) {
                    final List<String> gamesNames = (List<String>)this.main.getArenasData().getConfig().getConfigurationSection("arenas").getKeys(false).stream().filter(a -> this.main.getArenasData().getConfig().getInt("arenas." + a + ".maxPlayers") == k).filter(a -> this.main.getArenasData().getConfig().getConfigurationSection("arenas." + a + ".locations.teams").getKeys(false).size() == tn).collect(Collectors.toList());
                    if (gamesNames.size() < 2) {
                        this.main.getLogger().warning("There are not enough games to create MapVoting games.");
                    }
                    else {
                        if (gamesNames.size() % 2 == 1) {
                            this.main.getLogger().warning("Arena " + gamesNames.get(gamesNames.size() - 1) + " is not playable at the moment.");
                            this.waitingGames.add(gamesNames.get(gamesNames.size() - 1));
                            gamesNames.remove(gamesNames.size() - 1);
                        }
                        for (int j = 0; j < gamesNames.size(); j += 2) {
                            this.games.add(new Game(this.main, gamesNames.get(j), gamesNames.get(j + 1)));
                        }
                        gamesNames.clear();
                    }
                }
            }
            return this.games;
        }
        this.main.getLogger().warning("No games was found. Please create one using the creation command.");
        return null;
    }
    
    public void addAndUpdateGames(final String name) {
        this.waitingGames.add(name);
        final Set<Integer> duplicates = new HashSet<Integer>();
        final Set<Integer> teamNames = new HashSet<Integer>();
        this.waitingGames.stream().forEach(a -> {
        	duplicates.add(this.main.getArenasData().getConfig().getInt("arenas." + a + ".maxPlayers"));
        	teamNames.add(this.main.getArenasData().getConfig().getConfigurationSection("arenas." + a + ".locations.teams").getKeys(false).size());
            return;
        });
        for (final Integer k : duplicates) {
            for (final Integer tn : teamNames) {
                final List<String> gamesNames = this.waitingGames.stream().filter(a -> this.main.getArenasData().getConfig().getInt("arenas." + a + ".maxPlayers") == k).filter(a -> this.main.getArenasData().getConfig().getConfigurationSection("arenas." + a + ".locations.teams").getKeys(false).size() == tn).collect(Collectors.toList());
                if (gamesNames.size() < 2) {
                    continue;
                }
                if (gamesNames.size() % 2 == 1) {
                    gamesNames.remove(gamesNames.size() - 1);
                }
                for (int j = 0; j < gamesNames.size(); j += 2) {
                    this.games.add(new Game(this.main, gamesNames.get(j), gamesNames.get(j + 1)));
                    this.waitingGames.remove(gamesNames.get(j));
                    this.waitingGames.remove(gamesNames.get(j + 1));
                }
                gamesNames.clear();
            }
        }
        this.listGames();
    }
    
    public void listGames() {
        for (final Game g : this.games) {
            this.main.getLogger().warning("MapVoting# -> Arena1: " + g.getMapVoting().getArena_1() + "; Arena2: " + g.getMapVoting().getArena_2());
        }
    }
}
