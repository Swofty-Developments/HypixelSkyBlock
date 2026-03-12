package net.swofty.type.garden.gui;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.Components;
import net.swofty.type.generic.gui.v2.DefaultState;
import net.swofty.type.generic.gui.v2.StatelessView;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public class GUISkyMart extends StatelessView {
    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("SkyMart", InventoryType.CHEST_4_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.fill(layout);
        Components.backOrClose(layout, 31, ctx);

        SkyBlockPlayer player = (SkyBlockPlayer) ctx.player();
        long copper = GardenGuiSupport.core(player).getCopper();

        layout.slot(4, ItemStackCreator.getStack(
            "§6Copper Purse",
            Material.COPPER_BLOCK,
            1,
            "§7Available Copper: §c" + StringUtility.commaify(copper)
        ));

        layout.slot(11, ItemStackCreator.getStack(
            "§aFarming Essentials",
            Material.GREEN_DYE,
            1,
            "§7All the farming supplies you could",
            "§7ever need!",
            "",
            "§7Entries: §e" + net.swofty.type.garden.GardenServices.desk().getSkyMartEntries("farming_essentials").size(),
            "§eClick to view!"
        ), (click, c) -> c.push(new GUISkyMartCategory("farming_essentials", "Farming Essentials")));

        layout.slot(12, ItemStackCreator.getStack(
            "§aFarming Tools",
            Material.DIAMOND_HOE,
            1,
            "§7Purchase tools made specifically for",
            "§7each crop!",
            "",
            "§7Entries: §e" + net.swofty.type.garden.GardenServices.desk().getSkyMartEntries("farming_tools").size(),
            "§eClick to view!"
        ), (click, c) -> c.push(new GUISkyMartCategory("farming_tools", "Farming Tools")));

        layout.slot(13, ItemStackCreator.getStack(
            "§aBarn Skins",
            Material.OAK_PLANKS,
            1,
            "§7Spruce up your Barn!",
            "",
            "§7Entries: §e" + net.swofty.type.garden.GardenServices.desk().getSkyMartEntries("barn_skins").size(),
            "§eClick to view!"
        ), (click, c) -> c.push(new GUISkyMartCategory("barn_skins", "Barn Skins")));

        layout.slot(14, ItemStackCreator.getStack(
            "§aGreenhouse Skins",
            Material.WHITE_STAINED_GLASS,
            1,
            "§7Make your Greenhouse look fresh!",
            "",
            "§cNo live entries yet"
        ));

        layout.slot(15, ItemStackCreator.getStack(
            "§aPests",
            Material.CHEST_MINECART,
            1,
            "§7Got pests? We got you.",
            "",
            "§cNo live entries yet"
        ));
    }
}
