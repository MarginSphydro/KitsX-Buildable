package dev.darkxx.utils.menu.listener;

import dev.darkxx.utils.item.ItemBuilder;
import dev.darkxx.utils.menu.Button;
import dev.darkxx.utils.menu.normal.Menu;
import dev.darkxx.utils.menu.paginated.PaginatedMenu;
import dev.darkxx.utils.menu.paginated.PaginatedSlot;
import java.util.Iterator;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.InventoryHolder;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/menu/listener/MenuListener.class */
public class MenuListener implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Button button;
        Button button2;
        Button.ButtonListener listener;
        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        InventoryHolder holder = event.getInventory().getHolder();
        if ((holder instanceof Menu) && (button2 = ((Menu) holder).button(event.getSlot())) != null && (listener = button2.listener()) != null) {
            listener.onClick(event);
        }
        InventoryHolder holder2 = event.getInventory().getHolder();
        if (holder2 instanceof PaginatedMenu) {
            PaginatedMenu menu = (PaginatedMenu) holder2;
            Button stickyButton = menu.stickyButton(event.getSlot());
            if (menu.currentPageItemEnabled() && event.getSlot() == menu.currentPageSlot()) {
                event.setCancelled(true);
            }
            Iterator<Map.Entry<Integer, ItemBuilder>> it = menu.previousButton().entrySet().iterator();
            while (it.hasNext()) {
                if (event.getSlot() == it.next().getKey().intValue()) {
                    event.setCancelled(true);
                    if (menu.currentPage() > 1) {
                        menu.open(player, menu.currentPage() - 1);
                    }
                }
            }
            Iterator<Map.Entry<Integer, ItemBuilder>> it2 = menu.nextButton().entrySet().iterator();
            while (it2.hasNext()) {
                if (event.getSlot() == it2.next().getKey().intValue()) {
                    event.setCancelled(true);
                    if (menu.totalPages() > 1) {
                        menu.open(player, menu.currentPage() + 1);
                    }
                }
            }
            Iterator<Map.Entry<Integer, Button>> it3 = menu.stickyButtons().entrySet().iterator();
            while (it3.hasNext()) {
                if (event.getSlot() == it3.next().getKey().intValue() && stickyButton != null && stickyButton.listener() != null) {
                    stickyButton.listener().onClick(event);
                }
            }
            for (Map.Entry<PaginatedSlot, Button> entry : menu.buttons().entrySet()) {
                PaginatedSlot slotHolder = entry.getKey();
                if (slotHolder.slot() == event.getSlot() && slotHolder.page() == menu.currentPage() && (button = entry.getValue()) != null && button.listener() != null) {
                    button.listener().onClick(event);
                }
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        event.getPlayer();
        InventoryHolder holder = event.getInventory().getHolder();
        if (holder instanceof Menu) {
            Menu menu = (Menu) holder;
            if (menu.onClose() != null) {
                menu.onClose().onClose(event);
                return;
            }
            return;
        }
        InventoryHolder holder2 = event.getInventory().getHolder();
        if (holder2 instanceof PaginatedMenu) {
            PaginatedMenu menu2 = (PaginatedMenu) holder2;
            if (menu2.onClose() != null) {
                menu2.onClose().onClose(event);
            }
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        event.getWhoClicked();
        InventoryHolder holder = event.getInventory().getHolder();
        if (holder instanceof Menu) {
            Menu menu = (Menu) holder;
            if (menu.onDrag() != null) {
                menu.onDrag().onDrag(event);
                return;
            }
            return;
        }
        InventoryHolder holder2 = event.getInventory().getHolder();
        if (holder2 instanceof PaginatedMenu) {
            PaginatedMenu menu2 = (PaginatedMenu) holder2;
            if (menu2.onDrag() != null) {
                menu2.onDrag().onDrag(event);
            }
        }
    }
}
