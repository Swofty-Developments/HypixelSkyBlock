package net.swofty.type.generic.gui.v2.test;

import net.kyori.adventure.text.Component;
import net.minestom.server.component.DataComponents;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.*;
import net.swofty.type.generic.gui.v2.context.GuiContext;

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
    public InventoryType size() {
        return InventoryType.CHEST_3_ROW;
    }

    @Override
    public Component title(State state, GuiContext ctx) {
        return Component.text("Counter: " + state.counter());
    }

    @Override
    public void layout(GuiLayout<State> layout, State state, GuiContext ctx) {
        layout.filler(Layouts.border(0, 26), Components.FILLER);

        layout.slot(11, (_, _) -> ItemStackCreator.createNamedItemStack(Material.RED_WOOL, "§c-1").build(),
            (click, c) -> c.session(State.class).update(State::decrement));

        layout.slot(13, (s, _) -> ItemStackCreator.createNamedItemStack(Material.PAPER, "§eCounter: " + s.counter()).build());

        layout.slot(15, (_, _) -> ItemStackCreator.createNamedItemStack(Material.GREEN_WOOL, "§a+1").build(),
            (click, c) -> c.session(State.class).update(State::increment));

        Components.close(layout, 22);
    }

}

