package dev.darkxx.utils.event.crystal;

import org.bukkit.block.BlockState;
import org.bukkit.block.data.type.RespawnAnchor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/event/crystal/PlayerAnchorDeathEvent.class */
public class PlayerAnchorDeathEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();

    @NotNull
    private final BlockState block;

    @NotNull
    private final Player victim;

    @NotNull
    private final Player attacker;

    @Nullable
    private final RespawnAnchor anchor;

    public PlayerAnchorDeathEvent(@NotNull BlockState block, @NotNull Player victim, @NotNull Player attacker) {
        this.block = block;
        this.victim = victim;
        this.attacker = attacker;
        RespawnAnchor respawnAnchor = (RespawnAnchor) this.block;
        if (respawnAnchor instanceof RespawnAnchor) {
            RespawnAnchor respawnAnchor2 = respawnAnchor;
            this.anchor = respawnAnchor2;
        } else {
            this.anchor = null;
        }
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @NotNull
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    @NotNull
    public HandlerList handlers() {
        return getHandlers();
    }

    @NotNull
    public String getEventName() {
        return "PlayerAnchorDeathEvent";
    }

    @NotNull
    public String eventName() {
        return getEventName();
    }

    @NotNull
    public BlockState block() {
        return this.block;
    }

    @NotNull
    public Player victim() {
        return this.victim;
    }

    @NotNull
    public Player attacker() {
        return this.attacker;
    }

    @Nullable
    public RespawnAnchor anchor() {
        return this.anchor;
    }
}
