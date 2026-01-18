package net.swofty.type.generic.gui.v2.test;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.*;
import net.swofty.type.generic.gui.v2.context.ClickContext;
import net.swofty.type.generic.gui.v2.context.ViewContext;

import java.util.HashMap;
import java.util.Map;

public final class TestContainerView implements StatefulView<TestContainerView.State> {

    private static final int[] STORAGE_SLOTS = {10, 11, 12, 13, 14, 15, 16};

    public record State(Map<Integer, ItemStack> items) {
        public State withItem(int slot, ItemStack item) {
            Map<Integer, ItemStack> newItems = new HashMap<>(items);
            if (item.isAir()) {
                newItems.remove(slot);
            } else {
                newItems.put(slot, item);
            }
            return new State(newItems);
        }

        public int itemCount() {
            return (int) items.values().stream().filter(i -> !i.isAir()).count();
        }
    }

    @Override
    public State initialState() {
        return new State(new HashMap<>());
    }

    @Override
    public ViewConfiguration<State> configuration() {
        return new ViewConfiguration<>("§8Personal Storage", InventoryType.CHEST_3_ROW);
    }

    @Override
    public void layout(ViewLayout<State> layout, State state, ViewContext ctx) {
        layout.filler(Layouts.border(0, 26), Components.FILLER);

        for (int slot : STORAGE_SLOTS) {
            layout.editable(slot, (s, c) -> s.items().getOrDefault(slot, ItemStack.AIR).builder(),
                (changedSlot, oldItem, newItem, s) ->
                    ctx.session(State.class).updateQuiet(current -> current.withItem(changedSlot, newItem))
            );
        }

        layout.slot(4, (s, c) -> ItemStackCreator.getStack(
            "§eStorage Info",
            Material.CHEST,
            1,
            "§7Items stored: §f" + s.itemCount(),
            "",
            "§7Place or remove items below!"
        ));

        Components.close(layout, 22);
        layout.allowHotkey(true);
    }

    @Override
    public boolean onBottomClick(ClickContext<State> click, ViewContext ctx) {
        return true;
    }
}
