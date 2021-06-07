
package me.unldenis.bedwars.object.clans;

import java.util.stream.Collectors;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.Location;
import org.bukkit.ChatColor;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import me.unldenis.bedwars.util.MyItems;
import org.bukkit.Bukkit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.Map;
import me.unldenis.bedwars.Bedwars;

public class ClanManager
{
    private final Bedwars main;
    private Map<UUID, Clan> playerClanMap;
    private List<Clan> clans;
    private Map<UUID, Clan> playerRequestClanMap;
    
    public ClanManager(final Bedwars bedwars) {
        this.playerClanMap = new HashMap<UUID, Clan>();
        this.clans = new ArrayList<Clan>();
        this.playerRequestClanMap = new HashMap<UUID, Clan>();
        this.main = bedwars;
    }
    
    public void loadClans() {
        if (this.main.getClansData().getConfig().getConfigurationSection("clans") != null) {
            for (final String clanName : this.main.getClansData().getConfig().getConfigurationSection("clans").getKeys(false)) {
                final List<String> members = (List<String>)this.main.getClansData().getConfig().getStringList("clans." + clanName + ".members");
                final String founder = this.main.getClansData().getConfig().getString("clans." + clanName + ".founder");
                final List<UUID> members2 = new ArrayList<UUID>();
                for (int j = 0; j < members.size(); ++j) {
                    members2.add(UUID.fromString(members.get(j)));
                }
                final int score = this.main.getClansData().getConfig().getInt("clans." + clanName + ".score");
                final Clan cl = new Clan(this.main, UUID.fromString(founder), members2, clanName, score);
                this.clans.add(cl);
                for (final UUID m : members2) {
                    this.playerClanMap.put(m, cl);
                }
            }
            this.main.getLogger().warning("Clans loaded: " + this.clans.size());
        }
        else {
            this.main.getLogger().warning("No clans have been created. Please create one using the creation command.");
        }
        Bukkit.getScheduler().scheduleSyncRepeatingTask((Plugin)this.main, (Runnable)new Runnable() {
            @SuppressWarnings("deprecation")
			@Override
            public void run() {
                if (ClanManager.this.main.getClansData().getConfig().getConfigurationSection("armorstand") != null) {
                    final List<Clan> tops = ClanManager.this.main.getClanManager().sortClans();
                    for (final String uuid : ClanManager.this.main.getClansData().getConfig().getConfigurationSection("armorstand").getKeys(false)) {
                        final Location loc = ClanManager.this.main.getClansData().getConfig().getLocation("armorstand." + uuid + ".location");
                        final int index = ClanManager.this.main.getClansData().getConfig().getInt("armorstand." + uuid + ".rank");
                        final MyItems myItm = new MyItems();
                        for (final Entity entity : loc.getWorld().getNearbyEntities(loc, 0.0, 2.0, 0.0)) {
                            if (!(entity instanceof ArmorStand)) {
                                continue;
                            }
                            final ArmorStand stand = (ArmorStand)entity;
                            if (!stand.isVisible() || !stand.isCustomNameVisible()) {
                                continue;
                            }
                            try {
                                stand.setHelmet(myItm.getHead(Bukkit.getOfflinePlayer(tops.get(index - 1).getFounder()).getName()));
                                switch (index) {
                                    case 1: {
                                        stand.setCustomName("+" + ChatColor.GREEN + tops.get(0).getScore() + ChatColor.GOLD + " " + ChatColor.BOLD + tops.get(0).getNameClan());
                                        continue;
                                    }
                                    case 2: {
                                        stand.setCustomName("+" + ChatColor.GREEN + tops.get(1).getScore() + ChatColor.WHITE + " " + ChatColor.BOLD + tops.get(1).getNameClan());
                                        continue;
                                    }
                                    case 3: {
                                        stand.setCustomName("+" + ChatColor.GREEN + tops.get(2).getScore() + ChatColor.RED + " " + ChatColor.BOLD + tops.get(2).getNameClan());
                                        continue;
                                    }
                                }
                            }
                            catch (IndexOutOfBoundsException ex) {}
                        }
                    }
                }
            }
        }, 0L, (long)(this.main.getClansData().getConfig().getInt("config.update-armorstand") * 20));
    }
    
    public Clan getClan(final String name) {
        for (final Clan c : this.clans) {
            if (c.getNameClan().equalsIgnoreCase(name)) {
                return c;
            }
        }
        return null;
    }
    
    public Clan createClan(final UUID founder, final String name) {
        final Clan cl = new Clan(this.main, founder, name, 0);
        this.clans.add(cl);
        this.setClan(Bukkit.getPlayer(founder), cl);
        return cl;
    }
    
    public Clan getClan(final Player player) {
        return this.playerClanMap.get(player.getUniqueId());
    }
    
    public void setClan(final Player player, final Clan clan) {
        if (clan == null) {
            this.playerClanMap.remove(player.getUniqueId());
        }
        else {
            this.playerClanMap.put(player.getUniqueId(), clan);
        }
    }
    
    public boolean hasClan(final Player player) {
        return this.playerClanMap.containsKey(player.getUniqueId());
    }
    
    public void sendRequest(final Player player, final Clan clan) {
        if (clan == null) {
            this.playerRequestClanMap.remove(player.getUniqueId());
        }
        else {
            this.playerRequestClanMap.put(player.getUniqueId(), clan);
        }
    }
    
    public boolean hasClanRequest(final Player player) {
        return this.playerRequestClanMap.containsKey(player.getUniqueId());
    }
    
    public Clan getClanRequest(final Player player) {
        return this.playerRequestClanMap.get(player.getUniqueId());
    }
    
    public List<Clan> sortClans() {
        return this.clans.stream().sorted((c2, c) -> Integer.compare(c.getScore(), c2.getScore())).collect(Collectors.toList());
    }
}
