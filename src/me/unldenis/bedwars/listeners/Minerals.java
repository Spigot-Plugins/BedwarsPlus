
package me.unldenis.bedwars.listeners;

import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.event.Listener;

public class Minerals implements Listener
{
    @SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.HIGH)
    public void onItemMerge(final ItemMergeEvent event) {
        if (event.getEntity().getItemStack().equals((Object)new ItemStack(Material.LEGACY_CLAY_BRICK)) || event.getEntity().getItemStack().equals((Object)new ItemStack(Material.IRON_INGOT)) || event.getEntity().getItemStack().equals((Object)new ItemStack(Material.GOLD_INGOT))) {
            event.setCancelled(true);
        }
    }
}
