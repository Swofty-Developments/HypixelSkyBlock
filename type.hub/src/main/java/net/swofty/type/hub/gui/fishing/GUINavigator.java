package net.swofty.type.hub.gui.fishing;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.Components;
import net.swofty.type.generic.gui.v2.DefaultState;
import net.swofty.type.generic.gui.v2.Layouts;
import net.swofty.type.generic.gui.v2.StatelessView;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ViewContext;

public class GUINavigator extends StatelessView {

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("Navigator", InventoryType.CHEST_6_ROW);
    }

    // todo: make this work with the mission
    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        layout.slots(Layouts.row(0), (_, _) -> Components.FILLER);
        layout.slots(Layouts.row(5), (_, _) -> Components.FILLER);
        layout.slots(Layouts.rectangle(9, 45), (_, _) -> Components.FILLER.material(Material.BLUE_STAINED_GLASS_PANE));
        Components.close(layout, 49);

        layout.slot(10, ItemStackCreator.getStackHead(
            "§2Backwater Bayou",
            "1c0cd33590f64d346d98cdd01606938742e715dda6737353306a44f81c8ba426",
            1,
            "§7A small, marshy outlet in the middle",
            "§7of nowhere. Due to its isolated",
            "§7nature, people frequently come here",
            "§7to dump their trash.",
            "",
            "§7Activities:",
            "§8 ■ §7Fish up §2Junk §7and trade it with §2Junker",
            "    §2Joel §7for useful items!",
            "§8 ■ §7Apply §9Rod Parts §7with §2Roddy§7.",
            "§8 ■ §7Fish §2Bayou Sea Creatures§7.",
            "§8 ■ §7Learn about §dFishing Hotspots §7from",
            "    §dHattie§7.",
            "",
            "§eClick to travel!"
        ));
        layout.slot(40, ItemStackCreator.getStackHead(
            "§bFishing Outpost",
            "d7cc6687423d0570d556ac53e0676cb563bbdd9717cd8269bdebed6f6d4e7bf8",
            1,
            "§7Your base of operations.",
            "",
            "§a§lYOU ARE HERE!"
        ));
    }
}
