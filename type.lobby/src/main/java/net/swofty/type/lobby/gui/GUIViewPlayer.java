package net.swofty.type.lobby.gui;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.DefaultState;
import net.swofty.type.generic.gui.v2.StatelessView;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ViewContext;

public class GUIViewPlayer extends StatelessView {

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return ViewConfiguration.withString((_, ctx) -> ctx.player().getUsername(), InventoryType.CHEST_6_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        layout.slot(0, ItemStackCreator.getStackHead(
            ctx.player().getFullDisplayName(),
            ctx.player().getPlayerSkin(),
            1,
            "§7Hypixel Level: §6177",
            "§7Achievement Points: §e8,505",
            "§7Guild: §bNONE"
        ));
        layout.slot(22, ItemStackCreator.getStackHead(
                "§aSocial Media",
                "3685a0be743e9067de95cd8c6d1ba21ab21d37371b3d597211bb75e43279",
                1,
                "§7Click to view Player's Social Media",
                "§7links."
        ));
        layout.slot(23, ItemStackCreator.getStack(
            "§aReport " + ctx.player().getUsername(),
            Material.PAPER,
            1
        ));
        layout.slot(29, ItemStackCreator.getStackHead(
            "§aGift a Rank",
            "84e1c42f11383b9dc8e67f2846fa311b16320f2c2ec7e175538dbff1dd94bb7",
            1,
            "§7Gift a rank to " + ctx.player().getFullDisplayName() + ".",
            "",
            "§eClick to gift!"
        ));
        layout.slot(30, ItemStackCreator.getStack(
            "§aInvite to Party",
            Material.DIAMOND,
            1,
            "§7Click here to invite " + ctx.player().getUsername() + " to a party."
        ));
        layout.slot(31, ItemStackCreator.getStack(
            "§aAdd Friend",
            Material.WRITABLE_BOOK,
            1,
            "§7Click here to send " + ctx.player().getUsername() + " a friend",
            "§7request."
        ));
        layout.slot(32, ItemStackCreator.getStack(
            "§aBlock Player",
            Material.HOPPER,
            1,
            "§7Click here to add " + ctx.player().getUsername() + " to your",
            "§7block list."
        ));
        layout.slot(33, ItemStackCreator.getStack(
            "§aSend Duel Request",
            Material.IRON_SWORD,
            1
        ));
        layout.slot(39, ItemStackCreator.getStack(
            "§aInvite to guild",
            Material.GRAY_STAINED_GLASS_PANE,
            1,
            "§7You can't invite " + ctx.player().getUsername() + " to your guild",
            "§7because they are already in a guild."
        ));
        layout.slot(40, ItemStackCreator.getStack(
            "§aPromote Party Role",
            Material.GRAY_STAINED_GLASS_PANE,
            1,
            "§7You must be in a party to use this."
        ));
        layout.slot(41, ItemStackCreator.getStack(
            "§aPromote to higher guild rank",
            Material.GRAY_STAINED_GLASS_PANE,
            1,
            "§7You can't use this because " + ctx.player().getUsername(),
            "§7isn't in your guild."
        ));
        layout.slot(49, ItemStackCreator.getStack(
            "§aClose",
            Material.ARROW,
            1
        ));
    }
}
