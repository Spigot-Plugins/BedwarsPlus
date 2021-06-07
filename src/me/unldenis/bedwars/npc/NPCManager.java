
package me.unldenis.bedwars.npc;

import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import java.util.Arrays;
import org.bukkit.ChatColor;
import me.unldenis.bedwars.Bedwars;

public class NPCManager
{
    private Bedwars main;
    
    public NPCManager(final Bedwars plugin) {
        this.main = plugin;
    }
    
    public void loadAllNpcs() {
        for (int nNpcs = this.main.getNpcsData().getConfig().getInt("amount"), i = 1; i <= nNpcs; ++i) {
            final Location loc = this.main.getNpcsData().getConfig().getLocation("npcs." + i + ".location");
            final String type = this.capitalize(this.main.getNpcsData().getConfig().getString("npcs." + i + ".type"));
            final String skin = this.main.getNpcsData().getConfig().getString("npcs." + i + ".skin");
            new NPC(this.main, type, loc, skin);
            this.main.getHoloManager().createHologram(new StringBuilder().append(ChatColor.YELLOW).append(ChatColor.BOLD).append("CLICK TO PLAY").toString(), Arrays.asList("[" + type + "]", new StringBuilder().append(ChatColor.YELLOW).append(ChatColor.BOLD).append("0 Players").toString()), loc.add(0.0, 0.28, 0.0));
        }
    }
    
    public void removeNPCs() {
        if (!Bukkit.getOnlinePlayers().isEmpty()) {
            for (final Player player : Bukkit.getOnlinePlayers()) {
                NPC.getNPCs().forEach(npc -> npc.removeNPCPacket(player));
            }
        }
    }
    
    private String capitalize(final String str) {
        if (str == null) {
            return str;
        }
        return String.valueOf(str.substring(0, 1).toUpperCase()) + str.substring(1);
    }
}
