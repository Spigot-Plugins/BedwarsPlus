
package me.unldenis.bedwars.listeners;

import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.meta.ItemMeta;
import me.unldenis.bedwars.scoreboards.MainLobbyScoreboard;
import me.unldenis.bedwars.npc.PacketReader;
import me.unldenis.bedwars.npc.NPC;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.Material;
import org.bukkit.Difficulty;
import org.bukkit.plugin.Plugin;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.player.PlayerJoinEvent;
import me.unldenis.bedwars.util.MyItems;
import me.unldenis.bedwars.Bedwars;
import org.bukkit.event.Listener;

public class PlayerJoin implements Listener
{
    private Bedwars main;
    private MyItems myItm;
    
    public PlayerJoin(final Bedwars bedwars) {
        this.main = bedwars;
        this.myItm = new MyItems();
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onJoin(final PlayerJoinEvent event) {
        event.setJoinMessage((String)null);
        final Player player = event.getPlayer();
        try {
            player.teleport(this.main.getArenasData().getConfig().getLocation("main-lobby"));
        }
        catch (IllegalArgumentException e) {
            this.main.getLogger().warning("Bedwars lobby isn't set");
        }
        player.getInventory().clear();
        player.getInventory().setArmorContents((ItemStack[])null);
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20.0);
        Bukkit.getScheduler().runTaskLater((Plugin)this.main, (Runnable)new Runnable() {
            @Override
            public void run() {
                player.setGameMode(GameMode.ADVENTURE);
            }
        }, 3L);
        player.getWorld().setDifficulty(Difficulty.PEACEFUL);
        ItemStack testEnchant = new ItemStack(Material.COMPASS, 1);
        ItemMeta meta = testEnchant.getItemMeta();
        meta.addEnchant(Enchantment.LURE, 1, false);
        meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ENCHANTS });
        testEnchant.setItemMeta(meta);
        player.getInventory().setItem(0, this.myItm.nameItem(testEnchant, "§e§l" + this.main.getMessagesData().getConfig().getString("items.play"), ""));
        testEnchant = this.myItm.getHead(player.getName());
        meta = testEnchant.getItemMeta();
        meta.addEnchant(Enchantment.LURE, 1, false);
        meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ENCHANTS });
        testEnchant.setItemMeta(meta);
        player.getInventory().setItem(1, this.myItm.nameItem(testEnchant, "§3§l" + this.main.getMessagesData().getConfig().getString("items.top-players"), ""));
        testEnchant = new ItemStack(Material.EMERALD, 1);
        meta = testEnchant.getItemMeta();
        meta.addEnchant(Enchantment.LURE, 1, false);
        meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ENCHANTS });
        testEnchant.setItemMeta(meta);
        player.getInventory().setItem(2, this.myItm.nameItem(testEnchant, "§5§l" + this.main.getMessagesData().getConfig().getString("items.top-clans"), ""));
        for (final NPC npc : NPC.getNPCs()) {
            if (npc.hasSkin()) {
                npc.addNPCPacket(player);
            }
            else {
                npc.sendSetNPCSkinPacket(player);
            }
        }
        final PacketReader reader = new PacketReader(this.main);
        reader.inject(player);
        final MainLobbyScoreboard score = new MainLobbyScoreboard(this.main);
        score.createBoard(player);
        score.startScoreboard_Lobby(player);
    }
}
