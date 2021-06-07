
package me.unldenis.bedwars.listeners;

import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import me.unldenis.bedwars.object.clans.Clan;
import org.bukkit.plugin.Plugin;
import me.unldenis.bedwars.tasks.BackToLobbyTask;
import me.unldenis.bedwars.object.GamePlayer;
import me.unldenis.bedwars.object.teams.Team;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import me.unldenis.bedwars.object.Game;
import me.unldenis.bedwars.scoreboards.Board;
import me.unldenis.bedwars.npc.PacketReader;
import me.unldenis.bedwars.npc.NPC;
import org.bukkit.event.player.PlayerQuitEvent;
import me.unldenis.bedwars.util.MyChat;
import me.unldenis.bedwars.Bedwars;
import org.bukkit.event.Listener;

public class PlayerLeave implements Listener
{
    private Bedwars main;
    private MyChat myCht;
    
    public PlayerLeave(final Bedwars bedwars) {
        this.main = bedwars;
        this.myCht = new MyChat();
    }
    
    @SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.HIGH)
    public void onPlayerQuit(final PlayerQuitEvent event) {
        event.setQuitMessage((String)null);
        final Player player = event.getPlayer();
        for (final NPC npc : NPC.getNPCs()) {
            npc.removeNPCPacket(player);
        }
        final PacketReader reader = new PacketReader(this.main);
        reader.uninject(player);
        final Board board2 = new Board(player.getUniqueId());
        if (board2.hasID()) {
            board2.stop();
        }
        final Game game = this.main.getGameManager().getGame(player);
        if (game != null && game.getGamePlayer(player) != null) {
            final GamePlayer gamePlayer = game.getGamePlayer(player);
            if (gamePlayer.getPlayer() == player) {
                if (!game.isState(Game.GameState.PLAYING)) {
                    game.getPlayers().remove(gamePlayer);
                    if (game.getMapVoting() != null) {
                        game.getMapVoting().getVote_1().remove(gamePlayer);
                        game.getMapVoting().getVote_2().remove(gamePlayer);
                    }
                    final Team tm = game.getTeamManager().getTeamFromPlayer(gamePlayer);
                    if (tm != null) {
                        tm.removePlayer(gamePlayer);
                        if (tm.isPrivateTeam()) {
                            final Clan cl = this.main.getClanManager().getClan(player);
                            if (cl.isFounder(player)) {
                                tm.setPrivateTeam(false);
                            }
                        }
                    }
                    String s = this.main.getMessagesData().getConfig().getString("game.quit-lobby");
                    s = s.replace("%player", "&7" + gamePlayer.getName() + "&6");
                    game.sendMessage(s);
                    final Board board3 = new Board(player.getUniqueId());
                    if (board3.hasID()) {
                        board3.stop();
                    }
                    player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
                    for (final Player p : this.main.getGameManager().getPlayersInGame()) {
                        player.showPlayer(p);
                        p.showPlayer(player);
                    }
                    player.getPlayer().setDisplayName(player.getName());
                    this.main.getGameManager().setGame(player, null);
                    return;
                }
                event.setQuitMessage((String)null);
                final Team tm = game.getTeamManager().getTeamFromPlayer(gamePlayer);
                String s = this.main.getMessagesData().getConfig().getString("game.player-quit");
                s = s.replace("%player", String.valueOf(this.myCht.prefix(tm.getTeamType())) + gamePlayer.getName() + "&6");
                game.sendMessage(s);
                tm.removePlayer(gamePlayer);
                if (tm.getPlayers().size() == 0 && tm.isPrivateTeam()) {
                    final Clan clan = this.main.getClanManager().getClan(gamePlayer.getPlayer());
                    clan.addLose();
                }
                if (game.getTeamManager().remainsOneTeam().size() == 1) {
                    String win_message = "";
                    final Team tmWinner = game.getTeamManager().remainsOneTeam().get(0);
                    for (int j = 0; j < tmWinner.getPlayers().size(); ++j) {
                        win_message = String.valueOf(win_message) + this.myCht.prefix(tmWinner.getTeamType()) + tmWinner.getPlayers().get(j).getName();
                        if (j != tmWinner.getPlayers().size() - 1) {
                            win_message = String.valueOf(win_message) + "&7, ";
                        }
                        tmWinner.getPlayers().get(j).addScore(this.main.getStatsManager().getWIN_POINTS());
                    }
                    if (tmWinner.isPrivateTeam()) {
                        this.main.getClanManager().getClan(tmWinner.getPlayers().get(0).getPlayer()).addWin();
                    }
                    win_message = String.valueOf(win_message) + " &a " + this.main.getMessagesData().getConfig().getString("game.won");
                    game.sendMessage(win_message);
                    game.sendTitle(String.valueOf(this.myCht.prefix(tmWinner.getTeamType())) + tmWinner + " &7team won", win_message);
                    new BackToLobbyTask(game).runTaskTimer((Plugin)this.main, 0L, 20L);
                }
            }
        }
    }
}
