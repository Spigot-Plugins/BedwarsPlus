package me.unldenis.bedwars.scoreboards;

import org.bukkit.Bukkit;
import java.util.HashMap;
import java.util.UUID;
import java.util.Map;

public class Board
{
    private static Map<UUID, Integer> TASKS;
    private final UUID uuid;
    
    static {
        Board.TASKS = new HashMap<UUID, Integer>();
    }
    
    public Board(final UUID uuid) {
        this.uuid = uuid;
    }
    
    public void SetID(final int id) {
        Board.TASKS.put(this.uuid, id);
    }
    
    public int getID() {
        return Board.TASKS.get(this.uuid);
    }
    
    public boolean hasID() {
        return Board.TASKS.containsKey(this.uuid);
    }
    
    public void stop() {
        Bukkit.getScheduler().cancelTask((int)Board.TASKS.get(this.uuid));
        Board.TASKS.remove(this.uuid);
    }
}
