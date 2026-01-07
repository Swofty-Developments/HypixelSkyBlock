package net.swofty.type.generic.gui.v2;

import net.minestom.server.item.ItemStack;
import net.swofty.type.generic.gui.v2.context.ClickContext;
import net.swofty.type.generic.gui.v2.context.GuiContext;

import java.util.List;
import java.util.function.BiFunction;

public final class Pagination {

    public record Page<T>(List<T> items, int current, int total) {
        public boolean hasNext() { return current < total - 1; }
        public boolean hasPrev() { return current > 0; }
    }

    public static <T> Page<T> paginate(List<T> all, int page, int perPage) {
        int totalPages = Math.max(1, (int) Math.ceil((double) all.size() / perPage));
        int start = page * perPage;
        int end = Math.min(start + perPage, all.size());
        List<T> items = start < all.size() ? all.subList(start, end) : List.of();
        return new Page<>(items, page, totalPages);
    }

    public static <S, T> void items(
        GuiLayout<S> layout,
        List<Integer> slots,
        List<T> items,
        BiFunction<T, Integer, ItemStack> renderer,
        ClickHandler<S, T> onClick
    ) {
        for (int i = 0; i < slots.size(); i++) {
            int slot = slots.get(i);
            if (i < items.size()) {
                T item = items.get(i);
                int index = i;
                layout.slot(slot, (_, _) -> renderer.apply(item, index),
                    (click, ctx) -> onClick.handle(click, ctx, item, index));
            } else {
                layout.slot(slot, (_, _) -> ItemStack.AIR);
            }
        }
    }

    @FunctionalInterface
    public interface ClickHandler<S, T> {
        void handle(ClickContext<S> click, GuiContext ctx, T item, int index);
    }
}

