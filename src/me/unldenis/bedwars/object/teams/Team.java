
package me.unldenis.bedwars.object.teams;

import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import java.util.ArrayList;
import me.unldenis.bedwars.object.GamePlayer;
import java.util.List;
import org.bukkit.Location;

public class Team
{
    private final TeamType type;
    private Location spawn;
    private Location bed_1;
    private Location bed_2;
    private boolean privateTeam;
    private List<GamePlayer> players;
    private BedState bedState;
    
    public Team(final TeamType type, final Location spawn, final Location bed_1, final Location bed_2) {
        this.type = type;
        this.players = new ArrayList<GamePlayer>();
        this.spawn = spawn;
        this.setBed_1(bed_1);
        this.setBed_2(bed_2);
        this.setBedState(BedState.UNBROKEN);
        this.privateTeam = false;
    }
    
    public void sendMessage(final String message) {
        for (final GamePlayer p : this.players) {
            p.sendMessage(message);
        }
    }
    
    public void teleportToSpawn() {
        for (final GamePlayer p : this.players) {
            p.teleport(this.spawn);
            p.getPlayer().setGameMode(GameMode.SURVIVAL);
            p.getPlayer().getWorld().setDifficulty(Difficulty.PEACEFUL);
            p.getPlayer().getInventory().clear();
        }
    }
    
    public void addPlayer(final GamePlayer p) {
        if (this.players.contains(p)) {
            return;
        }
        this.players.add(p);
    }
    
    public void removePlayer(final GamePlayer p) {
        if (!this.players.contains(p)) {
            return;
        }
        this.players.remove(p);
    }
    
    public boolean isTeam(final TeamType team) {
        return this.type.equals(team);
    }
    
    public BedState getBedState() {
        return this.bedState;
    }
    
    public void setBedState(final BedState bedState) {
        this.bedState = bedState;
    }
    
    public List<GamePlayer> getPlayers() {
        return this.players;
    }
    
    public TeamType getTeamType() {
        return this.type;
    }
    
    @Override
    public String toString() {
        return this.type.toString();
    }
    
    public Location getBed_1() {
        return this.bed_1;
    }
    
    public void setBed_1(final Location bed_1) {
        this.bed_1 = bed_1;
    }
    
    public Location getBed_2() {
        return this.bed_2;
    }
    
    public void setBed_2(final Location bed_2) {
        this.bed_2 = bed_2;
    }
    
    public Location getSpawn() {
        return this.spawn;
    }
    
    public void setSpawn(final Location loc) {
        this.spawn = loc;
    }
    
    public boolean isPrivateTeam() {
        return this.privateTeam;
    }
    
    public void setPrivateTeam(final boolean privateTeam) {
        this.privateTeam = privateTeam;
    }
}
