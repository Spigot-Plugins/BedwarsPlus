package me.unldenis.bedwars.inventories;

import org.bukkit.inventory.meta.ItemMeta;
import java.util.List;
import net.md_5.bungee.api.ChatColor;
import java.util.ArrayList;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import me.unldenis.bedwars.object.Game;
import org.bukkit.Bukkit;
import me.unldenis.bedwars.Bedwars;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class GamesList implements InventoryHolder
{
    private final Inventory inv;
    private Bedwars main;
    
    public GamesList(final Bedwars main) {
        this.inv = Bukkit.createInventory((InventoryHolder)this, 54, main.getMessagesData().getConfig().getString("inventories.choose-game"));
        this.main = main;
        this.init();
    }
    
    private void init() {
        if (this.main.getGameManager().getMapVotingManager() == null) {
            for (final Game game : this.main.getGameManager().getGames()) {
                final ItemStack testEnchant = new ItemStack(Material.EMERALD_BLOCK, 1);
                final ItemMeta meta = testEnchant.getItemMeta();
                if (game.isState(Game.GameState.STARTING)) {
                    meta.addEnchant(Enchantment.LURE, 1, false);
                    meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ENCHANTS });
                }
                final List<String> lore = new ArrayList<String>();
                lore.add("§7§n" + this.main.getMessagesData().getConfig().getString("inventories.click-join"));
                lore.add("§7§o(" + game.getPlayers().size() + "/" + game.getMaxPlayers() + ")");
                meta.setDisplayName(ChatColor.GRAY + "§l" + game.getDisplayName());
                meta.setLore(lore);
                testEnchant.setItemMeta(meta);
                this.inv.setItem(this.inv.firstEmpty(), testEnchant);
            }
        }
        else {
            int i = 0;
            for (final Game game2 : this.main.getGameManager().getGames()) {
                ++i;
                final ItemStack testEnchant2 = new ItemStack(Material.EMERALD_BLOCK, 1);
                final ItemMeta meta2 = testEnchant2.getItemMeta();
                if (game2.isState(Game.GameState.STARTING)) {
                    meta2.addEnchant(Enchantment.LURE, 1, false);
                    meta2.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ENCHANTS });
                }
                final List<String> lore2 = new ArrayList<String>();
                lore2.add("§7§n" + this.main.getMessagesData().getConfig().getString("inventories.click-join"));
                lore2.add("§7§o(" + game2.getPlayers().size() + "/" + game2.getMaxPlayers() + ")");
                meta2.setDisplayName(ChatColor.GRAY + "§lMapVoting-" + i);
                meta2.setLore(lore2);
                testEnchant2.setItemMeta(meta2);
                this.inv.setItem(this.inv.firstEmpty(), testEnchant2);
            }
        }
    }
    
    public Inventory getInventory() {
        return this.inv;
    }
}
