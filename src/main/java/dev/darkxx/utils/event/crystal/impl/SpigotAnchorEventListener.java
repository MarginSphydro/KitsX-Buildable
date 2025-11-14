package dev.darkxx.utils.event.crystal.impl;

import dev.darkxx.utils.event.crystal.PlayerAnchorDeathEvent;
import dev.darkxx.utils.library.Utils;
import dev.darkxx.utils.player.PlayerUtils;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.type.RespawnAnchor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/event/crystal/impl/SpigotAnchorEventListener.class */
public class SpigotAnchorEventListener implements Listener {
    private static final Map<Location, UUID> DETONATOR_MAP = new HashMap();

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        ItemStack item;
        try {
            if (event.getClickedBlock() == null || !event.hasBlock() || event.getMaterial() != Material.RESPAWN_ANCHOR) {
                return;
            }
            RespawnAnchor blockData = (RespawnAnchor) event.getClickedBlock().getBlockData();
            if (blockData instanceof RespawnAnchor) {
                RespawnAnchor anchorBlock = blockData;
                if (event.getItem() == null) {
                    item = new ItemStack(Material.AIR);
                } else {
                    item = event.getItem();
                }
                ItemStack itemHand = item;
                if (anchorBlock.getCharges() > 3) {
                    return;
                }
                if (itemHand.getType() != Material.GLOWSTONE || anchorBlock.getCharges() == 0) {
                    DETONATOR_MAP.put(event.getClickedBlock().getLocation(), event.getPlayer().getUniqueId());
                }
            }
        } catch (Exception e) {
            HandlerList.unregisterAll(this);
        }
    }

    @EventHandler
    public void onAnchorDamage(EntityDamageByBlockEvent event) {
        UUID detonator;
        Player playerDetonator;
        try {
            if (event.getCause() != EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) {
                return;
            }
            Player entity = (Player) event.getEntity();
            if (entity instanceof Player) {
                Player victim = entity;
                if (event.getEntityType() != EntityType.PLAYER || victim.getHealth() > event.getDamage() || victim.getInventory().getItemInMainHand().getType() == Material.TOTEM_OF_UNDYING || victim.getInventory().getItemInOffHand().getType() == Material.TOTEM_OF_UNDYING || event.getDamagerBlockState() == null || event.getDamagerBlockState().getType() != Material.RESPAWN_ANCHOR || victim.getInventory().getItemInMainHand().getType() == Material.TOTEM_OF_UNDYING || victim.getInventory().getItemInOffHand().getType() == Material.TOTEM_OF_UNDYING || (detonator = DETONATOR_MAP.get(new Location(victim.getWorld(), event.getDamagerBlockState().getX(), event.getDamagerBlockState().getY(), event.getDamagerBlockState().getZ()))) == null || (playerDetonator = PlayerUtils.player(detonator)) == null) {
                    return;
                }
                PlayerAnchorDeathEvent calledEvent = new PlayerAnchorDeathEvent(event.getDamagerBlockState(), victim, playerDetonator);
                Utils.plugin().getServer().getPluginManager().callEvent(calledEvent);
                DETONATOR_MAP.remove(event.getDamagerBlockState().getLocation());
            }
        } catch (Exception e) {
            HandlerList.unregisterAll(this);
        }
    }
}
