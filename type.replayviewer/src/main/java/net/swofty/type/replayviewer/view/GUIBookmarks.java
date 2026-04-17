package net.swofty.type.replayviewer.view;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.DefaultState;
import net.swofty.type.generic.gui.v2.StatelessView;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ViewContext;

// TODO: implement (make actually data-driven)
public class GUIBookmarks extends StatelessView {

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("Bookmarks", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        layout.slot(10, ItemStackCreator.getStack(
            "§aYellow Bed Destroyed",
            Material.PAPER,
            1,
            "§7Time: §a01:52 seconds",
            "",
            "§aPlayer: [VIP] robin_631",
            "",
            "§eLeft Click to go to this time!",
            "§eRight Click to share!"
        ));
        layout.slot(11, ItemStackCreator.getStack(
            "§aFinal Death",
            Material.PAPER,
            1,
            "§7Time: §a01:57 seconds",
            "",
            "§aPlayer: [VIP] Netanel_Yair",
            "",
            "§eLeft Click to go to this time!",
            "§eRight Click to share!"
        ));
        layout.slot(12, ItemStackCreator.getStack(
            "§aWhite Bed Destroyed",
            Material.PAPER,
            1,
            "§7Time: §a02:01 seconds",
            "",
            "§aPlayer: §7sereklo",
            "",
            "§eLeft Click to go to this time!",
            "§eRight Click to share!"
        ));
        layout.slot(13, ItemStackCreator.getStack(
            "§aFinal Death",
            Material.PAPER,
            1,
            "§7Time: §a02:03 seconds",
            "",
            "§aPlayer: [VIP] Clayterzz",
            "",
            "§eLeft Click to go to this time!",
            "§eRight Click to share!"
        ));
        layout.slot(14, ItemStackCreator.getStack(
            "§aGray Bed Destroyed",
            Material.PAPER,
            1,
            "§7Time: §a02:04 seconds",
            "",
            "§aPlayer: §7RenzeHo",
            "",
            "§eLeft Click to go to this time!",
            "§eRight Click to share!"
        ));
        layout.slot(15, ItemStackCreator.getStack(
            "§aFinal Death",
            Material.PAPER,
            1,
            "§7Time: §a02:05 seconds",
            "",
            "§aPlayer: §7Barbapapa2011",
            "",
            "§eLeft Click to go to this time!",
            "§eRight Click to share!"
        ));
        layout.slot(16, ItemStackCreator.getStack(
            "§aGreen Bed Destroyed",
            Material.PAPER,
            1,
            "§7Time: §a02:36 seconds",
            "",
            "§aPlayer: §7aspera6552",
            "",
            "§eLeft Click to go to this time!",
            "§eRight Click to share!"
        ));
        layout.slot(19, ItemStackCreator.getStack(
            "§aFinal Death",
            Material.PAPER,
            1,
            "§7Time: §a04:00 seconds",
            "",
            "§aPlayer: §b[MVP§9+§b] ArikSquad",
            "",
            "§eLeft Click to go to this time!",
            "§eRight Click to share!"
        ));
        layout.slot(20, ItemStackCreator.getStack(
            "§aPink Bed Destroyed",
            Material.PAPER,
            1,
            "§7Time: §a04:46 seconds",
            "",
            "§aPlayer: [VIP] robin_631",
            "",
            "§eLeft Click to go to this time!",
            "§eRight Click to share!"
        ));
        layout.slot(21, ItemStackCreator.getStack(
            "§aFinal Death",
            Material.PAPER,
            1,
            "§7Time: §a04:48 seconds",
            "",
            "§aPlayer: §7sereklo",
            "",
            "§eLeft Click to go to this time!",
            "§eRight Click to share!"
        ));
        layout.slot(22, ItemStackCreator.getStack(
            "§aBlue Bed Destroyed",
            Material.PAPER,
            1,
            "§7Time: §a05:23 seconds",
            "",
            "§aPlayer: §7RenzeHo",
            "",
            "§eLeft Click to go to this time!",
            "§eRight Click to share!"
        ));
        layout.slot(23, ItemStackCreator.getStack(
            "§aFinal Death",
            Material.PAPER,
            1,
            "§7Time: §a05:30 seconds",
            "",
            "§aPlayer: §7aspera6552",
            "",
            "§eLeft Click to go to this time!",
            "§eRight Click to share!"
        ));
        layout.slot(24, ItemStackCreator.getStack(
            "§aFinal Death",
            Material.PAPER,
            1,
            "§7Time: §a06:01 seconds",
            "",
            "§aPlayer: [VIP] robin_631",
            "",
            "§eLeft Click to go to this time!",
            "§eRight Click to share!"
        ));
        layout.slot(49, ItemStackCreator.getStack(
            "§aGo Back",
            Material.ARROW,
            1,
            "§7To Replay Viewer"
        ));
    }
}
