package dev.darkxx.utils.menu.xmenu;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/menu/xmenu/GuiManager.class */
public final class GuiManager {
    private static final AtomicBoolean REGISTERED = new AtomicBoolean(false);

    private GuiManager() {
        throw new UnsupportedOperationException();
    }

    public static void register(Plugin plugin) {
        Objects.requireNonNull(plugin, "plugin");
        if (REGISTERED.getAndSet(true)) {
            throw new IllegalStateException("XMenus is already registered");
        }
        Bukkit.getPluginManager().registerEvents(new InventoryListener(plugin), plugin);
    }

    public static void closeAll() {
        Bukkit.getOnlinePlayers().stream().filter(p -> {
            return p.getOpenInventory().getTopInventory().getHolder() instanceof GuiBuilder;
        }).forEach((v0) -> {
            v0.closeInventory();
        });
    }

    /* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/menu/xmenu/GuiManager$InventoryListener.class */
    public static final class InventoryListener implements Listener {
        private final Plugin plugin;

        public InventoryListener(Plugin plugin) {
            this.plugin = plugin;
        }

        @EventHandler
        public void onInventoryClick(InventoryClickEvent e) {
            Inventory clickedInventory = e.getClickedInventory();
            if (clickedInventory != null && (e.getInventory().getHolder() instanceof GuiBuilder)) {
                GuiBuilder inv = (GuiBuilder) e.getInventory().getHolder();
                if (clickedInventory.equals(e.getView().getTopInventory())) {
                    e.setCancelled(true);
                }
                inv.handleClick(e);
            }
        }

        @EventHandler
        public void onInventoryOpen(InventoryOpenEvent e) {
            if (e.getInventory().getHolder() instanceof GuiBuilder) {
                GuiBuilder inv = (GuiBuilder) e.getInventory().getHolder();
                inv.handleOpen(e);
            }
        }

        @EventHandler
        public void onInventoryClose(InventoryCloseEvent e) {
            if (e.getInventory().getHolder() instanceof GuiBuilder) {
                GuiBuilder inv = (GuiBuilder) e.getInventory().getHolder();
                if (inv.handleClose(e)) {
                    Bukkit.getScheduler().runTask(this.plugin, () -> {
                        inv.open((Player) e.getPlayer());
                    });
                }
            }
        }

        @EventHandler
        public void onPluginDisable(PluginDisableEvent e) {
            if (e.getPlugin() == this.plugin) {
                GuiManager.closeAll();
                GuiManager.REGISTERED.set(false);
            }
        }
    }
}
