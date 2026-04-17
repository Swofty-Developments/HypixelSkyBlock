package net.swofty.type.replayviewer.view;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.DefaultState;
import net.swofty.type.generic.gui.v2.StatelessView;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ViewContext;

// TODO: implement
// think about moving to type.game as the real game has this same view when on spectator mode
public class GUIViewPlayer extends StatelessView {

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("ArikSquad", InventoryType.CHEST_2_ROW); // show player username
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        layout.slot(0, ItemStackCreator.getStackHead(
            "§b[MVP§9+§b] ArikSquad",
            "859269972cf257cd23db45a61bbd1f7d647b7966d14bd6ba07ef1d2630b23be2",
            1,
            "§7Food Level: §f20",
            "",
            "§eRight Click for first person!"
        ));
        layout.slot(1, ItemStackCreator.getStack(
            "§aActive Status Effects",
            Material.POTION,
            1,
            "§7No status effects."
        ));

        // main hand
        layout.slot(3, ItemStackCreator.getStack(
            "Oak Planks",
            Material.OAK_PLANKS,
            2
        ));

        // helmet
        layout.slot(5, ItemStackCreator.getStack(
            "§cEmpty helmet slot.",
            Material.RED_STAINED_GLASS_PANE,
            1
        ));

        // chestplate
        layout.slot(6, ItemStackCreator.getStack(
            "§cEmpty chestplate slot.",
            Material.RED_STAINED_GLASS_PANE,
            1
        ));

        // leggings
        layout.slot(7, ItemStackCreator.getStack(
            "Leather Pants",
            Material.LEATHER_LEGGINGS,
            1
        ));

        // boots
        layout.slot(8, ItemStackCreator.getStack(
            "§cEmpty boots slot.",
            Material.RED_STAINED_GLASS_PANE,
            1
        ));

        // report
        layout.slot(9, ItemStackCreator.getStack(
            "§aReport Player",
            Material.ANVIL,
            1,
            "§7Report this player for breaking the",
            "§7rules. This replay will be saved",
            "§7along with the report to be reviewed.",
            "",
            "§eClick to report!"
        ));
    }
}
