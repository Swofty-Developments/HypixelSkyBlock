package net.swofty.type.bedwarslobby.gui.cosmetics;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.Components;
import net.swofty.type.generic.gui.v2.DefaultState;
import net.swofty.type.generic.gui.v2.StatelessView;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ViewContext;

public class GUITheSlumberHotel extends StatelessView {

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("The Slumber Hotel", InventoryType.CHEST_4_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.backOrClose(layout, 31, ctx);

        layout.slot(10, ItemStackCreator.getStack(
            "§3Hotel Guide",
            Material.SAND,
            1,
            "",
            "§7The §3Slumber Hotel §7exists in a unique",
            "§7dimension between the waking world",
            "§7and the dream world.",
            "",
            "§eClick to learn more!"
        ), (_, c) -> c.push(new GUIHotelGuide()));
        layout.slot(12, ItemStackCreator.getStack(
            "§aQuest Log",
            Material.WRITABLE_BOOK,
            1,
            "",
            "§7Keep track of what quests you've",
            "§7started, completed, or haven't even",
            "§7found yet!",
            "",
            "§eClick to open!"
        ));
        layout.slot(14, ItemStackCreator.getStack(
            "§aSlumber Inventory",
            Material.CHEST,
            1,
            "",
            "§7Collect §eSlumber Tickets §7and various",
            "§aSlumber Items §7by playing Bed Wars!",
            "",
            "§eClick to open!"
        ));
        layout.slot(16, ItemStackCreator.getStack(
            "§cThe Dreamfeast",
            Material.RED_STAINED_GLASS,
            1,
            "§8You'll need to talk to the Hotel Owner",
            "§8first..."
        ));
    }
}
