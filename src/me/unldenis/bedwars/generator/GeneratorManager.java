
package me.unldenis.bedwars.generator;

import org.bukkit.inventory.ItemStack;
import org.bukkit.block.Block;
import java.util.HashMap;
import org.bukkit.plugin.Plugin;
import org.bukkit.Location;
import java.util.Map;

public class GeneratorManager
{
    private Map<Location, Generator> gens;
    private Plugin plugin;
    
    public GeneratorManager(final Plugin plugin) {
        this.gens = new HashMap<Location, Generator>();
        this.plugin = plugin;
    }
    
    public void addGenerator(final Block b, final ItemStack item, final double time) {
        this.gens.put(b.getLocation(), new Generator(b, item, time));
        this.gens.get(b.getLocation()).runTaskTimer(this.plugin, 0L, 1L);
    }
    
    public void removeGenerator(final Block b) {
        if (this.gens.containsKey(b.getLocation())) {
            this.gens.get(b.getLocation()).cancel();
            this.gens.remove(b);
        }
    }
}
