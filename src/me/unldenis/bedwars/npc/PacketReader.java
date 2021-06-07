
package me.unldenis.bedwars.npc;

import java.lang.reflect.Field;
import org.bukkit.plugin.Plugin;
import org.bukkit.event.Event;
import org.bukkit.Bukkit;
import io.netty.channel.ChannelHandler;
import net.minecraft.server.v1_16_R3.Packet;
import java.util.List;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.server.v1_16_R3.PacketPlayInUseEntity;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import java.util.HashMap;
import me.unldenis.bedwars.Bedwars;
import java.util.UUID;
import java.util.Map;
import io.netty.channel.Channel;

public class PacketReader
{
    Channel ahc;
    public static Map<UUID, Channel> channels;
    private Bedwars main;
    
    static {
        PacketReader.channels = new HashMap<UUID, Channel>();
    }
    
    public PacketReader(final Bedwars main) {
        this.main = main;
    }
    
    public void inject(final Player player) {
        final CraftPlayer craftPlayer = (CraftPlayer)player;
        this.ahc = craftPlayer.getHandle().playerConnection.networkManager.channel;
        PacketReader.channels.put(player.getUniqueId(), this.ahc);
        if (this.ahc.pipeline().get("PacketInjector") != null) {
            return;
        }
        this.ahc.pipeline().addAfter("decoder", "PacketInjector", (ChannelHandler)new MessageToMessageDecoder<PacketPlayInUseEntity>() {
            protected void decode(final ChannelHandlerContext channel, final PacketPlayInUseEntity packet, final List<Object> arg) throws Exception {
                arg.add(packet);
                PacketReader.this.readPacket(player, (Packet<?>)packet);
            }
        });
    }
    
    public void uninject(final Player player) {
        this.ahc = PacketReader.channels.get(player.getUniqueId());
        if (this.ahc.pipeline().get("PacketInjector") != null) {
            this.ahc.pipeline().remove("PacketInjector");
        }
    }
    
    public void readPacket(final Player player, final Packet<?> packet) {
        if (packet.getClass().getSimpleName().equalsIgnoreCase("PacketPlayInUseEntity")) {
            if (this.getValue(packet, "action").toString().equalsIgnoreCase("ATTACK")) {
                return;
            }
            if (this.getValue(packet, "d").toString().equalsIgnoreCase("OFF_HAND")) {
                return;
            }
            if (this.getValue(packet, "action").toString().equalsIgnoreCase("INTERACT_AT")) {
                return;
            }
            final int id = (int)this.getValue(packet, "a");
            if (this.getValue(packet, "action").toString().equalsIgnoreCase("INTERACT")) {
                for (final NPC npc : NPC.getNPCs()) {
                    if (npc.getEntityPlayer().getId() == id) {
                        Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin)this.main, (Runnable)new Runnable() {
                            @Override
                            public void run() {
                                Bukkit.getPluginManager().callEvent((Event)new RightClickNPC(player, npc));
                            }
                        }, 0L);
                    }
                }
            }
        }
    }
    
    private Object getValue(final Object instance, final String name) {
        Object result = null;
        try {
            final Field field = instance.getClass().getDeclaredField(name);
            field.setAccessible(true);
            result = field.get(instance);
            field.setAccessible(false);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
