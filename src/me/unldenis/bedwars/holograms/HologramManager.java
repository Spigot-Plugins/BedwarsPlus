package me.unldenis.bedwars.holograms;


import org.bukkit.Location;
import java.util.ArrayList;
import me.unldenis.bedwars.Bedwars;
import java.util.List;

public class HologramManager
{
    private List<Hologram> holograms;
    private Bedwars main;
    
    public HologramManager(final Bedwars plugin) {
        this.holograms = new ArrayList<Hologram>();
        this.main = plugin;
    }
    
    public Hologram createHologram(final String header, final List<String> lines, final Location location) {
        final Hologram holo = new Hologram(header, lines, location);
        this.holograms.add(holo);
        return holo;
    }
    
    public void clear() {
        for (final Hologram holo : this.holograms) {
            holo.delete();
        }
    }
    
    public Bedwars getMain() {
        return this.main;
    }
}
