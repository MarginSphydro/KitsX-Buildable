package dev.darkxx.utils.menu;

import dev.darkxx.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/menu/Button.class */
public class Button {
    private ButtonListener listener;
    private ItemBuilder item = new ItemBuilder(Material.AIR);
    private boolean isRefreshingButton = false;
    private long refreshDelay = 0;
    private long refreshPeriod = 20;
    private boolean isRefreshingAsync = true;

    /* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/menu/Button$ButtonListener.class */
    public interface ButtonListener {
        void onClick(@NotNull InventoryClickEvent inventoryClickEvent);
    }

    @NotNull
    public static Button button() {
        return new Button();
    }

    @NotNull
    public static Button button(@Nullable ItemBuilder item) {
        return new Button().item(item);
    }

    @NotNull
    public static Button button(@Nullable ItemBuilder item, @Nullable ButtonListener listener) {
        return new Button().item(item).onClick(listener);
    }

    @NotNull
    public Button refreshAsync(boolean refreshAsync) {
        this.isRefreshingAsync = refreshAsync;
        return this;
    }

    @NotNull
    public Button refresh(boolean refresh) {
        this.isRefreshingButton = refresh;
        return this;
    }

    @NotNull
    public Button refreshDelay(long refreshDelay) {
        this.refreshDelay = refreshDelay;
        return this;
    }

    @NotNull
    public Button refreshPeriod(long refreshPeriod) {
        this.refreshPeriod = refreshPeriod;
        return this;
    }

    @NotNull
    public Button refreshTime(long refreshDelay, long refreshPeriod) {
        this.refreshDelay = refreshDelay;
        this.refreshPeriod = refreshPeriod;
        return this;
    }

    @NotNull
    public Button onClick(@Nullable ButtonListener listener) {
        this.listener = listener;
        return this;
    }

    @NotNull
    public Button item(@Nullable ItemBuilder item) {
        this.item = item;
        return this;
    }

    public ItemBuilder item() {
        return this.item;
    }

    public ButtonListener listener() {
        return this.listener;
    }

    public boolean isRefreshingButton() {
        return this.isRefreshingButton;
    }

    public long refreshDelay() {
        return this.refreshDelay;
    }

    public long refreshPeriod() {
        return this.refreshPeriod;
    }

    public boolean isRefreshingAsync() {
        return this.isRefreshingAsync;
    }
}
