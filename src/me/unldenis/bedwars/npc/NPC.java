
package me.unldenis.bedwars.npc;

import net.minecraft.server.v1_16_R3.DataWatcher;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_16_R3.DataWatcherObject;
import net.minecraft.server.v1_16_R3.DataWatcherRegistry;
import java.io.IOException;
import com.mojang.authlib.properties.Property;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import net.minecraft.server.v1_16_R3.PacketPlayOutScoreboardTeam;
import net.minecraft.server.v1_16_R3.ScoreboardTeamBase;
import net.minecraft.server.v1_16_R3.ScoreboardTeam;
import org.bukkit.craftbukkit.v1_16_R3.scoreboard.CraftScoreboard;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_16_R3.PlayerConnection;
import net.minecraft.server.v1_16_R3.Entity;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityHeadRotation;
import net.minecraft.server.v1_16_R3.EntityHuman;
import net.minecraft.server.v1_16_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_16_R3.WorldServer;
import net.minecraft.server.v1_16_R3.MinecraftServer;
import org.bukkit.plugin.Plugin;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import net.minecraft.server.v1_16_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_16_R3.PlayerInteractManager;
import java.util.UUID;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.Location;
import me.unldenis.bedwars.Bedwars;
import java.util.ArrayList;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import java.util.List;

public class NPC
{
    private static List<NPC> NPC = new ArrayList<NPC>();
    private EntityPlayer npc;
    private GameProfile gameProfile;
    private String name;
    private String skin;
        
    public NPC(final Bedwars main, final String name, final Location location, final String skin) {
        me.unldenis.bedwars.npc.NPC.NPC.add(this);
        this.name = name;
        this.skin = skin;
        final MinecraftServer nmsServer = (MinecraftServer)((CraftServer)Bukkit.getServer()).getServer();
        final WorldServer nmsWorld = ((CraftWorld)Bukkit.getWorld(location.getWorld().getName())).getHandle();
        this.gameProfile = new GameProfile(UUID.randomUUID(), "");
        (this.npc = new EntityPlayer(nmsServer, nmsWorld, this.gameProfile, new PlayerInteractManager(nmsWorld))).setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        Bukkit.getScheduler().runTaskLater((Plugin)main, (Runnable)new Runnable() {
            @Override
            public void run() {
                final PacketPlayOutPlayerInfo info = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, new EntityPlayer[] { me.unldenis.bedwars.npc.NPC.this.npc });
                for (final Player p : Bukkit.getOnlinePlayers()) {
                    ((CraftPlayer)p).getHandle().playerConnection.sendPacket(info);
                }
            }
        }, 2L);
    }
    
    public void addNPCPacket(final Player player) {
        this.resetName(player);
        final PlayerConnection connection = ((CraftPlayer)player).getHandle().playerConnection;
        connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, new EntityPlayer[] { this.npc }));
        connection.sendPacket(new PacketPlayOutNamedEntitySpawn((EntityHuman)this.npc));
        connection.sendPacket(new PacketPlayOutEntityHeadRotation((Entity)this.npc, (byte)(this.npc.yaw * 256.0f / 360.0f)));
    }
    
    public void removeNPCPacket(final Player player) {
        final PlayerConnection connection = ((CraftPlayer)player).getHandle().playerConnection;
        connection.sendPacket(new PacketPlayOutEntityDestroy(new int[] { this.npc.getId() }));
    }
    
    public void resetName(final Player player) {
        final PacketPlayOutPlayerInfo info = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, new EntityPlayer[] { this.npc });
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(info);
        final ScoreboardTeam team = new ScoreboardTeam(((CraftScoreboard)Bukkit.getScoreboardManager().getMainScoreboard()).getHandle(), player.getName());
        team.setNameTagVisibility(ScoreboardTeamBase.EnumNameTagVisibility.NEVER);
        final ArrayList<String> playerToAdd = new ArrayList<String>();
        playerToAdd.add(this.npc.getName());
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(new PacketPlayOutScoreboardTeam(team, 1));
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(new PacketPlayOutScoreboardTeam(team, 0));
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(new PacketPlayOutScoreboardTeam(team, playerToAdd, 3));
    }
    
    public void sendSetNPCSkinPacket(final Player player) {
        this.removeNPCPacket(player);
        this.resetName(player);
        try {
            final HttpsURLConnection connection = (HttpsURLConnection)new URL(String.format("https://api.ashcon.app/mojang/v2/user/%s", this.skin)).openConnection();
            if (connection.getResponseCode() == 200) {
                final ArrayList<String> lines = new ArrayList<String>();
                final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                reader.lines().forEach(lines::add);
                final String reply = String.join(" ", lines);
                final int indexOfValue = reply.indexOf("\"value\": \"");
                final int indexOfSignature = reply.indexOf("\"signature\": \"");
                final String skin = reply.substring(indexOfValue + 10, reply.indexOf("\"", indexOfValue + 10));
                final String signature = reply.substring(indexOfSignature + 14, reply.indexOf("\"", indexOfSignature + 14));
                this.gameProfile.getProperties().put((String)"textures", new Property("textures", skin, signature));

            }
            else {
                Bukkit.getConsoleSender().sendMessage("Connection could not be opened when fetching player skin (Response code " + connection.getResponseCode() + ", " + connection.getResponseMessage() + ")");
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        final DataWatcher watcher = this.npc.getDataWatcher();
        watcher.set(new DataWatcherObject<>(16, DataWatcherRegistry.a), (byte)127);
        final PacketPlayOutEntityMetadata packet = new PacketPlayOutEntityMetadata(this.npc.getId(), watcher, true);
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
        this.addNPCPacket(player);
    }
    
    public String getName() {
        return this.name;
    }
    
    public EntityPlayer getEntityPlayer() {
        return this.npc;
    }
    
    public static List<NPC> getNPCs() {
        return me.unldenis.bedwars.npc.NPC.NPC;
    }
    
    public boolean hasSkin() {
        return this.skin != null;
    }
}
