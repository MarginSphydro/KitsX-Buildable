package dev.darkxx.utils.parser.menu;

import dev.darkxx.utils.menu.MenuBase;
import dev.darkxx.utils.registration.Registrar;
import dev.darkxx.utils.scheduler.Schedulers;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.jetbrains.annotations.NotNull;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/parser/menu/ParsedMenu.class */
public abstract class ParsedMenu<M extends MenuBase<M>> implements Listener {
    protected final Player player;
    protected final MenuParser<M> parser;

    public abstract void init();

    public ParsedMenu(@NotNull Player player, @NotNull Class<M> menuType, @NotNull String fileName) {
        this.player = player;
        this.parser = MenuParser.wrap(Menus.load(fileName), menuType);
        this.parser.menu();
        Registrar.events(this);
    }

    @EventHandler
    public final void execute(@NotNull InventoryCloseEvent event) {
        if (event.getInventory().equals(this.parser.menu().inventory())) {
            onClose(event);
            Schedulers.async().execute(() -> {
                HandlerList.unregisterAll(this);
            }, 10);
        }
    }

    public void onClose(@NotNull InventoryCloseEvent event) {
    }

    @EventHandler
    public final void execute(@NotNull InventoryClickEvent event) {
        if (event.getInventory().equals(this.parser.menu().inventory())) {
            onClick(event);
            Schedulers.async().execute(() -> {
                HandlerList.unregisterAll(this);
            }, 10);
        }
    }

    public void onClick(@NotNull InventoryClickEvent event) {
    }

    @NotNull
    public final Player player() {
        return this.player;
    }

    @NotNull
    public final MenuParser<M> parser() {
        return this.parser;
    }
}
