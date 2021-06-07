
package me.unldenis.bedwars.object.teams;

import org.bukkit.entity.ArmorStand;
import org.bukkit.Material;
import org.bukkit.World;
import java.util.stream.Collectors;
import me.unldenis.bedwars.util.MyChat;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import me.unldenis.bedwars.object.GamePlayer;
import java.util.ArrayList;
import me.unldenis.bedwars.object.Game;
import java.util.List;
import me.unldenis.bedwars.Bedwars;

public class TeamManager
{
    private Bedwars main;
    private final String arena;
    private List<Team> teams;
    private Game game;
    
    public TeamManager(final Bedwars bedwars, final Game game, final String arena) {
        this.main = bedwars;
        this.arena = arena;
        this.teams = new ArrayList<Team>();
        this.game = game;
        this.load();
    }
    
    public Team getTeamFromColor(final TeamType type) {
        for (final Team t : this.teams) {
            if (t.isTeam(type)) {
                return t;
            }
        }
        return null;
    }
    
    public Team getTeamFromPlayer(final GamePlayer player) {
        return this.getTeamFromColor(player.getTeamPlayer());
    }
    
    public Team getTeamByBedLocation(final Location loc) {
        for (final Team t : this.teams) {
            if (t.getBed_1().equals((Object)loc) || t.getBed_2().equals((Object)loc)) {
                return t;
            }
        }
        return null;
    }
    
    public boolean isTSet(final Team team) {
        return team.getPlayers().size() == this.game.getMaxPlayers() / this.teams.size();
    }
    
    public void TSelect(final Team team, final GamePlayer gamePlayer) {
        if (team.isPrivateTeam()) {
            gamePlayer.sendMessage(ChatColor.RED + this.main.getMessagesData().getConfig().getString("game.team-private-clan"));
            return;
        }
        if (this.isTSet(team)) {
            gamePlayer.sendMessage(ChatColor.RED + this.main.getMessagesData().getConfig().getString("game.team-player-full"));
            return;
        }
        if (this.getTeamFromPlayer(gamePlayer) != null) {
            this.getTeamFromPlayer(gamePlayer).removePlayer(gamePlayer);
        }
        gamePlayer.setTeamPlayer(team.getTeamType());
        team.addPlayer(gamePlayer);
        String s = this.main.getMessagesData().getConfig().getString("game.team-player-joined");
        s = s.replace("%team", team.toString());
        gamePlayer.sendMessage(ChatColor.AQUA + s);
        final MyChat myCht = new MyChat();
        gamePlayer.getPlayer().setPlayerListName(ChatColor.DARK_GRAY + "[" + myCht.prefix(gamePlayer.getTeamPlayer()) + gamePlayer.getTeamPlayer() + ChatColor.DARK_GRAY + "] " + ChatColor.WHITE + gamePlayer.getName());
    }
    
    public void autoTeam() {
        final int maxSpace = this.game.getMaxPlayers() / this.teams.size();
        for (final GamePlayer p : this.game.getPlayers()) {
            boolean hasTeam = false;
            for (final Team team : this.teams) {
                if (team.getPlayers().contains(p)) {
                    hasTeam = true;
                    break;
                }
            }
            if (!hasTeam) {
                for (final Team team : this.teams) {
                    if (team.getPlayers().size() < maxSpace && !team.isPrivateTeam()) {
                        this.TSelect(team, p);
                        break;
                    }
                }
            }
        }
        if (this.game.getPlayers().size() <= maxSpace) {
            Team tm = null;
            for (final Team t : this.teams) {
                if (this.game.getPlayers().size() == t.getPlayers().size()) {
                    tm = t;
                }
            }
            if (tm == null) {
                return;
            }
            for (final Team t : this.teams) {
                if (!t.equals(tm) && !t.isPrivateTeam()) {
                    this.TSelect(t, this.game.getPlayers().get(0));
                    break;
                }
            }
        }
    }
    
    public void assignSpawnPositions() {
        for (final Team tm : this.teams) {
            tm.teleportToSpawn();
        }
    }
    
    public List<Team> remainsOneTeam() {
        final List<Team> temp = this.teams.stream().filter(t -> !t.getPlayers().isEmpty()).collect(Collectors.toList());
        return temp;
    }
    
