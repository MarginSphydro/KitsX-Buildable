package dev.darkxx.utils.menu.paginated;

import org.jetbrains.annotations.NotNull;

public record PaginatedSlot(int slot, int page) {
    @NotNull
    public static PaginatedSlot paginatedSlot(int slot, int page) {
        return new PaginatedSlot(slot, page);
    }
}
