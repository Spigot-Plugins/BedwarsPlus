package me.unldenis.bedwars.inventories;


import org.bukkit.inventory.meta.ItemMeta;
import java.util.List;
import java.util.ArrayList;
import me.unldenis.bedwars.object.clans.Clan;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.Bukkit;
import me.unldenis.bedwars.Bedwars;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class TopClans implements InventoryHolder
{
    private final Inventory inv;
    private Bedwars main;
    
    public TopClans(final Bedwars plugin) {
        this.main = plugin;
        this.inv = Bukkit.createInventory((InventoryHolder)this, 54, this.main.getMessagesData().getConfig().getString("inventories.top-clans"));
        this.init();
    }
    
    private void init() {
        final List<Clan> clans = this.main.getClanManager().sortClans();
        ItemStack dvp = null;
        ItemMeta meta = null;
        for (int j = 0; j < Math.min(50, clans.size()); ++j) {
            switch (j) {
                case 0: {
                    dvp = new ItemStack(Material.EMERALD);
                    meta = dvp.getItemMeta();
                    meta.setDisplayName("§6§l" + (j + 1) + "° §f§l" + clans.get(j).getNameClan());
                    break;
                }
                case 1: {
                    dvp = new ItemStack(Material.DIAMOND);
                    meta = dvp.getItemMeta();
                    meta.setDisplayName("§f§l" + (j + 1) + "° §f§l" + clans.get(j).getNameClan());
                    break;
                }
                case 2: {
                    dvp = new ItemStack(Material.GOLD_INGOT);
                    meta = dvp.getItemMeta();
                    meta.setDisplayName("§c§l" + (j + 1) + "° §f§l" + clans.get(j).getNameClan());
                    break;
                }
                default: {
                    dvp = new ItemStack(Material.IRON_INGOT);
                    meta = dvp.getItemMeta();
                    meta.setDisplayName("§f§l" + (j + 1) + "° §f§l" + clans.get(j).getNameClan());
                    break;
                }
            }
            final List<String> lore = new ArrayList<String>();
            lore.add("§a" + clans.get(j).getScore() + "§7§o points");
            lore.add("§7Members:");
            clans.get(j).getPlayers().forEach(p -> lore.add(" §o" + p.getName()));
            meta.setLore(lore);
            dvp.setItemMeta(meta);
            this.inv.setItem(this.inv.firstEmpty(), dvp);
        }
    }
    
    public Inventory getInventory() {
        return this.inv;
    }
}
