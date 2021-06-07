package me.unldenis.bedwars.npc;

import org.bukkit.event.HandlerList;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

public class RightClickNPC extends Event implements Cancellable
{
    private final Player player;
    private final NPC npc;
    private boolean isCancelled;
    private static final HandlerList HANDLERS;
    
    static {
        HANDLERS = new HandlerList();
    }
    
    public RightClickNPC(final Player player, final NPC npc) {
        this.player = player;
        this.npc = npc;
    }
    
    public boolean isCancelled() {
        return this.isCancelled;
    }
    
    public void setCancelled(final boolean arg0) {
        this.isCancelled = arg0;
    }
    
    public HandlerList getHandlers() {
        return RightClickNPC.HANDLERS;
    }
    
    public static HandlerList getHandlerList() {
        return RightClickNPC.HANDLERS;
    }
    
    public Player getPlayer() {
        return this.player;
    }
    
    public NPC getNPC() {
        return this.npc;
    }
}
