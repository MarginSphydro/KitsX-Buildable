package dev.darkxx.utils.menu.normal;

import com.mojang.brigadier.CommandDispatcher;
import dev.darkxx.utils.item.ItemBuilder;
import dev.darkxx.utils.library.Utils;
import dev.darkxx.utils.menu.Button;
import dev.darkxx.utils.menu.MenuBase;
import dev.darkxx.utils.menu.listener.CloseListener;
import dev.darkxx.utils.menu.listener.DragListener;
import dev.darkxx.utils.scheduler.Schedulers;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.logging.Level;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/menu/normal/Menu.class */
public class Menu implements InventoryHolder, MenuBase<Menu> {
    private final Inventory inventory;
    private final Component title;
    private final int size;
    private final Map<Integer, Button> buttons = new HashMap();
    private final Map<Integer, ItemBuilder> items = new HashMap();
    private CloseListener onClose;
    private DragListener onDrag;

    public Menu(@NotNull Component title, int size) {
        this.inventory = Utils.plugin().getServer().createInventory(this, size, title);
        this.title = title;
        this.size = size;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dev.darkxx.utils.menu.MenuBase
    @NotNull
    public Menu button(int slot, @NotNull Button button) {
        this.buttons.put(Integer.valueOf(slot), button);
        this.inventory.setItem(slot, button.item().build());
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dev.darkxx.utils.menu.MenuBase
    @NotNull
    public Menu item(int slot, @NotNull ItemBuilder item) {
        this.items.put(Integer.valueOf(slot), item);
        this.inventory.setItem(slot, item.build());
        return this;
    }

    @NotNull
    public static Menu menu(@NotNull Component title, int size) {
        return new Menu(title, size);
    }

    @NotNull
    public static Menu menu(@NotNull Component title, int width, int height) {
        return menu(title, width * height);
    }

    @Override // dev.darkxx.utils.menu.MenuBase
    public int size() {
        return this.size;
    }

    @Override // dev.darkxx.utils.menu.MenuBase
    @NotNull
    public Component title() {
        return this.title;
    }

    @Override // dev.darkxx.utils.menu.MenuBase
    public void open(@NotNull Player player) {
        player.openInventory(this.inventory);
        for (Button button : this.buttons.values()) {
            if (button.isRefreshingButton()) {
                Schedulers.builder((Consumer<BukkitTask>) task -> {
                    if (player.getOpenInventory().getTopInventory() != getInventory()) {
                        task.cancel();
                        return;
                    }
                    int slot = slotByButton(button);
                    getInventory().clear(slot);
                    getInventory().setItem(slot, button.item().build());
                }).config(config -> {
                    if (button.isRefreshingAsync()) {
                        config.async();
                    } else {
                        config.sync();
                    }
                    config.afterTicks(Integer.valueOf((int) button.refreshDelay()));
                    config.everyTicks(Integer.valueOf((int) button.refreshPeriod()));
                }).execute();
            }
        }
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dev.darkxx.utils.menu.MenuBase
    @NotNull
    public Menu border(@NotNull Button borderItem, @NotNull String... borderPatterns) {
        int row = 0;
        for (String borderPattern : borderPatterns) {
            if (row < this.size) {
                String[] rowCharacters = borderPattern.split(CommandDispatcher.ARGUMENT_SEPARATOR);
                for (int col = 0; col < rowCharacters.length && col < 9; col++) {
                    String character = rowCharacters[col];
                    if (character.equals("X")) {
                        this.inventory.setItem(col + (row * 9), borderItem.item().build().clone());
                        this.buttons.put(Integer.valueOf(col + (row * 9)), borderItem);
                    }
                }
                row++;
            }
        }
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dev.darkxx.utils.menu.MenuBase
    @NotNull
    public Menu fill(@NotNull Object filler, @NotNull String... pattern) {
        int row = 0;
        for (String rowPattern : pattern) {
            if (row < this.size / 9) {
                String[] rowCharacters = rowPattern.split(CommandDispatcher.ARGUMENT_SEPARATOR);
                for (int col = 0; col < rowCharacters.length && col < 9; col++) {
                    String character = rowCharacters[col];
                    int slot = col + (row * 9);
                    if (character.equals("X")) {
                        if (filler instanceof Button) {
                            button(slot, (Button) filler);
                        } else if (filler instanceof ItemBuilder) {
                            item(slot, (ItemBuilder) filler);
                        } else {
                            int callersLineNumber = Thread.currentThread().getStackTrace()[2].getLineNumber();
                            Utils.plugin().getLogger().log(Level.WARNING, "You can not use Menu.fill(Object filler, String... pattern) with a object different than an ItemBuilder or Button!");
                            Utils.plugin().getLogger().log(Level.WARNING, "    Line: [" + callersLineNumber + "], FileBuilder: [" + Thread.currentThread().getStackTrace()[2].getFileName() + "]");
                        }
                    }
                }
                row++;
            }
        }
        return this;
    }

    @Nullable
    public ItemBuilder item(int slot) {
        return this.items.get(Integer.valueOf(slot));
    }

    @Nullable
    public Button button(int slot) {
        return this.buttons.get(Integer.valueOf(slot));
    }

    public int slotByButton(@NotNull Button button) {
        for (Map.Entry<Integer, Button> entry : this.buttons.entrySet()) {
            if (entry.getValue() == button) {
                return entry.getKey().intValue();
            }
        }
        return -1;
    }

    public int slotByItem(@NotNull ItemBuilder item) {
        for (Map.Entry<Integer, ItemBuilder> entry : this.items.entrySet()) {
            if (entry.getValue() == item) {
                return entry.getKey().intValue();
            }
        }
        return -1;
    }

    @NotNull
    public Inventory getInventory() {
        return this.inventory;
    }

    @Override // dev.darkxx.utils.menu.MenuBase
    @NotNull
    public Inventory inventory() {
        return getInventory();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dev.darkxx.utils.menu.MenuBase
    @NotNull
    public Menu type() {
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dev.darkxx.utils.menu.MenuBase
    @NotNull
    public Menu onClose(@Nullable CloseListener onClose) {
        this.onClose = onClose;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dev.darkxx.utils.menu.MenuBase
    @NotNull
    public Menu onDrag(@Nullable DragListener onDrag) {
        this.onDrag = onDrag;
        return this;
    }

    public CloseListener onClose() {
        return this.onClose;
    }

    public DragListener onDrag() {
        return this.onDrag;
    }
}
