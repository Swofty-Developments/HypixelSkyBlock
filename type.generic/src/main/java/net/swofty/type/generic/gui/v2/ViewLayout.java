package net.swofty.type.generic.gui.v2;

import lombok.Getter;
import lombok.experimental.Accessors;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.swofty.type.generic.gui.v2.context.ClickContext;
import net.swofty.type.generic.gui.v2.context.ViewContext;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.IntStream;

public final class ViewLayout<S> {

    @Getter
    @Accessors(fluent = true)
    private final Map<Integer, ViewComponent<S>> components = new HashMap<>();

    @Getter
    @Accessors(fluent = true)
    private final InventoryType inventoryType;

    public ViewLayout(InventoryType inventoryType) {
        this.inventoryType = inventoryType;
    }

    public void slot(
        int slot,
        BiFunction<S, ViewContext, ItemStack.Builder> render,
        BiConsumer<ClickContext<S>, ViewContext> onClick
    ) {
        components.put(slot, new ViewComponent<>(slot, render, onClick));
    }

    public void slot(int slot, BiFunction<S, ViewContext, ItemStack.Builder> render) {
        slot(slot, render, (_, _) -> {});
    }

    public void slot(int slot, ItemStack.Builder builder, BiConsumer<ClickContext<S>, ViewContext> onClick) {
        components.put(slot, new ViewComponent<>(slot, (_, _) -> builder, onClick));
    }

    public void slot(int slot, ItemStack.Builder builder, Function<S, S> stateUpdater) {
        components.put(slot, new ViewComponent<>(slot, (_, _) -> builder,
            (_, ctx) -> ctx.session(Object.class).updateUnchecked(stateUpdater)));
    }

    public void slot(int slot, ItemStack.Builder builder) {
        components.put(slot, ViewComponent.staticItem(slot, builder));
    }

    public void filler(Collection<Integer> slots, ItemStack.Builder builder) {
        slots.forEach(s ->
            components.put(s, ViewComponent.staticItem(s, builder))
        );
    }

    public void filler(Collection<Integer> slots, BiFunction<S, ViewContext, ItemStack.Builder> itemSupplier) {
        slots.forEach(s ->
            components.put(s, new ViewComponent<>(s, itemSupplier, (_, _) -> {}))
        );
    }

    public void filler(ItemStack.Builder builder) {
        IntStream.range(0, inventoryType.getSize()).forEach(s ->
            components.put(s, ViewComponent.staticItem(s, builder))
        );
    }

}
