package me.unldenis.bedwars.generator;

import org.bukkit.inventory.ItemStack;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

public class Generator extends BukkitRunnable
{
    private Block b;
    private ItemStack item;
    private double time;
    private final double timeX;
    
    public Generator(final Block b, final ItemStack item, final double time) {
        this.b = b;
        this.item = item;
        this.time = time;
        this.timeX = time;
    }
    
    public void run() {
        if (this.time <= 0.0) {
            this.b.getWorld().dropItem(this.b.getLocation().add(0.5, 1.0, 0.5), this.item);
            this.time = this.timeX;
        }
        this.time -= 0.05;
    }
}
