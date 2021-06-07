package me.unldenis.bedwars;

import org.bukkit.Color;
import me.unldenis.bedwars.npc.PacketReader;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import me.unldenis.bedwars.listeners.SelectArmor;
import me.unldenis.bedwars.listeners.Damage;
import me.unldenis.bedwars.listeners.ClickNPC;
import me.unldenis.bedwars.listeners.PlayerChat;
import me.unldenis.bedwars.listeners.PlayerJoin;
import me.unldenis.bedwars.listeners.PlayerLeave;
import me.unldenis.bedwars.listeners.PlayerDeath;
import me.unldenis.bedwars.listeners.VListener;
import me.unldenis.bedwars.listeners.Interacts;
import me.unldenis.bedwars.listeners.InventoryEvents;
import me.unldenis.bedwars.listeners.Beds;
import org.bukkit.plugin.Plugin;
import org.bukkit.event.Listener;
import me.unldenis.bedwars.listeners.Minerals;
import me.unldenis.bedwars.commands.Clan;
import me.unldenis.bedwars.commands.Npc;
import org.bukkit.command.CommandExecutor;
import me.unldenis.bedwars.commands.Setup;
import me.unldenis.bedwars.object.StatsManager;
import me.unldenis.bedwars.npc.NPCManager;
import me.unldenis.bedwars.holograms.HologramManager;
import me.unldenis.bedwars.object.clans.ClanManager;
import me.unldenis.bedwars.object.GameManager;
import me.unldenis.bedwars.generator.GeneratorManager;
import me.unldenis.bedwars.files.DataManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Bedwars extends JavaPlugin
{
    private DataManager arenasData;
    private DataManager configData;
    private DataManager npcsData;
    private DataManager statsData;
    private DataManager clansData;
    private DataManager messagesData;
    private GeneratorManager generatorManager;
    private GameManager gameManager;
    private ClanManager clanManager;
    private HologramManager holoManager;
    private NPCManager npcsManager;
    private StatsManager statsManager;
    
    public void onEnable() {
        this.arenasData = new DataManager(this, "arenas.yml");
        this.configData = new DataManager(this, "config.yml");
        this.npcsData = new DataManager(this, "npcs.yml");
        this.statsData = new DataManager(this, "stats.yml");
        this.clansData = new DataManager(this, "clans.yml");
        this.messagesData = new DataManager(this, "messages.yml");
        
        (this.gameManager = new GameManager(this)).LoadGames();
        (this.clanManager = new ClanManager(this)).loadClans();
        this.statsManager = new StatsManager(this);
        
        this.getCommand("bw").setExecutor((CommandExecutor)new Setup(this));
        this.getCommand("bwextra").setExecutor((CommandExecutor)new Npc(this));
        this.getCommand("clan").setExecutor((CommandExecutor)new Clan(this));
        
        
        this.getServer().getPluginManager().registerEvents((Listener)new Minerals(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new Beds(this), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new InventoryEvents(this), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new Interacts(this), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new VListener(this), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new PlayerDeath(this), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new PlayerLeave(this), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new PlayerJoin(this), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new PlayerChat(this), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new ClickNPC(this), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new Damage(this), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new SelectArmor(this), (Plugin)this);
        
        this.generatorManager = new GeneratorManager((Plugin)this);
        this.holoManager = new HologramManager(this);
        (this.npcsManager = new NPCManager(this)).loadAllNpcs();
        
        if (!Bukkit.getOnlinePlayers().isEmpty()) {
            for (final Player player : Bukkit.getOnlinePlayers()) {
                final PacketReader reader = new PacketReader(this);
                reader.inject(player);
            }
        }
        this.getLogger().warning(Color.AQUA.toString().toLowerCase());
    }
    
    public void onDisable() {
        this.gameManager.UnloadGames();
        this.holoManager.clear();
        this.npcsManager.removeNPCs();
        if (!Bukkit.getOnlinePlayers().isEmpty()) {
            for (final Player player : Bukkit.getOnlinePlayers()) {
                final PacketReader reader = new PacketReader(this);
                reader.uninject(player);
            }
        }
    }
    
    public GeneratorManager getGeneratorManager() {
        return this.generatorManager;
    }
    
    public GameManager getGameManager() {
        return this.gameManager;
    }
    
    public DataManager getConfigData() {
        return this.configData;
    }
    
    public DataManager getNpcsData() {
        return this.npcsData;
    }
    
    public DataManager getArenasData() {
        return this.arenasData;
    }
    
    public HologramManager getHoloManager() {
        return this.holoManager;
    }
    
    public NPCManager getNPCsManager() {
        return this.npcsManager;
    }
    
    public DataManager getStatsData() {
        return this.statsData;
    }
    
    public ClanManager getClanManager() {
        return this.clanManager;
    }
    
    public DataManager getClansData() {
        return this.clansData;
    }
    
    public StatsManager getStatsManager() {
        return this.statsManager;
    }
    
    public DataManager getMessagesData() {
        return this.messagesData;
    }
}
