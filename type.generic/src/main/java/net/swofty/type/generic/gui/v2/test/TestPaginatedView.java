package net.swofty.type.generic.gui.v2.test;

import net.kyori.adventure.text.Component;
import net.minestom.server.component.DataComponents;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.v2.*;
import net.swofty.type.generic.gui.v2.context.ClickContext;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.List;
import java.util.stream.IntStream;

public final class TestPaginatedView extends PaginatedView<Integer, TestPaginatedView.State> {

    private static final int[] PAGINATED_SLOTS = {
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34,
            37, 38, 39, 40, 41, 42, 43
    };

    @Override
    public ViewConfiguration<State> configuration() {
        return ViewConfiguration.withString(
                (state, _) -> "Test Paginated View - Page " + (state.page() + 1),
                InventoryType.CHEST_6_ROW
        );
    }

    @Override
    protected int[] getPaginatedSlots() {
        return PAGINATED_SLOTS;
    }

    @Override
    protected ItemStack.Builder renderItem(Integer item, int index, HypixelPlayer player) {
        return ItemStack.builder(Material.PAPER)
                .set(DataComponents.CUSTOM_NAME, Component.text("§eItem #" + item))
                .set(DataComponents.LORE, List.of(
                        Component.text("§7Index: §a" + index),
                        Component.text(""),
                        Component.text("§eClick to select!")
                ));
    }

    @Override
    protected void onItemClick(ClickContext<State> click, ViewContext ctx, Integer item, int index) {
        ctx.player().sendMessage(Component.text("§aYou clicked item #" + item + " at index " + index));
    }

    @Override
    protected boolean shouldFilterFromSearch(State state, Integer item) {
        return !String.valueOf(item).contains(state.query);
    }

    @Override
    protected void layoutCustom(ViewLayout<State> layout, State state, ViewContext ctx) {
        Components.close(layout, 49);
        if (!Components.back(layout, 48, ctx)) {
            layout.slot(48, FILLER);
        }
    }

    @Override
    protected int getPreviousPageSlot() {
        return 45;
    }

    @Override
    protected int getNextPageSlot() {
        return 53;
    }

    public record State(List<Integer> items, int page, String query) implements PaginatedState<Integer> {
        public State(List<Integer> items) {
            this(items, 0, "");
        }

        @Override
        public PaginatedState<Integer> withPage(int page) {
            return new State(items, page, query);
        }

        @Override
        public PaginatedState<Integer> withItems(List<Integer> items) {
            return new State(items, page, query);
        }
    }

}

