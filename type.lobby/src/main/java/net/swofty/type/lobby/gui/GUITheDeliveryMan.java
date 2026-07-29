package net.swofty.type.lobby.gui;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.DefaultState;
import net.swofty.type.generic.gui.v2.StatelessView;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ViewContext;

public class GUITheDeliveryMan extends StatelessView {

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("The Delivery Man", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        layout.slot(20, ItemStackCreator.getStack(
            "§cMystery Dust Delivery",
            Material.MINECART,
            1,
            "§7You already picked up this delivery,",
            "§7come back later!",
            "",
            "§7Next Delivery: 0d 0h 0m 0s"
        ), (defaultStateClickContext, _) -> defaultStateClickContext.player().notImplemented());
        layout.slot(21, ItemStackCreator.getStack(
            "§cMystery Dust Delivery",
            Material.MINECART,
            1,
            "§7You already picked up this delivery,",
            "§7come back later!",
            "",
            "§7Next Delivery: 0d 0h 0m 0s"
        ), (defaultStateClickContext, _) -> defaultStateClickContext.player().notImplemented());
        layout.slot(22, ItemStackCreator.getStack(
            "§cMystery Dust Delivery",
            Material.MINECART,
            1,
            "§7You already picked up this delivery,",
            "§7come back later!",
            "",
            "§7Next Delivery: 0d 0h 0m 0s"
        ), (defaultStateClickContext, _) -> defaultStateClickContext.player().notImplemented());
        layout.slot(23, ItemStackCreator.getStack(
            "§cMystery Dust Delivery",
            Material.MINECART,
            1,
            "§7You already picked up this delivery,",
            "§7come back later!",
            "",
            "§7Next Delivery: 0d 0h 0m 0s"
        ), (defaultStateClickContext, _) -> defaultStateClickContext.player().notImplemented());
        layout.slot(24, ItemStackCreator.getStack(
            "§cMystery Dust Delivery",
            Material.MINECART,
            1,
            "§7You already picked up this delivery,",
            "§7come back later!",
            "",
            "§7Next Delivery: 0d 0h 0m 0s"
        ), (defaultStateClickContext, _) -> defaultStateClickContext.player().notImplemented());
        layout.slot(29, ItemStackCreator.getStack(
            "§cSurvey",
            Material.CHEST,
            1,
            "§7There isn't a survey available right",
            "§7now!"
        ));
        layout.slot(30, ItemStackCreator.getStack(
            "§6Social Media Rewards",
            Material.CHEST_MINECART,
            1,
            "§7Click to view all available Social Media",
            "§7Rewards!"
        ), (defaultStateClickContext, _) -> defaultStateClickContext.player().notImplemented());
        layout.slot(31, ItemStackCreator.getStack(
            "§aDaily Reward",
            Material.GOLD_BLOCK,
            1,
            "",
            "§7Daily rewards for visiting our",
            "§7website including: §6Coins§7, §3Hypixel",
            "§3Experience§7, §bSkyWars Souls§7, §cUnique",
            "§cCosmetics §7and more!",
            "",
            "§7Current Streak: §b0",
            "",
            "§eClick here to get the link in chat!"
        ), (defaultStateClickContext, _) -> defaultStateClickContext.player().notImplemented());
        layout.slot(32, ItemStackCreator.getStack(
            "§6Website Link",
            Material.MINECART,
            1,
            "§7You have linked your account to the",
            "§7forums."
        ), (defaultStateClickContext, _) -> defaultStateClickContext.player().notImplemented());
        layout.slot(33, ItemStackCreator.getStack(
            "§aDaily Reward",
            Material.CHEST_MINECART, // or minecart if claimed.
            1,
            "§7Free 2,200 Network Experience",
            "§7and 3,000 Arcade Coins!",
            "",
            "§eClick here to claim!"
        ));
        /*
        layout.slot(33, ItemStackCreator.getStack(
            "§cDaily Reward",
            Material.MINECART,
            1,
            "§7You have claimed this reward",
            "§7recently! Check back in 0h 0m 0s!"));
         */
        layout.slot(40, ItemStackCreator.getStack(
            "§cHardware Survey",
            Material.MINECART,
            1,
            "§7You have already completed this",
            "§7Hardware Survey, thank you!"
        ), (defaultStateClickContext, _) -> defaultStateClickContext.player().notImplemented());
    }
}
