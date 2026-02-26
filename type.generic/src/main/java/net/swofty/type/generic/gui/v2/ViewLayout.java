package net.swofty.type.generic.gui.v2;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.swofty.type.generic.gui.v2.context.ClickContext;
import net.swofty.type.generic.gui.v2.context.ViewContext;

import java.util.*;
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

    @Getter
    @Setter
    @Accessors(fluent = true)
    private boolean allowHotkey = false;

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
        slot(slot, render, (_, _) -> {
        });
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

    public void slots(Collection<Integer> slots,
                      BiFunction<S, ViewContext, ItemStack.Builder> render,
                      BiConsumer<ClickContext<S>, ViewContext> onClick) {
        slots.forEach(slot -> slot(slot, render, onClick));
    }

    public void slots(Collection<Integer> slots,
                      BiFunction<S, ViewContext, ItemStack.Builder> render) {
        slots.forEach(slot -> slot(slot, render));
    }

    public void editable(int slot, BiFunction<S, ViewContext, ItemStack.Builder> initialRender,
                         SlotChangeHandler<S> onChange) {
        components.put(slot, ViewComponent.editable(slot, initialRender, onChange));
    }

    public void editable(int slot, ItemStack.Builder initialItem, SlotChangeHandler<S> onChange) {
        components.put(slot, ViewComponent.editable(slot, initialItem, onChange));
    }

    public void autoUpdating(int slot,
                             BiFunction<S, ViewContext, ItemStack.Builder> render,
                             java.time.Duration updateInterval) {
        components.put(slot, ViewComponent.autoUpdating(slot, render, updateInterval));
    }

    public void autoUpdating(int slot,
                             BiFunction<S, ViewContext, ItemStack.Builder> render,
                             BiConsumer<ClickContext<S>, ViewContext> onClick,
                             java.time.Duration updateInterval) {
        components.put(slot, ViewComponent.autoUpdating(slot, render, onClick, updateInterval));
    }

    public void editable(int slot, SlotChangeHandler<S> onChange) {
        components.put(slot, ViewComponent.editable(slot, onChange));
    }

    public void editable(int slot) {
        components.put(slot, ViewComponent.editable(slot, (s, o, n, st) -> {
        }));
    }

    public void editableSlots(Collection<Integer> slots, SlotChangeHandler<S> onChange) {
        slots.forEach(slot -> editable(slot, onChange));
    }

    public void editableSlots(Collection<Integer> slots) {
        slots.forEach(this::editable);
    }

    public void editableGrid(int startSlot, int endSlot, SlotChangeHandler<S> onChange) {
        editableSlots(Layouts.rectangle(startSlot, endSlot), onChange);
    }

    public void filler(Collection<Integer> slots, ItemStack.Builder builder) {
        slots.forEach(s ->
                components.put(s, ViewComponent.staticItem(s, builder))
        );
    }

    public void filler(Collection<Integer> slots, BiFunction<S, ViewContext, ItemStack.Builder> itemSupplier) {
        slots.forEach(s ->
                components.put(s, new ViewComponent<>(s, itemSupplier, (_, _) -> {
                }))
        );
    }

    public void filler(ItemStack.Builder builder) {
        IntStream.range(0, inventoryType.getSize()).forEach(s ->
                components.put(s, ViewComponent.staticItem(s, builder))
        );
    }

    public SlotBehavior getBehavior(int slot) {
        ViewComponent<S> component = components.get(slot);
        return component != null ? component.behavior() : SlotBehavior.NO_RENDER;
    }

    public boolean isEditable(int slot) {
        return getBehavior(slot) == SlotBehavior.EDITABLE;
    }

    public Set<Integer> editableSlots() {
        Set<Integer> editable = new HashSet<>();
        components.forEach((slot, component) -> {
            if (component.behavior() == SlotBehavior.EDITABLE) {
                editable.add(slot);
            }
        });
        return editable;
    }
}
