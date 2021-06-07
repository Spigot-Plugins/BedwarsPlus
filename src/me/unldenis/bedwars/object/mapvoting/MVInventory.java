package me.unldenis.bedwars.object.mapvoting;

import org.bukkit.inventory.meta.ItemMeta;
import java.util.List;
import org.bukkit.ChatColor;
import me.unldenis.bedwars.object.GamePlayer;
import java.util.ArrayList;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class MVInventory implements InventoryHolder
{
    private final Inventory inv;
    private final MapVoting mapVoting;
    
    public MVInventory(final MapVoting mv) {
        this.inv = Bukkit.createInventory((InventoryHolder)this, 27, "Map voting");
        this.mapVoting = mv;
        this.init();
    }
    
    private void init() {
        ItemStack testEnchant = new ItemStack(Material.EMERALD_BLOCK, 1);
        ItemMeta meta = testEnchant.getItemMeta();
        meta.addEnchant(Enchantment.LURE, 1, false);
        meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ENCHANTS });
        List<String> lore = new ArrayList<String>();
        lore.add("§7§nClick to vote");
        lore.add("§7§l§o" + this.mapVoting.getVote_1().size() + " votes");
        for (final GamePlayer p : this.mapVoting.getVote_1()) {
            lore.add("§7§o" + p.getName());
        }
        meta.setDisplayName(ChatColor.GRAY + this.mapVoting.getArena_1());
        meta.setLore(lore);
        testEnchant.setItemMeta(meta);
        this.inv.setItem(11, testEnchant);
        testEnchant = new ItemStack(Material.EMERALD_BLOCK, 1);
        meta = testEnchant.getItemMeta();
        meta.addEnchant(Enchantment.LURE, 1, false);
        meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ENCHANTS });
        lore = new ArrayList<String>();
        lore.add("§7§nClick to vote");
        lore.add("§7§l§o" + this.mapVoting.getVote_2().size() + " votes");
        for (final GamePlayer p : this.mapVoting.getVote_2()) {
            lore.add("§7§o" + p.getName());
        }
        meta.setDisplayName(ChatColor.GRAY + this.mapVoting.getArena_2());
        meta.setLore(lore);
        testEnchant.setItemMeta(meta);
        this.inv.setItem(15, testEnchant);
    }
    
    public Inventory getInventory() {
        return this.inv;
    }
    
    public MapVoting getMapVoting() {
        return this.mapVoting;
    }
}
