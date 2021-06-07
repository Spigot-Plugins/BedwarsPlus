package me.unldenis.bedwars.util;

import org.bukkit.WorldCreator;
import java.io.IOException;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;
import java.io.File;
import me.unldenis.bedwars.Bedwars;

public class MyWorld
{
    private Bedwars main;
    
    public MyWorld(final Bedwars main) {
        this.main = main;
    }
    
    public void save(String path, final String name) {
        path = String.valueOf(path) + "/region";
        final File folder = new File(path);
        File[] listFiles;
        for (int length = (listFiles = folder.listFiles()).length, i = 0; i < length; ++i) {
            final File f = listFiles[i];
            try {
                FileUtils.copyFileToDirectory(f, new File(this.main.getDataFolder() + "/saves/" + name + "/"));
                Bukkit.getConsoleSender().sendMessage("Saved §e" + f.getName());
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void deleteSaves(final String name) {
        try {
            File[] listFiles;
            for (int length = (listFiles = new File(this.main.getDataFolder() + "/saves/" + name + "/").listFiles()).length, i = 0; i < length; ++i) {
                final File f = listFiles[i];
                f.delete();
            }
        }
        catch (NullPointerException ex) {}
    }
    
    public void load(final String name, final String path) {
        Bukkit.getServer().unloadWorld(name, true);
        File[] listFiles;
        for (int length = (listFiles = new File(String.valueOf(path) + "/region").listFiles()).length, i = 0; i < length; ++i) {
            final File f = listFiles[i];
            f.delete();
        }
        File[] listFiles2;
        for (int length2 = (listFiles2 = new File(this.main.getDataFolder() + "/saves/" + name + "/").listFiles()).length, j = 0; j < length2; ++j) {
            final File f = listFiles2[j];
            try {
                FileUtils.copyFileToDirectory(f, new File(String.valueOf(path) + "/region/"));
                Bukkit.getConsoleSender().sendMessage("Copied §e" + f.getName());
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        new WorldCreator(name).createWorld();
    }
}
