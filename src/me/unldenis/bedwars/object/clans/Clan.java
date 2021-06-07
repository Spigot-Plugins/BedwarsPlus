
package me.unldenis.bedwars.object.clans;

import org.bukkit.OfflinePlayer;
import net.md_5.bungee.api.ChatColor;
import me.unldenis.bedwars.object.GamePlayer;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import me.unldenis.bedwars.object.teams.Team;
import me.unldenis.bedwars.object.Game;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import me.unldenis.bedwars.Bedwars;

public class Clan
{
    private final Bedwars main;
    private final String nameClan;
    private final UUID founder;
    private List<UUID> players;
    private ClanState clanState;
    private int score;
    
    public Clan(final Bedwars main, final UUID founder, final String name, final int score) {
        this.players = new ArrayList<UUID>();
        this.clanState = ClanState.INLOBBY;
        this.main = main;
        this.founder = founder;
        this.nameClan = name;
        this.players.add(founder);
        this.score = score;
    }
    
    public Clan(final Bedwars main, final UUID founder, final List<UUID> players, final String name, final int score) {
        this.players = new ArrayList<UUID>();
        this.clanState = ClanState.INLOBBY;
        this.main = main;
        this.founder = founder;
        this.nameClan = name;
        this.players = players;
        this.score = score;
    }
    
    public void followFounder(final Game game, final Team team) {
        final List<Player> p = this.getOnlinePlayers();
        p.remove(Bukkit.getPlayer(this.founder));
        for (int j = 0; j < Math.min(p.size(), game.getMaxPlayers() / game.getTeamManager().getTeams().size()); ++j) {
            final GamePlayer gp = new GamePlayer(p.get(j), this.main);
            game.joinGame(gp);
            game.getTeamManager().TSelect(team, gp);
        }
    }
    
    public void listMembers(final Player p) {
        for (final UUID uuid : this.players) {
            p.sendMessage(new StringBuilder().append(ChatColor.AQUA).append(ChatColor.BOLD).append("CLAN Members -> ").append(ChatColor.GRAY).append(Bukkit.getOfflinePlayer(uuid).getName()).toString());
        }
    }
    
    public void addWin() {
        this.score += this.main.getClansData().getConfig().getInt("points.win");
        this.main.getClansData().getConfig().set("clans." + this.nameClan + ".score", (Object)this.score);
        this.main.getClansData().saveConfig();
    }
    
    public void addLose() {
        this.score -= this.main.getClansData().getConfig().getInt("points.lose");
        this.main.getClansData().getConfig().set("clans." + this.nameClan + ".score", (Object)this.score);
        this.main.getClansData().saveConfig();
    }
    
    public void sendRequestToPlayer(final Player me, final Player other) {
        this.main.getClanManager().sendRequest(other, this);
        String s = this.main.getMessagesData().getConfig().getString("clan.player-request");
        s = s.replace("%clan", this.getNameClan());
        s = s.replace("%player", me.getDisplayName());
        other.sendMessage(new StringBuilder().append(ChatColor.AQUA).append(ChatColor.BOLD).append("CLAN -> ").append(ChatColor.GRAY).append(s).toString());
        other.sendMessage(new StringBuilder().append(ChatColor.AQUA).append(ChatColor.BOLD).append("CLAN -> ").append(ChatColor.GRAY).append("Type '/clan accept' to join the clan.").toString());
    }
    
    public void acceptRequest(final Player player) {
        this.main.getClanManager().setClan(player, this.main.getClanManager().getClanRequest(player));
        this.main.getClanManager().sendRequest(player, null);
        final List<String> l = (List<String>)this.main.getClansData().getConfig().getStringList("clan." + this.nameClan + ".members");
        l.add(player.getUniqueId().toString());
        this.main.getClansData().getConfig().set("clans." + this.nameClan + ".members", (Object)l);
        this.main.getClansData().saveConfig();
        this.players.add(player.getUniqueId());
        String s = this.main.getMessagesData().getConfig().getString("clan.player-joined");
        s = s.replace("%player", player.getDisplayName());
        this.sendMessage(s);
    }
    
    public void kickPlayer(final Player player) {
        this.main.getClanManager().setClan(player, null);
        final List<String> l = (List<String>)this.main.getClansData().getConfig().getStringList("clans." + this.nameClan + ".members");
        l.remove(player.getUniqueId().toString());
        this.main.getClansData().getConfig().set("clans." + this.nameClan + ".members", (Object)l);
        this.main.getClansData().saveConfig();
        String s = this.main.getMessagesData().getConfig().getString("clan.player-kicked");
        s = s.replace("%player", player.getDisplayName());
        this.sendMessage(s);
        this.players.remove(player.getUniqueId());
    }
    
    public void sendMessage(final String msg) {
        for (final Player p : this.getOnlinePlayers()) {
            p.sendMessage("");
            p.sendMessage(new StringBuilder().append(ChatColor.AQUA).append(ChatColor.BOLD).append("[").append(this.nameClan).append("] -> ").append(ChatColor.GRAY).append(msg).toString());
        }
    }
    
    public List<Player> getOnlinePlayers() {
        final List<Player> onlinePlayers = new ArrayList<Player>();
        for (final UUID uuid : this.players) {
            if (Bukkit.getPlayer(uuid) != null) {
                onlinePlayers.add(Bukkit.getPlayer(uuid));
            }
        }
        return onlinePlayers;
    }
    
    public List<OfflinePlayer> getPlayers() {
        final List<OfflinePlayer> allplayers = new ArrayList<OfflinePlayer>();
        for (final UUID uuid : this.players) {
            allplayers.add(Bukkit.getOfflinePlayer(uuid));
        }
        return allplayers;
    }
    
    public boolean isFounder(final Player player) {
        return player.getUniqueId().equals(this.founder);
    }
    
    public String getNameClan() {
        return this.nameClan;
    }
    
    public UUID getFounder() {
        return this.founder;
    }
    
    public ClanState getClanState() {
        return this.clanState;
    }
    
    public void setClanState(final ClanState clanState) {
        this.clanState = clanState;
    }
    
    public boolean isState(final ClanState clanState) {
        return clanState.equals(clanState);
    }
    
    public int getScore() {
        return this.score;
    }
}
