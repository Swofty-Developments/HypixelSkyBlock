package net.swofty.type.generic.gui.v2;

import net.minestom.server.item.ItemStack;
import net.swofty.type.generic.gui.v2.context.ClickContext;
import net.swofty.type.generic.gui.v2.context.ViewContext;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public record ViewComponent<S>(
    int slot,
    BiFunction<S, ViewContext, ItemStack.Builder> render,
    BiConsumer<ClickContext<S>, ViewContext> onClick
) {
    public static <S> ViewComponent<S> staticItem(int slot, ItemStack.Builder item) {
        return new ViewComponent<>(slot, (_, _) -> item, (_, _) -> {});
    }

    public static <S> ViewComponent<S> clickable(
        int slot,
        BiFunction<S, ViewContext, ItemStack.Builder> render,
        BiConsumer<ClickContext<S>, ViewContext> onClick
    ) {
        return new ViewComponent<>(slot, render, onClick);
    }
}
