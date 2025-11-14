package dev.darkxx.utils.menu.paginated;

import com.mojang.brigadier.CommandDispatcher;
import dev.darkxx.utils.item.ItemBuilder;
import dev.darkxx.utils.library.Utils;
import dev.darkxx.utils.menu.Button;
import dev.darkxx.utils.menu.MenuBase;
import dev.darkxx.utils.menu.listener.CloseListener;
import dev.darkxx.utils.menu.listener.DragListener;
import dev.darkxx.utils.scheduler.Schedulers;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;
import java.util.logging.Level;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/menu/paginated/PaginatedMenu.class */
public class PaginatedMenu implements InventoryHolder, MenuBase<PaginatedMenu> {
    private final Inventory inventory;
    private final Component title;
    private final int size;
    private int totalPages;
    private boolean currentPageItemEnabled;
    private ItemBuilder currentPageItem;
    private int currentPageSlot;
    private CloseListener onClose;
    private DragListener onDrag;
    private final Map<PaginatedSlot, Button> buttons = new HashMap();
    private final Map<PaginatedSlot, ItemBuilder> items = new HashMap();
    private final Map<Integer, ItemBuilder> previousButton = new HashMap();
    private int currentPage = 1;
    private final Map<Integer, ItemBuilder> nextButton = new HashMap();
    private final Map<Button, String[]> borderMap = new HashMap();
    private final Map<Integer, Button> stickyButtons = new HashMap();

    public PaginatedMenu(@NotNull Component title, int size) {
        this.inventory = Bukkit.createInventory(this, size, title);
        this.title = title;
        this.size = size;
    }

    @Override // dev.darkxx.utils.menu.MenuBase
    @NotNull
    public Component title() {
        return this.title;
    }

    @Override // dev.darkxx.utils.menu.MenuBase
    public int size() {
        return this.size;
    }

    @NotNull
    public Map<PaginatedSlot, Button> buttons() {
        return this.buttons;
    }

    @NotNull
    public Map<Integer, ItemBuilder> previousButton() {
        return this.previousButton;
    }

    @NotNull
    public Map<Integer, ItemBuilder> nextButton() {
        return this.nextButton;
    }

    @NotNull
    public Map<Button, String[]> borderMap() {
        return this.borderMap;
    }

    @NotNull
    public Map<Integer, Button> stickyButtons() {
        return this.stickyButtons;
    }

    @NotNull
    public Map<PaginatedSlot, ItemBuilder> items() {
        return this.items;
    }

    public int totalPages() {
        return this.totalPages;
    }

    @NotNull
    public PaginatedMenu totalPages(int totalPages) {
        this.totalPages = totalPages;
        return this;
    }

