package net.swofty.type.generic.gui.v2;

import net.minestom.server.item.ItemStack;
import net.swofty.type.generic.gui.v2.context.ClickContext;
import net.swofty.type.generic.gui.v2.context.GuiContext;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public final class GuiLayout<S> {

    private final Map<Integer, GuiComponent<S>> components = new HashMap<>();

    public void slot(
        int slot,
        BiFunction<S, GuiContext, ItemStack> render,
        BiConsumer<ClickContext<S>, GuiContext> onClick
    ) {
        components.put(slot, new GuiComponent<>(slot, render, onClick));
    }

    public void slot(int slot, BiFunction<S, GuiContext, ItemStack> render) {
        slot(slot, render, (_, _) -> {});
    }

    public void filler(Collection<Integer> slots, ItemStack item) {
        slots.forEach(s ->
            components.put(s, GuiComponent.staticItem(s, item))
        );
    }

    public void filler(Collection<Integer> slots, BiFunction<S, GuiContext, ItemStack> itemSupplier) {
        slots.forEach(s ->
            components.put(s, new GuiComponent<>(s, itemSupplier, (_, _) -> {}))
        );
    }

    public Map<Integer, GuiComponent<S>> components() {
        return components;
    }
}
