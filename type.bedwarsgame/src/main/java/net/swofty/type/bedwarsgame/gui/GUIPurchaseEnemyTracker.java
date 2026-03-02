package net.swofty.type.bedwarsgame.gui;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.Components;
import net.swofty.type.generic.gui.v2.DefaultState;
import net.swofty.type.generic.gui.v2.StatelessView;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ViewContext;

public class GUIPurchaseEnemyTracker extends StatelessView {

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("Purchase Enemy Tracker", InventoryType.CHEST_4_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.back(layout, 31, ctx);
        layout.slot(10, ItemStackCreator.getStack(
            "§cTrack Team Blue",
            Material.BLUE_WOOL,
            1,
            "§7Purchase tracking upgrade for your",
            "§7compass which will track each player",
            "§7on a specific team until you die.",
            "",
            "§7Cost: §22 Emeralds",
            "",
            "§cUnlocks when all enemy beds are destroyed!"
        ));
    }
}
