package dev.darkxx.utils.event.crystal.impl;

import dev.darkxx.utils.event.crystal.PlayerCrystalDeathEvent;
import dev.darkxx.utils.library.Utils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/event/crystal/impl/SpigotCrystalEventListener.class */
public class SpigotCrystalEventListener implements Listener {
    private Entity crystal;
    private Player victim;
    private Player killer;
    private EntityDamageByEntityEvent playerDamageEvent;

    private void reset() {
        this.crystal = null;
        this.victim = null;
        this.killer = null;
        this.playerDamageEvent = null;
    }

    @EventHandler
    public void onCrystalExplode(EntityDamageByEntityEvent event) {
        if (event.getEntityType() == EntityType.ENDER_CRYSTAL && event.getDamager().getType() == EntityType.PLAYER) {
            reset();
            this.crystal = event.getEntity();
            this.killer = (Player) event.getDamager();
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (event.getEntityType() != EntityType.PLAYER) {
            return;
        }
        if (this.crystal == null) {
            reset();
        } else if (event.getDamager() != this.crystal) {
            reset();
        } else {
            this.victim = (Player) event.getEntity();
            this.playerDamageEvent = event;
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (this.crystal == null || this.killer == null || this.victim == null || this.playerDamageEvent == null || event.getEntity() != this.victim) {
            return;
        }
        PlayerCrystalDeathEvent calledEvent = new PlayerCrystalDeathEvent(this.killer, this.victim, this.crystal, this.playerDamageEvent, event);
        Utils.plugin().getServer().getPluginManager().callEvent(calledEvent);
        reset();
    }
}
