
package me.unldenis.bedwars.holograms;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.Location;
import java.util.List;
import me.unldenis.bedwars.util.MyChat;
import org.bukkit.entity.ArmorStand;
import java.util.ArrayList;

public class Hologram
{
    private ArrayList<ArmorStand> holoEntities;
    private MyChat myCht;
    
    public Hologram(final String header, final List<String> lines, final Location location) {
        this.holoEntities = new ArrayList<ArmorStand>();
        this.myCht = new MyChat();
        this.setupHologram(header, location);
        if (lines == null) {
            return;
        }
        for (final String line : lines) {
            this.addLine(line);
        }
    }
    
    private void setupHologram(final String header, final Location location) {
        final ArmorStand headerStand = (ArmorStand)location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        headerStand.setCustomName(this.myCht.getColoredText(header));
        headerStand.setCustomNameVisible(true);
        headerStand.setGravity(false);
        headerStand.setVisible(false);
        this.holoEntities.add(headerStand);
    }
    
    public void addLine(final String line) {
        final Location headerLocation = this.holoEntities.get(0).getLocation().clone();
        final ArmorStand lineStand = (ArmorStand)headerLocation.getWorld().spawnEntity(headerLocation.add(0.0, -0.28 * this.holoEntities.size(), 0.0), EntityType.ARMOR_STAND);
        lineStand.setCustomName(this.myCht.getColoredText(line));
        lineStand.setCustomNameVisible(true);
        lineStand.setGravity(false);
        lineStand.setVisible(false);
        this.holoEntities.add(lineStand);
    }
    
    public void setLine(final int line, final String text) {
        if (line < 1 || line > this.holoEntities.size()) {
            return;
        }
        final ArmorStand lineStand = this.holoEntities.get(line - 1);
        lineStand.setCustomName(this.myCht.getColoredText(text));
        lineStand.setCustomNameVisible(true);
        lineStand.setGravity(false);
        lineStand.setVisible(false);
    }
    
    public void removeLine(final int line) {
        if (line < 1 || line > this.holoEntities.size()) {
            return;
        }
        final ArmorStand lineStand = this.holoEntities.get(line - 1);
        lineStand.remove();
        this.holoEntities.remove(lineStand);
        this.update();
    }
    
    public String getLine(final int line) {
        if (line < 1 || line > this.holoEntities.size()) {
            return null;
        }
        return this.holoEntities.get(line - 1).getCustomName();
    }
    
    public String getStrippedLine(final int line) {
        if (line < 1 || line > this.holoEntities.size()) {
            return null;
        }
        return this.myCht.getStrippedText(this.holoEntities.get(line - 1).getCustomName());
    }
    
    public void move(final Location newLocation) {
        this.holoEntities.get(0).teleport(newLocation);
        this.update();
    }
    
    public void update() {
        final Location headerLocation = this.holoEntities.get(0).getLocation().clone();
        for (int x = 1; x < this.holoEntities.size(); ++x) {
            final ArmorStand lineStand = this.holoEntities.get(x);
            lineStand.teleport(headerLocation.add(0.0, -0.28, 0.0));
        }
    }
    
    public void delete() {
        for (final Entity e : this.holoEntities) {
            e.remove();
        }
    }
}
