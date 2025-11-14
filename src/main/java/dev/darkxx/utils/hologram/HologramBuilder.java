package dev.darkxx.utils.hologram;

import dev.darkxx.utils.location.LocationUtils;
import dev.darkxx.utils.world.Worlds;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/hologram/HologramBuilder.class */
public class HologramBuilder {
    private List<Component> lines;
    private Location location;
    private double lineHeight;
    private boolean visible;
    private boolean deleted;

    public HologramBuilder() {
        this.deleted = false;
        this.visible = true;
        this.lineHeight = 0.25d;
        World world = Worlds.world("world");
        double y = world.getHighestBlockAt(0, 0).getLocation().clone().add(0.0d, 4.0d, 0.0d).getY();
        this.location = new Location(world, 0.0d, y, 0.0d);
        this.lines = new ArrayList();
    }

    public HologramBuilder(@NotNull Location location) {
        this.deleted = false;
        this.visible = true;
        this.lineHeight = 0.25d;
        this.location = location;
        this.lines = new ArrayList();
    }

    @NotNull
    public static HologramBuilder hologram() {
        return new HologramBuilder();
    }

    @NotNull
    public static HologramBuilder hologram(@NotNull Location location) {
        return new HologramBuilder(location);
    }

    @NotNull
    public HologramBuilder lines(@NotNull Component... lines) {
        this.lines = Arrays.asList(lines);
        return this;
    }

    @NotNull
    public HologramBuilder lines(@NotNull List<Component> lines) {
        this.lines = lines;
        return this;
    }

    @NotNull
    public HologramBuilder location(@NotNull Location location) {
        this.location = location;
        return this;
    }

    @NotNull
    public HologramBuilder lineHeight(double lineHeight) {
        this.lineHeight = lineHeight;
        return this;
    }

    @NotNull
    public HologramBuilder visible(boolean visible) {
        this.visible = visible;
        return this;
    }

    @NotNull
    public HologramBuilder delete() {
        Objects.requireNonNull(this.location, "Location for hologram cannot be null");
        Objects.requireNonNull(this.lines, "Lines of hologram cannot be null");
        Objects.requireNonNull(this.location.getWorld(), "World of hologram's location cannot be null");
        ArmorStand toDelete = null;
        Iterator it = this.location.getWorld().getEntitiesByClass(ArmorStand.class).iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            ArmorStand stand = (ArmorStand) it.next();
            if (stand.getLocation() == this.location && stand.getArrowCooldown() == 1) {
                toDelete = stand;
                break;
            }
        }
        Objects.requireNonNull(toDelete, "No hologram found at <location>. Seems to be a NPE".replaceAll("<location>", LocationUtils.serialize(this.location)));
        toDelete.remove();
        this.deleted = true;
        return this;
    }

    @NotNull
    public HologramBuilder build() {
        Objects.requireNonNull(this.location, "Location for hologram cannot be null");
        Objects.requireNonNull(this.lines, "Lines of hologram cannot be null");
        if (this.deleted) {
            throw new NullPointerException("This hologram has previously been deleted therefore it was not found.");
        }
        for (Component text : this.lines) {
            Objects.requireNonNull(this.location.getWorld(), "World of hologram's location cannot be null");
            ArmorStand stand = (ArmorStand) this.location.getWorld().spawnEntity(this.location, EntityType.ARMOR_STAND);
            stand.setGravity(false);
            stand.setInvisible(true);
            stand.setInvulnerable(true);
            stand.setCustomNameVisible(true);
            stand.customName(text);
            stand.setSilent(true);
            stand.setAI(false);
            stand.setArrowCooldown(1);
            stand.setBasePlate(false);
            stand.setCanPickupItems(false);
            stand.setCollidable(false);
            stand.setPersistent(true);
            this.location = this.location.subtract(0.0d, this.lineHeight, 0.0d);
        }
        return this;
    }
}
