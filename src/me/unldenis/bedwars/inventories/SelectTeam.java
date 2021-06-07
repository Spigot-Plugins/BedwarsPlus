package me.unldenis.bedwars.inventories;

import org.bukkit.inventory.meta.ItemMeta;
import java.util.List;
import java.util.ArrayList;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import me.unldenis.bedwars.object.teams.Team;
import me.unldenis.bedwars.util.MyChat;
import me.unldenis.bedwars.util.MyItems;
import org.bukkit.Bukkit;
import me.unldenis.bedwars.object.Game;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class SelectTeam implements InventoryHolder
{
    private final Inventory inv;
    private Game game;
    
    public SelectTeam(final Game game) {
        this.inv = Bukkit.createInventory((InventoryHolder)this, 9, game.getMain().getMessagesData().getConfig().getString("inventories.choose-team"));
        this.game = game;
        this.init();
    }
    
    private void init() {
        final MyItems myItm = new MyItems();
        final MyChat myCht = new MyChat();
        for (final Team tm : this.game.getTeamManager().getTeams()) {
            final ItemStack testEnchant = new ItemStack(myItm.materialFromTeam(tm.getTeamType()), 1);
            final ItemMeta meta = testEnchant.getItemMeta();
            meta.addEnchant(Enchantment.LURE, 1, false);
            meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ENCHANTS });
            final List<String> lore = new ArrayList<String>();
            lore.add("§7§n" + this.game.getMain().getMessagesData().getConfig().getString("inventories.click-join-team"));
            lore.add("§7§o(" + tm.getPlayers().size() + "/" + this.game.getMaxPlayers() / this.game.getTeamManager().getTeams().size() + ")");
            meta.setDisplayName(String.valueOf(myCht.prefix(tm.getTeamType())) + "§l" + tm.toString().toUpperCase() + " TEAM");
            meta.setLore(lore);
            testEnchant.setItemMeta(meta);
            this.inv.setItem(this.inv.firstEmpty(), testEnchant);
        }
    }
    
    public Inventory getInventory() {
        return this.inv;
    }
}
