package dev.darkxx.utils.event.impl;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import dev.darkxx.utils.event.worldguard.RegionEnterEvent;
import dev.darkxx.utils.event.worldguard.RegionLeaveEvent;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.java.JavaPlugin;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/event/impl/RegionListener.class */
public class RegionListener implements Listener {
    private final Map<Player, Set<ProtectedRegion>> playerRegions = new HashMap();

    public RegionListener(JavaPlugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        checkRegion(event.getPlayer(), event.getTo(), event.getFrom());
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        checkRegion(event.getPlayer(), event.getTo(), event.getFrom());
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        checkRegion(event.getPlayer(), event.getRespawnLocation(), event.getPlayer().getLocation());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        this.playerRegions.remove(player);
    }

    private void checkRegion(Player player, Location to, Location from) {
        if (to != null) {
            if (to.getBlockX() == from.getBlockX() && to.getBlockY() == from.getBlockY() && to.getBlockZ() == from.getBlockZ()) {
                return;
            }
            World world = BukkitAdapter.adapt(player.getWorld());
            RegionContainer regionContainer = WorldGuard.getInstance().getPlatform().getRegionContainer();
            RegionManager regionManager = regionContainer.get(world);
            if (regionManager == null) {
                return;
            }
            ApplicableRegionSet fromRegions = regionManager.getApplicableRegions(BukkitAdapter.asBlockVector(from));
            ApplicableRegionSet toRegions = regionManager.getApplicableRegions(BukkitAdapter.asBlockVector(to));
            Set<ProtectedRegion> enteredRegions = new HashSet<>(toRegions.getRegions());
            Set<ProtectedRegion> exitedRegions = new HashSet<>(fromRegions.getRegions());
            enteredRegions.removeAll(fromRegions.getRegions());
            exitedRegions.removeAll(toRegions.getRegions());
            for (ProtectedRegion region : enteredRegions) {
                Bukkit.getPluginManager().callEvent(new RegionEnterEvent(player, region));
            }
            for (ProtectedRegion region2 : exitedRegions) {
                Bukkit.getPluginManager().callEvent(new RegionLeaveEvent(player, region2));
            }
            this.playerRegions.put(player, new HashSet(toRegions.getRegions()));
        }
    }
}
