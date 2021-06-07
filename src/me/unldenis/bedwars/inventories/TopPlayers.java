package me.unldenis.bedwars.inventories;

import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.ItemStack;
import java.util.List;
import java.util.ArrayList;
import org.bukkit.OfflinePlayer;
import me.unldenis.bedwars.util.MyItems;
import org.bukkit.Bukkit;
import me.unldenis.bedwars.Bedwars;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class TopPlayers implements InventoryHolder
{
    private final Inventory inv;
    private Bedwars main;
    
    public TopPlayers(final Bedwars plugin) {
        this.main = plugin;
        this.inv = Bukkit.createInventory((InventoryHolder)this, 54, this.main.getMessagesData().getConfig().getString("inventories.top-players"));
        this.init();
    }
    
    private void init() {
        final List<OfflinePlayer> players = this.main.getStatsManager().sortPlayers();
        ItemStack dvp = null;
        ItemMeta meta = null;
        final MyItems myItm = new MyItems();
        for (int j = 0; j < Math.min(50, players.size()); ++j) {
            dvp = myItm.getHead(players.get(j).getName());
            meta = dvp.getItemMeta();
            final List<String> lore = new ArrayList<String>();
            lore.add("§a" + this.main.getStatsManager().getScore(players.get(j)) + "§7§o points");
            switch (j) {
                case 0: {
                    meta.setDisplayName("§6§l" + (j + 1) + "° §f§l" + players.get(j).getName());
                    break;
                }
                case 1: {
                    meta.setDisplayName("§f§l" + (j + 1) + "° §f§l" + players.get(j).getName());
                    break;
                }
                case 2: {
                    meta.setDisplayName("§c§l" + (j + 1) + "° §f§l" + players.get(j).getName());
                    break;
                }
                default: {
                    meta.setDisplayName("§7§l" + (j + 1) + "° §f§l" + players.get(j).getName());
                    break;
                }
            }
            meta.setLore(lore);
            dvp.setItemMeta(meta);
            this.inv.setItem(this.inv.firstEmpty(), dvp);
        }
    }
    
    public Inventory getInventory() {
        return this.inv;
    }
}