    public Team getBestTeam() {
        final List<Team> temp = this.teams.stream().filter(t -> !t.isPrivateTeam()).sorted((t1, t2) -> Integer.compare(t1.getPlayers().size(), t2.getPlayers().size())).collect(Collectors.toList());
        return temp.get(0);
    }
    
    public World getWorld() {
        return this.teams.get(0).getSpawn().getWorld();
    }
    
    public void reload(final String arena) {
        for (final Team t : this.teams) {
            t.setSpawn(this.main.getArenasData().getConfig().getLocation("arenas." + arena + ".locations.teams." + t.toString().toLowerCase() + ".spawn"));
            t.setBed_1(this.main.getArenasData().getConfig().getLocation("arenas." + arena + ".locations.teams." + t.toString().toLowerCase() + ".bed-1"));
            t.setBed_2(this.main.getArenasData().getConfig().getLocation("arenas." + arena + ".locations.teams." + t.toString().toLowerCase() + ".bed-2"));
        }
    }
    
    private void load() {
        if (this.main.getArenasData().getConfig().getConfigurationSection("arenas." + this.arena + ".locations.teams") != null) {
            for (final String gameName : this.main.getArenasData().getConfig().getConfigurationSection("arenas." + this.arena + ".locations.teams").getKeys(false)) {
                switch ( gameName) {
                    case "yellow": {
                        this.teams.add(new Team(TeamType.Yellow, null, null, null));
                        continue;
                    }
                    case "red": {
                        this.teams.add(new Team(TeamType.Red, null, null, null));
                        continue;
                    }
                    case "blue": {
                        this.teams.add(new Team(TeamType.Blue, null, null, null));
                        continue;
                    }
                    case "cyan": {
                        this.teams.add(new Team(TeamType.Cyan, null, null, null));
                        continue;
                    }
                    case "pink": {
                        this.teams.add(new Team(TeamType.Pink, null, null, null));
                        continue;
                    }
                    case "black": {
                        this.teams.add(new Team(TeamType.Black, null, null, null));
                        continue;
                    }
                    case "green": {
                        this.teams.add(new Team(TeamType.Green, null, null, null));
                        continue;
                    }
                    case "white": {
                        this.teams.add(new Team(TeamType.White, null, null, null));
                        continue;
                    }
                    default: {
                        continue;
                    }
                }
            }
            this.reload(this.arena);
        }
        else {
            this.main.getLogger().warning("No teams of arena <" + this.arena + "> have been created. Please create one using the creation command.");
        }
    }
    
    public Game getGame() {
        return this.game;
    }
    
    public List<Team> getTeams() {
        return this.teams;
    }
    
    public static TeamType teamFromMaterial(final Material type) {
        switch (type) {
            case RED_WOOL: {
                return TeamType.Red;
            }
            case BLUE_WOOL: {
                return TeamType.Blue;
            }
            case GREEN_WOOL: {
                return TeamType.Green;
            }
            case YELLOW_WOOL: {
                return TeamType.Yellow;
            }
            case WHITE_WOOL: {
                return TeamType.White;
            }
            case BLACK_WOOL: {
                return TeamType.Black;
            }
            case PINK_WOOL: {
                return TeamType.Pink;
            }
            case CYAN_WOOL: {
                return TeamType.Cyan;
            }
            default: {
                return null;
            }
        }
    }
    
    public static TeamType teamFromArmorstand(final ArmorStand armorstand) {
        final MyChat myCht = new MyChat();
        final String color = myCht.getStrippedText(armorstand.getCustomName()).toLowerCase();
        System.out.println(color);
        switch (color) {
            case "yellow": {
                return TeamType.Yellow;
            }
            case "red": {
                return TeamType.Red;
            }
            case "blue": {
                return TeamType.Blue;
            }
            case "cyan": {
                return TeamType.Cyan;
            }
            case "pink": {
                return TeamType.Pink;
            }
            case "black": {
                return TeamType.Black;
            }
            case "green": {
                return TeamType.Green;
            }
            case "white": {
                return TeamType.White;
            }
            default:
                break;
        }
        return null;
    }
}
