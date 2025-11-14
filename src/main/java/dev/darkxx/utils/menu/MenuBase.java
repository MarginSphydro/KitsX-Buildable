package dev.darkxx.utils.menu;

import dev.darkxx.utils.item.ItemBuilder;
import dev.darkxx.utils.menu.listener.CloseListener;
import dev.darkxx.utils.menu.listener.DragListener;
import dev.darkxx.utils.menu.paginated.PaginatedSlot;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/menu/MenuBase.class */
public interface MenuBase<T> {
    @NotNull
    T type();

    @NotNull
    T onClose(@Nullable CloseListener closeListener);

    @NotNull
    T onDrag(@Nullable DragListener dragListener);

    int size();

    Component title();

    void open(Player player);

    @NotNull
    T border(Button button, String... strArr);

    @NotNull
    T fill(Object obj, String... strArr);

    Inventory inventory();

    @NotNull
    default T button(int slot, Button button) {
        return type();
    }

    @NotNull
    default T item(int slot, ItemBuilder item) {
        return type();
    }

    @NotNull
    default T button(PaginatedSlot where, Button button) {
        return type();
    }

    @NotNull
    default T item(PaginatedSlot where, ItemBuilder item) {
        return type();
    }

    default void open(Player player, int page) {
        open(player);
    }
}
