package net.swofty.type.generic.gui.v2.test;

import net.kyori.adventure.text.Component;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.*;
import net.swofty.type.generic.gui.v2.context.ViewContext;

public final class TestStateView implements StatefulView<TestStateView.State> {

    @Override
    public State initialState() {
        return new State(0);
    }

    public record State(int counter) {
        public State increment() { return new State(counter + 1); }
        public State decrement() { return new State(counter - 1); }
    }

    @Override
    public ViewConfiguration<State> configuration() {
        return ViewConfiguration.withString((view, ctx) -> "Counter: " + view.counter(), InventoryType.CHEST_3_ROW);
    }

    @Override
    public void layout(ViewLayout<State> layout, State state, ViewContext ctx) {
        layout.filler(Layouts.border(0, 26), Components.FILLER);
        layout.slot(11, ItemStackCreator.createNamedItemStack(Material.RED_WOOL, "§c-1"), State::decrement);
        layout.slot(13, (s, _) -> ItemStackCreator.createNamedItemStack(Material.PAPER, "§eCounter: " + s.counter()));
        layout.slot(15, ItemStackCreator.createNamedItemStack(Material.GREEN_WOOL, "§a+1"), State::increment);
        Components.close(layout, 22);
    }

}
