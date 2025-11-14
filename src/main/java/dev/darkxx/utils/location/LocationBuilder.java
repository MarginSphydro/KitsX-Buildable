package dev.darkxx.utils.location;

import dev.darkxx.utils.world.Worlds;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/location/LocationBuilder.class */
public class LocationBuilder {
    private double x;
    private double y;
    private double z;
    private float pitch;
    private float yaw;
    private World world;

    public LocationBuilder() {
        this.x = 0.0d;
        this.y = 64.0d;
        this.z = 0.0d;
        this.world = Worlds.world("world");
        this.pitch = 0.0f;
        this.yaw = 0.0f;
    }

    public LocationBuilder(double x, double y, double z, @NotNull World world) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;
    }

    public LocationBuilder(double x, double y, double z, @NotNull World world, float pitch, float yaw) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;
        this.pitch = pitch;
        this.yaw = yaw;
    }

    @NotNull
    public static LocationBuilder location() {
        return new LocationBuilder();
    }

    @NotNull
    public static LocationBuilder location(double x, double y, double z, @NotNull World world) {
        return new LocationBuilder(x, y, z, world);
    }

    @NotNull
    public static LocationBuilder location(double x, double y, double z, @NotNull World world, float pitch, float yaw) {
        return new LocationBuilder(x, y, z, world, pitch, yaw);
    }

    @NotNull
    public LocationBuilder x(double x) {
        this.x = x;
        return this;
    }

    @NotNull
    public LocationBuilder y(double y) {
        this.y = y;
        return this;
    }

    @NotNull
    public LocationBuilder z(double z) {
        this.z = z;
        return this;
    }

    @NotNull
    public LocationBuilder world(@NotNull World world) {
        this.world = world;
        return this;
    }

    @NotNull
    public LocationBuilder pitch(float pitch) {
        this.pitch = pitch;
        return this;
    }

    @NotNull
    public LocationBuilder yaw(float yaw) {
        this.yaw = yaw;
        return this;
    }

    @NotNull
    public Location build() {
        return new Location(this.world, this.x, this.y, this.z, this.yaw, this.pitch);
    }
}
