package net.swofty.type.bedwarsgame.gui;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.DefaultState;
import net.swofty.type.generic.gui.v2.StatelessView;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ViewContext;

public class GUITrackerAndCommunication extends StatelessView {

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("Tracker & Communication", InventoryType.CHEST_3_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        layout.slot(11, ItemStackCreator.getStack(
            "§aQuick Communications",
            Material.EMERALD,
            1,
            "§7Send highlighted chat messages to",
            "§7your teammates!",
            "",
            "§eClick to open!"
        ));
        layout.slot(15, ItemStackCreator.getStack(
            "§aTracker Shop",
            Material.COMPASS,
            1,
            "§7Purchase tracking upgrade for your",
            "§7compass which will track each player",
            "§7on a specific team until you die.",
            "",
            "§eClick to open!"
        ));
    }
}
