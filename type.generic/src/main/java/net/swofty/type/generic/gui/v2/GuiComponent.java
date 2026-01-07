package net.swofty.type.generic.gui.v2;

import net.minestom.server.item.ItemStack;
import net.swofty.type.generic.gui.v2.context.GuiContext;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public record GuiComponent<S>(
    int slot,
    BiFunction<S, GuiContext, ItemStack> render,
    BiConsumer<ClickContext<S>, GuiContext> onClick
) {
    public static <S> GuiComponent<S> staticItem(int slot, ItemStack item) {
        return new GuiComponent<>(slot, (_, _) -> item, (_, _) -> {});
    }

    public static <S> GuiComponent<S> clickable(
        int slot,
        BiFunction<S, GuiContext, ItemStack> render,
        BiConsumer<ClickContext<S>, GuiContext> onClick
    ) {
        return new GuiComponent<>(slot, render, onClick);
    }
}
