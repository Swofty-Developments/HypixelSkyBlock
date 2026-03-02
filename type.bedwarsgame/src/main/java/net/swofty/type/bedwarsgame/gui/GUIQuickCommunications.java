package net.swofty.type.bedwarsgame.gui;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.DefaultState;
import net.swofty.type.generic.gui.v2.StatelessView;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ViewContext;

public class GUIQuickCommunications extends StatelessView {

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("Quick Communications", InventoryType.CHEST_5_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        layout.slot(10, ItemStackCreator.getStack(
            "§aHello ( ﾟ◡ﾟ)/!",
            Material.BOOK,
            1,
            "",
            "§eClick to send!"
        ));
        layout.slot(11, ItemStackCreator.getStack(
            "§aI'm coming back to base!",
            Material.BOOK,
            1,
            "",
            "§eClick to send!"
        ));
        layout.slot(12, ItemStackCreator.getStack(
            "§aI'm defending!",
            Material.IRON_BARS,
            1,
            "",
            "§eClick to send!"
        ));
        layout.slot(13, ItemStackCreator.getStack(
            "§aI'm attacking!",
            Material.IRON_SWORD,
            1,
            "§7You will be able to select the Team.",
            "",
            "§eClick to send!"
        ));
        layout.slot(14, ItemStackCreator.getStack(
            "§aI'm collecting resources!",
            Material.DIAMOND,
            1,
            "§7You will be able to select the",
            "§7Resource.",
            "",
            "§eClick to send!"
        ));
        layout.slot(15, ItemStackCreator.getStack(
            "§aI have resources!",
            Material.CHEST,
            1,
            "§7You will be able to select the",
            "§7Resource.",
            "",
            "§eClick to send!"
        ));
        layout.slot(20, ItemStackCreator.getStack(
            "§aThank You!",
            Material.BOOK,
            1,
            "",
            "§eClick to send!"
        ));
        layout.slot(21, ItemStackCreator.getStack(
            "§aGet back to base!",
            Material.BOOK,
            1,
            "",
            "§eClick to send!"
        ));
        layout.slot(22, ItemStackCreator.getStack(
            "§aPlease defend!",
            Material.IRON_BARS,
            1,
            "",
            "§eClick to send!"
        ));
        layout.slot(23, ItemStackCreator.getStack(
            "§aLet's attack!",
            Material.IRON_SWORD,
            1,
            "§7You will be able to select the Team.",
            "",
            "§eClick to send!"
        ));
        layout.slot(24, ItemStackCreator.getStack(
            "§aWe need resources!",
            Material.DIAMOND,
            1,
            "§7You will be able to select the",
            "§7Resource.",
            "",
            "§eClick to send!"
        ));
        layout.slot(25, ItemStackCreator.getStack(
            "§aPlayer incoming!!",
            Material.FEATHER,
            1,
            "",
            "§eClick to send!"
        ));
        layout.slot(40, ItemStackCreator.getStack(
            "§aGo Back",
            Material.ARROW,
            1,
            "§7To Tracker & Communication"
        ));
    }
}
