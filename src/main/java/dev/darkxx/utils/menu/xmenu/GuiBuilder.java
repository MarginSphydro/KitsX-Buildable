package dev.darkxx.utils.menu.xmenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/menu/xmenu/GuiBuilder.class */
public class GuiBuilder implements InventoryHolder {
    private final Map<Integer, Consumer<InventoryClickEvent>> itemHandlers;
    private final List<Consumer<InventoryOpenEvent>> openHandlers;
    private final List<Consumer<InventoryCloseEvent>> closeHandlers;
    private final List<Consumer<InventoryClickEvent>> clickHandlers;
    private final Inventory inventory;
    private Predicate<Player> closeFilter;

    public GuiBuilder(int size) {
        this((Function<InventoryHolder, Inventory>) owner -> {
            return Bukkit.createInventory(owner, size);
        });
    }

    public GuiBuilder(int size, String title) {
        this((Function<InventoryHolder, Inventory>) owner -> {
            return Bukkit.createInventory(owner, size, title);
        });
    }

    public GuiBuilder(InventoryType type) {
        this((Function<InventoryHolder, Inventory>) owner -> {
            return Bukkit.createInventory(owner, type);
        });
    }

    public GuiBuilder(InventoryType type, String title) {
        this((Function<InventoryHolder, Inventory>) owner -> {
            return Bukkit.createInventory(owner, type, title);
        });
    }

    public GuiBuilder(Function<InventoryHolder, Inventory> inventoryFunction) {
        this.itemHandlers = new HashMap();
        this.openHandlers = new ArrayList();
        this.closeHandlers = new ArrayList();
        this.clickHandlers = new ArrayList();
        Objects.requireNonNull(inventoryFunction, "inventoryFunction");
        Inventory inv = inventoryFunction.apply(this);
        if (inv.getHolder() != this) {
            throw new IllegalStateException("Inventory holder is not FastInv, found: " + String.valueOf(inv.getHolder()));
        }
        this.inventory = inv;
    }

    protected void onOpen(InventoryOpenEvent event) {
    }

    protected void onClick(InventoryClickEvent event) {
    }

    protected void onClose(InventoryCloseEvent event) {
    }

    public void addItem(ItemStack item) {
        addItem(item, null);
    }

    public void addItem(ItemStack item, Consumer<InventoryClickEvent> handler) {
        int slot = this.inventory.firstEmpty();
        if (slot >= 0) {
            setItem(slot, item, handler);
        }
    }

    public void setItem(int slot, ItemStack item) {
        setItem(slot, item, null);
    }

    public void setItem(int slot, ItemStack item, Consumer<InventoryClickEvent> handler) {
        this.inventory.setItem(slot, item);
        if (handler != null) {
            this.itemHandlers.put(Integer.valueOf(slot), handler);
        } else {
            this.itemHandlers.remove(Integer.valueOf(slot));
        }
    }

    public void setItems(int slotFrom, int slotTo, ItemStack item) {
        setItems(slotFrom, slotTo, item, null);
    }

    public void setItems(int slotFrom, int slotTo, ItemStack item, Consumer<InventoryClickEvent> handler) {
        for (int i = slotFrom; i <= slotTo; i++) {
            setItem(i, item, handler);
        }
    }

    public void setItems(int[] slots, ItemStack item) {
        setItems(slots, item, (Consumer<InventoryClickEvent>) null);
    }

    public void setItems(int[] slots, ItemStack item, Consumer<InventoryClickEvent> handler) {
        for (int slot : slots) {
            setItem(slot, item, handler);
        }
    }

    public void removeItem(int slot) {
        this.inventory.clear(slot);
        this.itemHandlers.remove(Integer.valueOf(slot));
    }

    public void removeItems(int... slots) {
        for (int slot : slots) {
            removeItem(slot);
        }
    }

    public void fillEmptySlots(ItemStack fillerItem) {
        for (int i = 0; i < this.inventory.getSize(); i++) {
            if (this.inventory.getItem(i) == null || this.inventory.getItem(i).getType() == Material.AIR) {
                setItem(i, fillerItem);
            }
        }
    }

    public void setCloseFilter(Predicate<Player> closeFilter) {
        this.closeFilter = closeFilter;
    }

    public void addOpenHandler(Consumer<InventoryOpenEvent> openHandler) {
        this.openHandlers.add(openHandler);
    }

    public void addCloseHandler(Consumer<InventoryCloseEvent> closeHandler) {
        this.closeHandlers.add(closeHandler);
    }

    public void addClickHandler(Consumer<InventoryClickEvent> clickHandler) {
        this.clickHandlers.add(clickHandler);
    }

    public void open(Player player) {
        player.openInventory(this.inventory);
    }

    public int[] getBorders() {
        int size = this.inventory.getSize();
        return IntStream.range(0, size).filter(i -> {
            return size < 27 || i < 9 || i % 9 == 0 || (i - 8) % 9 == 0 || i > size - 9;
        }).toArray();
    }

    public int[] getCorners() {
        int size = this.inventory.getSize();
        return IntStream.range(0, size).filter(i -> {
            return i < 2 || (i > 6 && i < 10) || i == 17 || i == size - 18 || ((i > size - 11 && i < size - 7) || i > size - 3);
        }).toArray();
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    void handleOpen(InventoryOpenEvent e) {
        onOpen(e);
        this.openHandlers.forEach(c -> {
            c.accept(e);
        });
    }

    boolean handleClose(InventoryCloseEvent e) {
        onClose(e);
        this.closeHandlers.forEach(c -> {
            c.accept(e);
        });
        return this.closeFilter != null && this.closeFilter.test((Player) e.getPlayer());
    }

    void handleClick(InventoryClickEvent e) {
        onClick(e);
        this.clickHandlers.forEach(c -> {
            c.accept(e);
        });
        Consumer<InventoryClickEvent> clickConsumer = this.itemHandlers.get(Integer.valueOf(e.getRawSlot()));
        if (clickConsumer != null) {
            clickConsumer.accept(e);
        }
    }
}
