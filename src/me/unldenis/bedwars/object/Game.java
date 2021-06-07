package me.unldenis.bedwars.object;

import org.bukkit.scoreboard.DisplaySlot;
import me.unldenis.bedwars.util.MyScoreboard;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import me.unldenis.bedwars.scoreboards.MainLobbyScoreboard;
import me.unldenis.bedwars.util.MyWorld;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import me.unldenis.bedwars.tasks.GameCountdownTask;
import org.bukkit.inventory.meta.ItemMeta;
import me.unldenis.bedwars.object.teams.Team;
import me.unldenis.bedwars.object.clans.Clan;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.Material;
import me.unldenis.bedwars.scoreboards.InLobbyScoreboard;
import me.unldenis.bedwars.util.MyItems;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.attribute.Attribute;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Bukkit;
import me.unldenis.bedwars.scoreboards.Board;
import net.md_5.bungee.api.ChatColor;
import java.util.HashSet;
import java.util.ArrayList;
import me.unldenis.bedwars.object.mapvoting.MapVoting;
import me.unldenis.bedwars.object.teams.TeamManager;
import java.util.List;
import org.bukkit.World;
import java.util.Set;
import org.bukkit.Location;
import me.unldenis.bedwars.Bedwars;

public class Game
{
    private Bedwars main;
    private String displayName;
    private String madeBy;
    private Location lobbyLoc;
    private Set<Location> bronzeLoc;
    private Set<Location> ironLoc;
    private Set<Location> goldLoc;
    private World world;
    private int minPlayers;
    private int maxPlayers;
    private List<GamePlayer> players;
    private GameState gameState;
    private Set<Location> placedBlocks;
    private TeamManager teamManager;
    private int taskID;
    private MapVoting mapVoting;
    
    public Game(final Bedwars main, final String name) {
        this.gameState = GameState.LOBBY;
        this.main = main;
        this.displayName = name;
        this.minPlayers = main.getArenasData().getConfig().getInt("arenas." + this.displayName + ".minPlayers");
        this.maxPlayers = main.getArenasData().getConfig().getInt("arenas." + this.displayName + ".maxPlayers");
        this.lobbyLoc = main.getArenasData().getConfig().getLocation("arenas." + this.displayName + ".locations.lobby");
        this.loadMapFromConfig(name);
        this.players = new ArrayList<GamePlayer>();
        this.placedBlocks = new HashSet<Location>();
    }
    
    public Game(final Bedwars main, final String arena_1, final String arena_2) {
        this.gameState = GameState.LOBBY;
        this.main = main;
        this.mapVoting = new MapVoting(main, this, arena_1, arena_2);
        this.minPlayers = main.getArenasData().getConfig().getInt("arenas." + arena_1 + ".minPlayers");
        this.maxPlayers = main.getArenasData().getConfig().getInt("arenas." + arena_1 + ".maxPlayers");
        this.lobbyLoc = main.getArenasData().getConfig().getLocation("map-voting.lobby");
        this.teamManager = new TeamManager(main, this, arena_1);
        this.players = new ArrayList<GamePlayer>();
        this.placedBlocks = new HashSet<Location>();
    }
    
