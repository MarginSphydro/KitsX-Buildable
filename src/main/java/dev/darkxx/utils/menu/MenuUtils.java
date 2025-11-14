package dev.darkxx.utils.menu;

import dev.darkxx.utils.item.ItemBuilder;
import dev.darkxx.utils.menu.paginated.PaginatedSlot;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/menu/MenuUtils.class */
public class MenuUtils {
    public static int getCenter(int size) {
        return (size / 2) + ((size / 9) % 2 > 0 ? 1 : 5);
    }

    public static int getCenterSlot(int size) {
        return getCenter(size) - 1;
    }

    public static int paginatedSlotToSlot(@NotNull PaginatedSlot slot) {
        return slot.slot();
    }

    @NotNull
    public static PaginatedSlot slotToPaginatedSlot(int slot) {
        return PaginatedSlot.paginatedSlot(slot, 1);
    }

    public static int[] paginatedSlotToArray(@NotNull PaginatedSlot slot) {
        return new int[]{slot.slot(), slot.page()};
    }

    @NotNull
    public static List<ItemBuilder> contents(@NotNull MenuBase<?> menu) {
        List<ItemBuilder> result = new ArrayList<>();
        for (ItemStack stack : menu.inventory().getContents()) {
            ItemBuilder item = ItemBuilder.item(stack);
            result.add(item);
        }
        return result;
    }

    public static void clear(@NotNull MenuBase<?> menu) {
        menu.inventory().clear();
    }
}
