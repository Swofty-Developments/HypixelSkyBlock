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

public class GUIBedWarsMenuShop extends StatelessView {

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("Bed Wars Menu", InventoryType.CHEST_4_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.close(layout, 31);

        layout.slot(11, ItemStackCreator.getStack(
            "§3The Slumber Hotel",
            Material.SAND,
            1,
            "§7Progress through the hotel to earn",
            "§7more §drewards§7, §bxp§7, and new §acosmetics§7!",
            "",
            "§eClick to open!"
        ), (_, c) -> c.player().notImplemented());
        layout.slot(13, ItemStackCreator.getStack(
            "§aMy Cosmetics",
            Material.ARMOR_STAND,
            1,
            "§7Browse and equip all the available",
            "§7in-game Bed Wars cosmetics.",
            "",
            "§eClick to browse!"
        ), (_, c) -> {
            c.push(new GUIMyCosmetics());
        });
        layout.slot(15, ItemStackCreator.getStack(
            "§aGame Settings",
            Material.COMPARATOR,
            1,
            "§7Adjust game settings for Bed Wars.",
            "",
            "§eClick to open!"
        ), (_, c) -> c.player().notImplemented());
    }
}