    public boolean joinGame(final GamePlayer gamePlayer) {
        if (this.main.getGameManager().isInGame(gamePlayer.getPlayer())) {
            gamePlayer.sendMessage(ChatColor.RED + this.main.getMessagesData().getConfig().getString("game.already-in-game"));
            return false;
        }
        if (!this.isState(GameState.LOBBY) && !this.isState(GameState.STARTING)) {
            gamePlayer.sendMessage(ChatColor.RED + "At the moment the game is in the following status: " + this.getGameState().name());
            return false;
        }
        if (this.players.size() != this.maxPlayers) {
            this.players.add(gamePlayer);
            gamePlayer.teleport(this.lobbyLoc);
            this.sendMessage("&a[+] &6" + gamePlayer.getName() + " &7(" + this.getPlayers().size() + "&a/&7" + this.maxPlayers + ")");
            if (this.main.getClanManager().hasClan(gamePlayer.getPlayer())) {
                final Clan clan = this.main.getClanManager().getClan(gamePlayer.getPlayer());
                if (clan.isFounder(gamePlayer.getPlayer()) && clan.getOnlinePlayers().size() > 1) {
                    final Team team = this.getTeamManager().getBestTeam();
                    if (team.getPlayers().size() > 0) {
                        gamePlayer.sendMessage(ChatColor.RED + "Your clan members can't join because there aren't team available");
                    }
                    else {
                        this.teamManager.TSelect(team, gamePlayer);
                        clan.followFounder(this, team);
                        team.setPrivateTeam(true);
                    }
                }
            }
            final Board board = new Board(gamePlayer.getPlayer().getUniqueId());
            if (board.hasID()) {
                board.stop();
            }
            gamePlayer.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
            gamePlayer.getPlayer().getInventory().clear();
            gamePlayer.getPlayer().getInventory().setArmorContents((ItemStack[])null);
            gamePlayer.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20.0);
            gamePlayer.getPlayer().setGameMode(GameMode.ADVENTURE);
            gamePlayer.getPlayer().getWorld().setDifficulty(Difficulty.PEACEFUL);
            final MyItems myItm = new MyItems();
            final InLobbyScoreboard score = new InLobbyScoreboard();
            score.createBoard(gamePlayer, this);
            this.startScoreboard_Lobby(gamePlayer);
            ItemStack testEnchant = new ItemStack(Material.NETHER_STAR, 1);
            ItemMeta meta = testEnchant.getItemMeta();
            meta.addEnchant(Enchantment.LURE, 1, false);
            meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ENCHANTS });
            testEnchant.setItemMeta(meta);
            gamePlayer.getPlayer().getInventory().setItem(0, myItm.nameItem(testEnchant, "§7§lChoose team", ""));
            testEnchant = new ItemStack(Material.SLIME_BALL, 1);
            meta = testEnchant.getItemMeta();
            meta.addEnchant(Enchantment.LURE, 1, false);
            meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ENCHANTS });
            testEnchant.setItemMeta(meta);
            gamePlayer.getPlayer().getInventory().setItem(8, myItm.nameItem(testEnchant, "§2§lLeave", ""));
            if (this.mapVoting != null) {
                testEnchant = new ItemStack(Material.PAPER, 1);
                meta = testEnchant.getItemMeta();
                meta.addEnchant(Enchantment.LURE, 1, false);
                meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ENCHANTS });
                testEnchant.setItemMeta(meta);
                gamePlayer.getPlayer().getInventory().setItem(4, myItm.nameItem(testEnchant, "§6§lMap voting", ""));
            }
            if (this.players.size() == this.minPlayers && !this.isState(GameState.STARTING)) {
                this.setGameState(GameState.STARTING);
                this.sendMessage("&a[*] " + this.main.getMessagesData().getConfig().getString("game.begin-in-30s"));
                this.startCountdown();
            }
            this.main.getGameManager().setGame(gamePlayer.getPlayer(), this);
            this.vanishPlayers(gamePlayer);
            return true;
        }
        gamePlayer.sendMessage(ChatColor.RED + this.main.getMessagesData().getConfig().getString("game.full") + " &7(" + this.getPlayers().size() + "&a/&7" + this.maxPlayers + ")");
        return false;
    }
    
    public void startCountdown() {
        new GameCountdownTask(this).runTaskTimer((Plugin)this.main, 0L, 20L);
    }
    
    public void setMovementFrozen(final boolean b) {
    }
    
    @SuppressWarnings("deprecation")
	public void vanishPlayers(final GamePlayer gamePlayer) {
        for (final Player p : this.main.getGameManager().getPlayersInGame()) {
            if (!this.main.getGameManager().getGame(p).equals(this.main.getGameManager().getGame(gamePlayer.getPlayer()))) {
                p.hidePlayer(gamePlayer.getPlayer());
                gamePlayer.getPlayer().hidePlayer(p);
            }
        }
    }
    
    public void saveMap() {
        final MyWorld myWorld = new MyWorld(this.main);
        myWorld.save(Bukkit.getWorld(this.world.getName()).getWorldFolder().getPath(), this.world.getName());
    }
    
    public void resetMap() throws Exception {
        final MyWorld myWorld = new MyWorld(this.main);
        myWorld.load(this.world.getName(), Bukkit.getWorld(this.world.getName()).getWorldFolder().getPath());
    }
    
    public void deleteMap() {
        final MyWorld myWorld = new MyWorld(this.main);
        myWorld.deleteSaves(this.world.getName());
    }
    
    @SuppressWarnings("deprecation")
	public void startGame() {
        try {
            for (final Location loc : this.bronzeLoc) {
                this.main.getGeneratorManager().addGenerator(loc.getBlock(), new ItemStack(Material.LEGACY_CLAY_BRICK), 1.3);
            }
            for (final Location loc : this.ironLoc) {
                this.main.getGeneratorManager().addGenerator(loc.getBlock(), new ItemStack(Material.IRON_INGOT), 14.0);
            }
            for (final Location loc : this.goldLoc) {
                this.main.getGeneratorManager().addGenerator(loc.getBlock(), new ItemStack(Material.GOLD_INGOT), 45.0);
            }
        }
        catch (NullPointerException ex) {}
    }
    
    @SuppressWarnings("deprecation")
	public void endGame() {
        this.main.getStatsManager().updateStats(this);
        for (final Location loc : this.bronzeLoc) {
            this.main.getGeneratorManager().removeGenerator(loc.getBlock());
        }
        for (final Location loc : this.ironLoc) {
            this.main.getGeneratorManager().removeGenerator(loc.getBlock());
        }
        for (final Location loc : this.goldLoc) {
            this.main.getGeneratorManager().removeGenerator(loc.getBlock());
        }
        final MyItems myItm = new MyItems();
        for (final GamePlayer player : this.players) {
            player.teleport(this.main.getArenasData().getConfig().getLocation("main-lobby"));
            player.sendMessage("&9Bedwars &7>> &c" + this.main.getMessagesData().getConfig().getString("game.end"));
            this.main.getGameManager().setGame(player.getPlayer(), null);
            player.getPlayer().getInventory().clear();
            ItemStack testEnchant = new ItemStack(Material.COMPASS, 1);
            ItemMeta meta = testEnchant.getItemMeta();
            meta.addEnchant(Enchantment.LURE, 1, false);
            meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ENCHANTS });
            testEnchant.setItemMeta(meta);
            player.getPlayer().getInventory().setItem(0, myItm.nameItem(testEnchant, "§e§l" + this.main.getMessagesData().getConfig().getString("items.play"), ""));
            testEnchant = myItm.getHead(player.getName());
            meta = testEnchant.getItemMeta();
            meta.addEnchant(Enchantment.LURE, 1, false);
            meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ENCHANTS });
            testEnchant.setItemMeta(meta);
            player.getPlayer().getPlayer().getInventory().setItem(1, myItm.nameItem(testEnchant, "§3§l" + this.main.getMessagesData().getConfig().getString("items.top-players"), ""));
            testEnchant = new ItemStack(Material.EMERALD, 1);
            meta = testEnchant.getItemMeta();
            meta.addEnchant(Enchantment.LURE, 1, false);
            meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ENCHANTS });
            testEnchant.setItemMeta(meta);
            player.getPlayer().getPlayer().getInventory().setItem(2, myItm.nameItem(testEnchant, "§5§l" + this.main.getMessagesData().getConfig().getString("items.top-clans"), ""));
            final Board board = new Board(player.getPlayer().getUniqueId());
            if (board.hasID()) {
                board.stop();
            }
            player.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
            for (final Player p : Bukkit.getOnlinePlayers()) {
                player.getPlayer().showPlayer(p);
                p.showPlayer(player.getPlayer());
            }
            player.getPlayer().setDisplayName(player.getName());
            final MainLobbyScoreboard score = new MainLobbyScoreboard(this.main);
            score.createBoard(player.getPlayer());
            score.startScoreboard_Lobby(player.getPlayer());
        }
        try {
            this.resetMap();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        this.main.getGameManager().getGames().remove(this);
        if (this.mapVoting != null) {
            this.main.getGameManager().getMapVotingManager().addAndUpdateGames(this.displayName);
        }
        else {
            this.main.getGameManager().getGames().add(new Game(this.main, this.getDisplayName()));
        }
    }
    
    public GamePlayer getGamePlayer(final Player player) {
        for (final GamePlayer gamePlayer : this.getPlayers()) {
            if (gamePlayer.getPlayer() == player) {
                return gamePlayer;
            }
        }
        return null;
    }
    
    public void sendTitle(final String title, final String subtitle) {
        for (final GamePlayer gp : this.players) {
            gp.getPlayer().sendTitle(ChatColor.translateAlternateColorCodes('&', title), ChatColor.translateAlternateColorCodes('&', subtitle), 1, 50, 1);
        }
    }
    
    public void sendMessage(final String message) {
        for (final GamePlayer gp : this.players) {
            gp.sendMessage(message);
        }
    }
    
    public boolean isState(final GameState state) {
        return this.getGameState() == state;
    }
    
    public Integer getMedPlayersScore(final GamePlayer player) {
        int n = 0;
        for (final GamePlayer p : this.players) {
            n += p.getScorePlayer();
        }
        if (n == 0 && this.players.size() == 0) {
            return Math.abs(n - player.getScorePlayer());
        }
        n /= this.players.size();
        return Math.abs(n - player.getScorePlayer());
    }
    
    public String getMadeBy() {
        return this.madeBy;
    }
    
    public String getDisplayName() {
        return this.displayName;
    }
    
    public Set<Location> getBronzeLoc() {
        return this.bronzeLoc;
    }
    
    public Set<Location> getIronLoc() {
        return this.ironLoc;
    }
    
    public Set<Location> getGoldLoc() {
        return this.goldLoc;
    }
    
    public Location getLobbyLoc() {
        return this.lobbyLoc;
    }
    
    public GameState getGameState() {
        return this.gameState;
    }
    
    public void setGameState(final GameState gameState) {
        this.gameState = gameState;
    }
    
    public List<GamePlayer> getPlayers() {
        return this.players;
    }
    
    public World getWorld() {
        return this.world;
    }
    
    public Set<Location> getPlacedBlocks() {
        return this.placedBlocks;
    }
    
    public int getMaxPlayers() {
        return this.maxPlayers;
    }
    
    public int getMinPlayers() {
        return this.minPlayers;
    }
    
    public Bedwars getMain() {
        return this.main;
    }
    
    public MapVoting getMapVoting() {
        return this.mapVoting;
    }
    
    public TeamManager getTeamManager() {
        return this.teamManager;
    }
    
    public void loadMapFromConfig(final String name) {
        this.displayName = name;
        this.madeBy = this.main.getArenasData().getConfig().getString("arenas." + this.displayName + ".madeBy");
        if (this.teamManager != null) {
            this.teamManager.reload(name);
        }
        else {
            this.teamManager = new TeamManager(this.main, this, name);
        }
        this.world = this.teamManager.getWorld();
        final int nbronze = this.main.getArenasData().getConfig().getInt("arenas." + this.displayName + ".locations.bronzespawns.amount");
        this.bronzeLoc = new HashSet<Location>(nbronze);
        for (int i = 1; i <= nbronze; ++i) {
            this.bronzeLoc.add(this.main.getArenasData().getConfig().getLocation("arenas." + this.displayName + ".locations.bronzespawns." + i));
        }
        final int niron = this.main.getArenasData().getConfig().getInt("arenas." + this.displayName + ".locations.ironspawns.amount");
        this.ironLoc = new HashSet<Location>(niron);
        for (int j = 1; j <= niron; ++j) {
            this.ironLoc.add(this.main.getArenasData().getConfig().getLocation("arenas." + this.displayName + ".locations.ironspawns." + j));
        }
        final int ngold = this.main.getArenasData().getConfig().getInt("arenas." + this.displayName + ".locations.goldspawns.amount");
        this.goldLoc = new HashSet<Location>(niron);
        for (int k = 1; k <= ngold; ++k) {
            this.goldLoc.add(this.main.getArenasData().getConfig().getLocation("arenas." + this.displayName + ".locations.goldspawns." + k));
        }
    }
    
    public void startScoreboard_Lobby(final GamePlayer player) {
        this.taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask((Plugin)this.main, (Runnable)new Runnable() {
            int count = 0;
            Board board = new Board(player.getPlayer().getUniqueId());
            Scoreboard scoreboard = player.getPlayer().getScoreboard();
            Objective obj = this.scoreboard.getObjective("Bedwars-1");
            MyScoreboard myScr = new MyScoreboard();
            
            @Override
            public void run() {
                if (!this.board.hasID()) {
                    this.board.SetID(Game.this.taskID);
                }
                this.myScr.replaceScore(this.obj, 5, "Players: " + ChatColor.GREEN + Game.this.getPlayers().size() + "/" + Game.this.getMaxPlayers());
                if (this.count == 10) {
                    this.count = 0;
                }
                switch (this.count) {
                    case 0: {
                        player.getPlayer().getScoreboard().getObjective(DisplaySlot.SIDEBAR).setDisplayName(ChatColor.translateAlternateColorCodes('&', "&7&l<< &e&lBedWars &7&l>>"));
                        break;
                    }
                    case 1: {
                        player.getPlayer().getScoreboard().getObjective(DisplaySlot.SIDEBAR).setDisplayName(ChatColor.translateAlternateColorCodes('&', "&7&l<< &f&lB&e&ledWars &7&l>>"));
                        break;
                    }
                    case 2: {
                        player.getPlayer().getScoreboard().getObjective(DisplaySlot.SIDEBAR).setDisplayName(ChatColor.translateAlternateColorCodes('&', "&7&l<< &e&lB&f&le&e&ldWars &7&l>>"));
                        break;
                    }
                    case 3: {
                        player.getPlayer().getScoreboard().getObjective(DisplaySlot.SIDEBAR).setDisplayName(ChatColor.translateAlternateColorCodes('&', "&7&l<< &e&lBe&f&ld&e&lWars &7&l>>"));
                        break;
                    }
                    case 4: {
                        player.getPlayer().getScoreboard().getObjective(DisplaySlot.SIDEBAR).setDisplayName(ChatColor.translateAlternateColorCodes('&', "&7&l<< &e&lBed&f&lW&e&lars &7&l>>"));
                        break;
                    }
                    case 5: {
                        player.getPlayer().getScoreboard().getObjective(DisplaySlot.SIDEBAR).setDisplayName(ChatColor.translateAlternateColorCodes('&', "&7&l<< &e&lBedW&f&la&e&lrs &7&l>>"));
                        break;
                    }
                    case 6: {
                        player.getPlayer().getScoreboard().getObjective(DisplaySlot.SIDEBAR).setDisplayName(ChatColor.translateAlternateColorCodes('&', "&7&l<< &e&lBedWa&f&lr&e&ls &7&l>>"));
                        break;
                    }
                    case 7: {
                        player.getPlayer().getScoreboard().getObjective(DisplaySlot.SIDEBAR).setDisplayName(ChatColor.translateAlternateColorCodes('&', "&7&l<< &e&lBedWar&f&ls &7&l>>"));
                        break;
                    }
                    case 8: {
                        player.getPlayer().getScoreboard().getObjective(DisplaySlot.SIDEBAR).setDisplayName(ChatColor.translateAlternateColorCodes('&', "&7&l<< &f&lBedWars &7&l>>"));
                        break;
                    }
                    case 9: {
                        player.getPlayer().getScoreboard().getObjective(DisplaySlot.SIDEBAR).setDisplayName(ChatColor.translateAlternateColorCodes('&', "&7&l<< &e&lBedWars &7&l>>"));
                        break;
                    }
                }
                ++this.count;
            }
        }, 0L, 10L);
    }
    
    public enum GameState
    {
        LOBBY("LOBBY", 0), 
        STARTING("STARTING", 1), 
        PLAYING("PLAYING", 2), 
        ENDING("ENDING", 3);
        
        private GameState(final String name, final int ordinal) {
        }
    }
}
