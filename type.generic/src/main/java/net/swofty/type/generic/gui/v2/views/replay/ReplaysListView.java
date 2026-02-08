package net.swofty.type.generic.gui.v2.views.replay;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.component.DataComponents;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.v2.StatefulPaginatedView;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ClickContext;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.generic.user.HypixelPlayer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

public class ReplaysListView extends StatefulPaginatedView<ReplayEntry, ReplaysListView.State> {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMM dd, yyyy HH:mm");

    private final Consumer<ReplayEntry> onReplaySelect;

    public ReplaysListView(Consumer<ReplayEntry> onReplaySelect) {
        this.onReplaySelect = onReplaySelect;
    }

    @Override
    public ViewConfiguration<State> configuration() {
        return ViewConfiguration.withString((_, _) -> "Recent Games", InventoryType.CHEST_6_ROW);
    }

    @Override
    public State initialState() {
        return new State(
                Collections.emptyList(),
                0
        );
    }

    @Override
    protected int[] getPaginatedSlots() {
        return new int[]{
                10, 11, 12, 13, 14, 15, 16,
                19, 20, 21, 22, 23, 24, 25,
                28, 29, 30, 31, 32, 33, 34,
                37, 38, 39, 40, 41, 42, 43
        };
    }

    @Override
    protected int getNextPageSlot() {
        return 50;
    }

    @Override
    protected int getPreviousPageSlot() {
        return 48;
    }

    @Override
    protected void layoutBackground(ViewLayout<State> layout, State state, ViewContext ctx) {
        layout.slot(49, (s, c) -> ItemStack.builder(Material.BARRIER)
                        .set(DataComponents.CUSTOM_NAME, Component.text("Close", NamedTextColor.RED)),
                (_, viewCtx) -> viewCtx.player().closeInventory());

    }

    @Override
    protected ItemStack.Builder renderItem(ReplayEntry item, int index, HypixelPlayer player) {
        Material material = switch (item.serverType()) {
            case BEDWARS_GAME -> Material.RED_BED;
            case SKYWARS_GAME -> Material.ENDER_EYE;
            case MURDER_MYSTERY_GAME -> Material.IRON_SWORD;
            default -> Material.PAPER;
        };

        List<Component> lore = new ArrayList<>();
        lore.add(Component.empty());
        lore.add(Component.text("Map: ", NamedTextColor.GRAY)
                .append(Component.text(item.mapName(), NamedTextColor.WHITE)));
        lore.add(Component.text("Duration: ", NamedTextColor.GRAY)
                .append(Component.text(item.formattedDuration(), NamedTextColor.WHITE)));
        lore.add(Component.text("Date: ", NamedTextColor.GRAY)
                .append(Component.text(DATE_FORMAT.format(new Date(item.startTime())), NamedTextColor.WHITE)));
        lore.add(Component.text("Players: ", NamedTextColor.GRAY)
                .append(Component.text(item.players().size(), NamedTextColor.WHITE)));
        lore.add(Component.empty());

        // Show result
        if (item.winnerId() != null) {
            boolean won = item.players().containsKey(player.getUuid()) &&
                    (item.winnerId().equals(player.getUuid().toString()) ||
						item.players().keySet().stream()
								.anyMatch(uuid -> item.winnerId().contains(uuid.toString())));
            lore.add(won ? Component.text("VICTORY!", NamedTextColor.GREEN, TextDecoration.BOLD) :
                    Component.text("DEFEAT", NamedTextColor.RED));
        }

        lore.add(Component.empty());
        lore.add(Component.text("§eClick to view replay!"));

        return ItemStack.builder(material)
                .set(DataComponents.CUSTOM_NAME, Component.text(item.displayName(), NamedTextColor.GREEN))
                .set(DataComponents.LORE, lore)
                //.set(DataComponents.TOOLTIP_DISPLAY, new TooltipDisplay(false, Set.of()))
                ;
    }

    @Override
    protected void onItemClick(ClickContext<State> click, ViewContext ctx, ReplayEntry item, int index) {
        ctx.player().closeInventory();
        onReplaySelect.accept(item);
    }

    @Override
    protected boolean shouldFilterFromSearch(State state, ReplayEntry item) {
        return false;
    }

    public record State(
            List<ReplayEntry> items,
            int page
    ) implements PaginatedState<ReplayEntry> {
        @Override
        public State withPage(int page) {
            return new State(items, page);
        }

        @Override
        public State withItems(List<ReplayEntry> items) {
            return new State(items, page);
        }
    }
}
