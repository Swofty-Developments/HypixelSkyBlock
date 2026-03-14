package net.swofty.type.generic.gui.v2.test;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.*;
import net.swofty.type.generic.gui.v2.context.ClickContext;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.generic.user.HypixelPlayer;

public final class TestSharedContainerView implements View<TestSharedContainerView.State> {

    public record State(String teamName) {
    }

    @Override
    public ViewConfiguration<State> configuration() {

        return ViewConfiguration.withString((state, ctx) -> {
            SharedContext<State> shared = ctx.session(State.class).sharedContext();
            int viewers = shared != null ? shared.sessionCount() : 1;
            return "§6" + state.teamName() + " §7(§e" + viewers + " viewing§7)";
        }, InventoryType.CHEST_6_ROW);
    }

    @Override
    public void layout(ViewLayout<State> layout, State state, ViewContext ctx) {
        layout.filler(Components.FILLER);

        layout.slot(4, (s, c) -> {
            SharedContext<State> shared = ctx.session(State.class).sharedContext();
            int itemCount = shared != null
                    ? (int) shared.getAllSlotItems().values().stream().filter(i -> !i.isAir()).count()
                    : 0;

            return ItemStackCreator.getStack(
                    "§6" + s.teamName(),
                    Material.CHEST,
                    1,
                    "§7Shared team storage",
                    "",
                    "§7Items stored: §e" + itemCount,
                    "§7Players viewing: §e" + (shared != null ? shared.sessionCount() : 1)
            );
        });

        Components.sharedContainerGrid(layout, 10, 43);

        layout.slot(49, (s, c) -> ItemStackCreator.getStack(
                "§cClear All",
                Material.TNT,
                1,
                "§7Clears all items from storage",
                "",
                "§eShift Left Click to confirm!"
        ), (click, context) -> {
            if (click.click() instanceof Click.LeftShift) {
                SharedContext<State> shared = context.session(State.class).sharedContext();
                if (shared != null) {
                    shared.clearSlotItems();
                    shared.broadcastMessage("§c" + click.player().getUsername() + " cleared the team chest!");
                }
            }
        });

        Components.close(layout, 53);
        layout.allowHotkey(true);
    }

    public static ViewSession<State> open(HypixelPlayer player, String teamId, String teamName) {
        String contextId = "team-chest-" + teamId;
        ViewNavigator navigator = ViewNavigator.get(player);

        if (SharedContext.exists(contextId)) {
            return navigator.joinShared(new TestSharedContainerView(), contextId);
        }

        SharedContext<State> ctx = SharedContext.create(contextId, new State(teamName));
        ctx.onSlotChange(change ->
                System.out.println("Slot " + change.slot() + " changed: " + change.oldItem().material() + " -> " + change.newItem().material())
        );

        return navigator.pushShared(new TestSharedContainerView(), ctx);
    }

    @Override
    public boolean onBottomClick(ClickContext<State> click, ViewContext ctx) {
        return true;
    }
}
