package net.swofty.type.bedwarslobby.gui;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.Components;
import net.swofty.type.generic.gui.v2.DefaultState;
import net.swofty.type.generic.gui.v2.StatelessView;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ViewContext;

// TODO: dynamic
public class GUIDreamCalendar extends StatelessView {

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("Dream Calendar", InventoryType.CHEST_4_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.back(layout, 31, ctx);

        layout.slot(10, ItemStackCreator.getStack(
            "§6✸ §bOne Block",
            Material.RED_BED,
            1,
            "§8Dream Rotation",
            "",
            "§7Every few seconds brings a new",
            "§7surprise! Use these items to defend",
            "§7your bed or destroy enemy beds.",
            "",
            "§aThis mode is currently featured."
        ));
        layout.slot(11, ItemStackCreator.getStack(
            "§bRush V2",
            Material.ENDER_EYE,
            1,
            "§8Dream Rotation",
            "",
            "§7Everything is upgraded at the start!",
            "§7Fight to the death right away!",
            "",
            "§eThis mode will be featured in 5 days."
        ));
        layout.slot(12, ItemStackCreator.getStack(
            "§bUltimate V2",
            Material.NETHER_STAR,
            1,
            "§8Dream Rotation",
            "",
            "§7Pick an Ultimate Ability to use any",
            "§7time during the battle!",
            "",
            "§eThis mode will be featured in 12 days."
        ));
        layout.slot(13, ItemStackCreator.getStack(
            "§b40v40 Castle V2",
            Material.STONE_BRICKS,
            40,
            "§8Dream Rotation",
            "",
            "§7Massive 40 versus 40 all out war on",
            "§7a unique Castle map!",
            "",
            "§eThis mode will be featured in 19 days."
        ));
        layout.slot(14, ItemStackCreator.getStack(
            "§bVoidless",
            Material.BEDROCK,
            1,
            "§8Dream Rotation",
            "",
            "§7No longer can you be a victim of the",
            "§7Void!",
            "",
            "§eThis mode will be featured in 26 days."
        ));
        layout.slot(15, ItemStackCreator.getStack(
            "§bArmed",
            Material.DIAMOND_HOE,
            1,
            "§8Dream Rotation",
            "",
            "§7Utilize a new arsenal of ranged",
            "§7weapons to win the game!",
            "",
            "§eThis mode will be featured in 33 days."
        ));
        layout.slot(16, ItemStackCreator.getStackHead(
            "§bLucky Blocks V2",
            "50d8f863e9b42653e642711ee8b854dd8f9463ef4bfcde7db9776daadb532b",
            1,
            "§8Dream Rotation",
            "",
            "§7Find the Lucky Blocks to earn a",
            "§7variety of events and effects,",
            "§7leading to total mayhem!",
            "",
            "§eThis mode will be featured in 40 days."
        ));
    }
}
