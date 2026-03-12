package net.swofty.type.garden.gui;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.Components;
import net.swofty.type.generic.gui.v2.DefaultState;
import net.swofty.type.generic.gui.v2.StatelessView;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public class GUIAnita extends StatelessView {
    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("Anita", InventoryType.CHEST_4_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.fill(layout);
        Components.backOrClose(layout, 31, ctx);

        SkyBlockPlayer player = (SkyBlockPlayer) ctx.player();

        layout.slot(11, ItemStackCreator.getStack(
            "§aContest Overview",
            Material.WHEAT,
            1,
            "§7Jacob's Tickets: §a" + GardenGuiSupport.personal(player).getJacobsTickets(),
            "§6Gold Medals: §6" + GardenGuiSupport.getMedalCount(player, "gold"),
            "§fSilver Medals: §f" + GardenGuiSupport.getMedalCount(player, "silver"),
            "§cBronze Medals: §c" + GardenGuiSupport.getMedalCount(player, "bronze"),
            "",
            "§eClick to view!"
        ), (click, c) -> c.push(new GUIJacobSFarmingContests()));

        layout.slot(13, ItemStackCreator.getStack(
            "§aPersonal Bests",
            Material.PAPER,
            1,
            "§7Tracked crop personal bests:",
            "§e" + GardenGuiSupport.personal(player).getContestPersonalBests().size(),
            "",
            "§7Anita purchases tracked:",
            "§e" + GardenGuiSupport.personal(player).getAnitaPurchases().size()
        ));

        layout.slot(15, ItemStackCreator.getStackHead(
            "§aGarden Chips",
            "560aa469cc6b667dbcbfdc63e827b7c05ca7726af8a178a4aa2e8ffa2690e843",
            1,
            "§7Manage redeemed Chips and spend",
            "§2Sowdust §7on upgrades.",
            "",
            "§eClick to view!"
        ), (click, c) -> c.push(new GUIManageChips()));
    }
}
