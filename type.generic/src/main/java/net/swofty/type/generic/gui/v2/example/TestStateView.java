package net.swofty.type.generic.gui.v2.example;

import net.kyori.adventure.text.Component;
import net.minestom.server.component.DataComponents;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.v2.Components;
import net.swofty.type.generic.gui.v2.GuiLayout;
import net.swofty.type.generic.gui.v2.GuiSession;
import net.swofty.type.generic.gui.v2.Layouts;
import net.swofty.type.generic.gui.v2.View;
import net.swofty.type.generic.gui.v2.context.GuiContext;
import net.swofty.type.generic.user.HypixelPlayer;

public final class TestStateView implements View<TestStateView.State> {

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

        layout.slot(11, (_, _) -> item(Material.RED_WOOL, "§c-1"),
            (click, c) -> c.session(State.class).update(State::decrement));

        layout.slot(13, (s, _) -> item(Material.PAPER, "§eCounter: " + s.counter()));

        layout.slot(15, (_, _) -> item(Material.LIME_WOOL, "§a+1"),
            (click, c) -> c.session(State.class).update(State::increment));

        Components.close(layout, 22);
    }

    private static ItemStack item(Material mat, String name) {
        return ItemStack.builder(mat).set(DataComponents.CUSTOM_NAME, Component.text(name)).build();
    }

    public static void open(HypixelPlayer player) {
        GuiSession.open(new TestStateView(), player, new State(0));
    }
}

