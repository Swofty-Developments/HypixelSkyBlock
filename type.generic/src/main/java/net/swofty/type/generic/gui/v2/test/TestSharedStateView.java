package net.swofty.type.generic.gui.v2.test;

import net.kyori.adventure.text.Component;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.*;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.generic.user.HypixelPlayer;

public final class TestSharedStateView implements StatefulView<TestSharedStateView.State> {

    public record State(int counter, String lastModifiedBy) {
        public State increment(String playerName) {
            return new State(counter + 1, playerName);
        }

        public State decrement(String playerName) {
            return new State(counter - 1, playerName);
        }

        public State reset(String playerName) {
            return new State(0, playerName);
        }
    }

    @Override
    public State initialState() {
        return new State(0, "None");
    }

    @Override
    public ViewConfiguration<State> configuration() {
        return ViewConfiguration.withString((state, ctx) -> "§9Shared Counter: §e" + state.counter(), InventoryType.CHEST_3_ROW);
    }

    @Override
    public void layout(ViewLayout<State> layout, State state, ViewContext ctx) {
        layout.filler(Components.FILLER);

        layout.slot(11, (s, c) -> ItemStackCreator.getStack(
                        "§c-1",
                        Material.RED_WOOL,
                        1,
                        "§7Click to decrement",
                        "",
                        "§7Current: §f" + s.counter()
                ), (click, context) ->
                        context.session(State.class).update(s -> s.decrement(click.player().getUsername()))
        );

        layout.slot(13, (s, c) -> ItemStackCreator.getStack(
                "§eCounter: " + s.counter(),
                Material.PAPER,
                Math.max(1, Math.min(64, Math.abs(s.counter()))),
                "§7Last modified by: §f" + s.lastModifiedBy(),
                "",
                "§7This view is shared!",
                "§7All players see the same counter."
        ));

        layout.slot(15, (s, c) -> ItemStackCreator.getStack(
                        "§a+1",
                        Material.GREEN_WOOL,
                        1,
                        "§7Click to increment",
                        "",
                        "§7Current: §f" + s.counter()
                ), (click, context) ->
                        context.session(State.class).update(s -> s.increment(click.player().getUsername()))
        );

        layout.slot(22, (s, c) -> ItemStackCreator.getStack(
                        "§6Reset",
                        Material.SUNFLOWER,
                        1,
                        "§7Reset counter to 0"
                ), (click, context) ->
                        context.session(State.class).update(s -> s.reset(click.player().getUsername()))
        );

        Components.close(layout, 26);
    }

    public static void openNew(HypixelPlayer player, String contextId) {
        ViewNavigator.get(player).pushShared(new TestSharedStateView(), contextId, new State(0, player.getUsername()));
    }

    public static void join(HypixelPlayer player, String contextId) {
        ViewNavigator.get(player).joinShared(new TestSharedStateView(), contextId);
    }
}
