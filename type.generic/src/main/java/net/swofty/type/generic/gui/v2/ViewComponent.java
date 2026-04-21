package net.swofty.type.generic.gui.v2;

import net.minestom.server.item.ItemStack;
import net.swofty.type.generic.gui.v2.context.ClickContext;
import net.swofty.type.generic.gui.v2.context.ViewContext;

import java.time.Duration;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public record ViewComponent<S>(
        UUID id,
        int slot,
        BiFunction<S, ViewContext, ItemStack.Builder> render,
        BiConsumer<ClickContext<S>, ViewContext> onClick,
        SlotBehavior behavior,
        SlotChangeHandler<S> changeHandler,
        Duration updateInterval,
        Consumer<ViewContext> update
) {
    public ViewComponent(int slot, BiFunction<S, ViewContext, ItemStack.Builder> render,
                         BiConsumer<ClickContext<S>, ViewContext> onClick) {
        this(UUID.randomUUID(), slot, render, onClick, SlotBehavior.UI, null, null, null);
    }

    public static <S> ViewComponent<S> staticItem(int slot, ItemStack.Builder item) {
        return new ViewComponent<>(UUID.randomUUID(), slot, (_, _) -> item, (_, _) -> {
        }, SlotBehavior.UI, null, null, null);
    }

    public static <S> ViewComponent<S> autoUpdating(
            int slot,
            BiFunction<S, ViewContext, ItemStack.Builder> render,
            Duration updateInterval
    ) {
        return new ViewComponent<>(UUID.randomUUID(), slot, render, (_, _) -> {
        }, SlotBehavior.UI, null, updateInterval, null);
    }

    public static <S> ViewComponent<S> autoUpdating(
            int slot,
            BiFunction<S, ViewContext, ItemStack.Builder> render,
            BiConsumer<ClickContext<S>, ViewContext> onClick,
            Duration updateInterval
    ) {
        return new ViewComponent<>(UUID.randomUUID(), slot, render, onClick, SlotBehavior.UI, null, updateInterval, null);
    }

    public static <S> ViewComponent<S> autoUpdating(
        int slot,
        BiFunction<S, ViewContext, ItemStack.Builder> render,
        BiConsumer<ClickContext<S>, ViewContext> onClick,
        Duration updateInterval,
        Consumer<ViewContext> update
    ) {
        return new ViewComponent<>(UUID.randomUUID(), slot, render, onClick, SlotBehavior.UI, null, updateInterval, update);
    }

    public static <S> ViewComponent<S> clickable(
            int slot,
            BiFunction<S, ViewContext, ItemStack.Builder> render,
            BiConsumer<ClickContext<S>, ViewContext> onClick
    ) {
        return new ViewComponent<>(UUID.randomUUID(), slot, render, onClick, SlotBehavior.UI, null, null, null);
    }

    public static <S> ViewComponent<S> editable(
            int slot,
            BiFunction<S, ViewContext, ItemStack.Builder> initialRender,
            SlotChangeHandler<S> changeHandler
    ) {
        return new ViewComponent<>(UUID.randomUUID(), slot, initialRender, (_, _) -> {
        }, SlotBehavior.EDITABLE, changeHandler, null, null);
    }

    public static <S> ViewComponent<S> editable(int slot, ItemStack.Builder initialItem, SlotChangeHandler<S> changeHandler) {
        return new ViewComponent<>(UUID.randomUUID(), slot, (_, _) -> initialItem, (_, _) -> {
        }, SlotBehavior.EDITABLE, changeHandler, null, null);
    }

    public static <S> ViewComponent<S> editable(int slot, SlotChangeHandler<S> changeHandler) {
        return editable(slot, ItemStack.AIR.builder(), changeHandler);
    }
}