    public int currentPage() {
        return this.currentPage;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dev.darkxx.utils.menu.MenuBase
    @NotNull
    public PaginatedMenu button(@NotNull PaginatedSlot where, @NotNull Button button) {
        this.buttons.put(where, button);
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dev.darkxx.utils.menu.MenuBase
    @NotNull
    public PaginatedMenu item(@NotNull PaginatedSlot where, @Nullable ItemBuilder item) {
        this.items.put(where, item);
        return this;
    }

    @NotNull
    public static PaginatedMenu menu(@NotNull Component title, int size) {
        return new PaginatedMenu(title, size);
    }

    @NotNull
    public static PaginatedMenu menu(@NotNull Component title, int width, int height) {
        return menu(title, width * height);
    }

    @NotNull
    public PaginatedMenu stickyButton(int where, @NotNull Button button) {
        this.stickyButtons.put(Integer.valueOf(where), button);
        return this;
    }

    @NotNull
    public PaginatedMenu paginationButtons(int previousItemSlot, @NotNull ItemBuilder previousItem, int nextItemSlot, @NotNull ItemBuilder nextItem) {
        this.inventory.setItem(previousItemSlot, previousItem.build());
        this.previousButton.put(Integer.valueOf(previousItemSlot), previousItem);
        this.inventory.setItem(nextItemSlot, nextItem.build());
        this.nextButton.put(Integer.valueOf(nextItemSlot), nextItem);
        return this;
    }

    @NotNull
    public PaginatedMenu currentPageButton(int slot, @NotNull ItemBuilder item) {
        this.currentPageItemEnabled = true;
        this.currentPageItem = item;
        this.currentPageSlot = slot;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dev.darkxx.utils.menu.MenuBase
    @NotNull
    public PaginatedMenu border(@NotNull Button borderItem, @NotNull String... borderPatterns) {
        int row = 0;
        for (String borderPattern : borderPatterns) {
            if (row < this.size) {
                String[] rowCharacters = borderPattern.split(CommandDispatcher.ARGUMENT_SEPARATOR);
                for (int col = 0; col < rowCharacters.length && col < 9; col++) {
                    String character = rowCharacters[col];
                    if (character.equals("X")) {
                        this.inventory.setItem(col + (row * 9), borderItem.item().build().clone());
                        this.borderMap.put(borderItem, borderPatterns);
                        this.buttons.put(new PaginatedSlot(col + (row * 9), this.currentPage), borderItem);
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
    public PaginatedMenu fill(@NotNull Object filler, @NotNull String... pattern) {
        int row = 0;
        for (String rowPattern : pattern) {
            if (row < this.size / 9) {
                String[] rowCharacters = rowPattern.split(CommandDispatcher.ARGUMENT_SEPARATOR);
                for (int col = 0; col < rowCharacters.length && col < 9; col++) {
                    String character = rowCharacters[col];
                    int slot = col + (row * 9);
                    if (character.equals("X")) {
                        if (filler instanceof Button) {
                            button(new PaginatedSlot(slot, this.currentPage), (Button) filler);
                        } else if (filler instanceof ItemBuilder) {
                            item(new PaginatedSlot(slot, this.currentPage), (ItemBuilder) filler);
                        } else {
                            int callersLineNumber = Thread.currentThread().getStackTrace()[2].getLineNumber();
                            Utils.plugin().getLogger().log(Level.WARNING, "You can not use PaginatedMenu.fill(Object filler, String... pattern) with a object different than an ItemBuilder or Button!");
                            Utils.plugin().getLogger().log(Level.WARNING, "    Line: [" + callersLineNumber + "], FileBuilder: [" + Thread.currentThread().getStackTrace()[2].getFileName() + "]");
                        }
                    }
                }
                row++;
            }
        }
        return this;
    }

    @NotNull
    public Map<Button, String[]> border() {
        return this.borderMap;
    }

    @Override // dev.darkxx.utils.menu.MenuBase
    public void open(@NotNull Player player) {
        open(player, 1);
    }

    @Override // dev.darkxx.utils.menu.MenuBase
    public void open(@NotNull Player player, int page) {
        this.inventory.clear();
        player.openInventory(this.inventory);
        this.currentPage = page;
        int highestPage = 1;
        Iterator<PaginatedSlot> it = this.buttons.keySet().iterator();
        while (it.hasNext()) {
            highestPage = Math.max(highestPage, it.next().page());
            this.totalPages = highestPage;
        }
        for (PaginatedSlot slotHolder : this.items.keySet()) {
            if (this.totalPages > Math.max(highestPage, slotHolder.page())) {
                highestPage = Math.max(highestPage, slotHolder.page());
                this.totalPages = highestPage;
            }
        }
        for (Button button : this.buttons.values()) {
            if (pageSlotHolderByButton(button).page() == this.currentPage) {
                if (!button.isRefreshingButton()) {
                    this.inventory.setItem(pageSlotHolderByButton(button).slot(), button.item().build());
                } else {
                    Schedulers.builder((Consumer<BukkitTask>) task -> {
                        if (player.getOpenInventory().getTopInventory() != getInventory()) {
                            task.cancel();
                            return;
                        }
                        if (pageSlotHolderByButton(button).page() != this.currentPage) {
                            task.cancel();
                            return;
                        }
                        int slot = pageSlotHolderByButton(button).slot();
                        getInventory().clear(slot);
                        getInventory().setItem(slot, button.item().name(button.item().build().displayName()).build());
                        player.updateInventory();
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
        for (ItemBuilder item : this.items.values()) {
            if (pageSlotHolderByItem(item).page() == this.currentPage) {
                this.inventory.setItem(pageSlotHolderByItem(item).slot(), item.build());
            }
        }
        if (this.currentPage > 1) {
            for (Map.Entry<Integer, ItemBuilder> entry : this.previousButton.entrySet()) {
                this.inventory.setItem(entry.getKey().intValue(), this.previousButton.get(entry.getKey()).build());
            }
        }
        if (this.currentPage < this.totalPages) {
            for (Map.Entry<Integer, ItemBuilder> entry2 : this.nextButton.entrySet()) {
                this.inventory.setItem(entry2.getKey().intValue(), this.nextButton.get(entry2.getKey()).build());
            }
        }
        if (!this.borderMap.isEmpty()) {
            for (Map.Entry<Button, String[]> entry3 : this.borderMap.entrySet()) {
                border(entry3.getKey(), this.borderMap.get(entry3.getKey()));
            }
        }
        for (Map.Entry<Integer, Button> entry4 : this.stickyButtons.entrySet()) {
            this.inventory.setItem(entry4.getKey().intValue(), this.stickyButtons.get(entry4.getKey()).item().build());
        }
        if (this.totalPages > 1 && this.currentPageItemEnabled) {
            this.inventory.setItem(this.currentPageSlot, this.currentPageItem.rawName(this.currentPageItem.meta().getDisplayName().replaceAll("<current_page>", String.valueOf(this.currentPage)).replaceAll("<page>", String.valueOf(this.currentPage)).replaceAll("\\{current_page}", String.valueOf(this.currentPage)).replaceAll("\\{page}", String.valueOf(this.currentPage))).build());
        }
        player.updateInventory();
    }

    public void nextPage(@NotNull Player player) {
        open(player, currentPage() + 1);
    }

    public void previousPage(@NotNull Player player) {
        open(player, currentPage() - 1);
    }

    @Nullable
    public ItemBuilder item(@NotNull PaginatedSlot where) {
        return this.items.get(where);
    }

    @Nullable
    public Button button(@NotNull PaginatedSlot where) {
        return this.buttons.get(where);
    }

    @Nullable
    public Button stickyButton(int where) {
        return this.stickyButtons.get(Integer.valueOf(where));
    }

    public PaginatedSlot pageSlotHolderByButton(@NotNull Button button) {
        for (Map.Entry<PaginatedSlot, Button> entry : this.buttons.entrySet()) {
            if (entry.getValue() == button) {
                return entry.getKey();
            }
        }
        return null;
    }

    public PaginatedSlot pageSlotHolderByItem(@NotNull ItemBuilder item) {
        for (Map.Entry<PaginatedSlot, ItemBuilder> entry : this.items.entrySet()) {
            if (entry.getValue() == item) {
                return entry.getKey();
            }
        }
        return null;
    }

    public int slotByStickyButton(@NotNull Button button) {
        for (Map.Entry<Integer, Button> entry : this.stickyButtons.entrySet()) {
            if (entry.getValue() == button) {
                return entry.getKey().intValue();
            }
        }
        return -1;
    }

    public int slotByNextPageButton(@NotNull ItemBuilder item) {
        for (Map.Entry<Integer, ItemBuilder> entry : this.nextButton.entrySet()) {
            if (entry.getValue() == item) {
                return entry.getKey().intValue();
            }
        }
        return -1;
    }

    public int slotByPreviousPageButton(@NotNull ItemBuilder item) {
        for (Map.Entry<Integer, ItemBuilder> entry : this.previousButton.entrySet()) {
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
    public PaginatedMenu type() {
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dev.darkxx.utils.menu.MenuBase
    @NotNull
    public PaginatedMenu onClose(@Nullable CloseListener onClose) {
        this.onClose = onClose;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dev.darkxx.utils.menu.MenuBase
    @NotNull
    public PaginatedMenu onDrag(@Nullable DragListener onDrag) {
        this.onDrag = onDrag;
        return this;
    }

    public CloseListener onClose() {
        return this.onClose;
    }

    public DragListener onDrag() {
        return this.onDrag;
    }

    public boolean currentPageItemEnabled() {
        return this.currentPageItemEnabled;
    }

    public ItemBuilder currentPageItem() {
        return this.currentPageItem;
    }

    public int currentPageSlot() {
        return this.currentPageSlot;
    }
}
