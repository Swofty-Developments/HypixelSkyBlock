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
        return new ViewConfiguration<>("Bed Wars Menu & Shop", InventoryType.CHEST_4_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.close(layout, 31);

        layout.slot(11, ItemStackCreator.getStack(
            "§aBed Wars Cosmetics",
            Material.ARMOR_STAND,
            1,
            "§7Browse your unlocked Bed Wars",
            "§7cosmetics, or buy them directly with",
            "§7Tokens."
        ), (_, c) -> {
            c.push(new GUIMyCosmetics());
        });
        layout.slot(13, ItemStackCreator.getStack(
            "§aSlumber Inventory",
            Material.CHEST,
            1,
            "§7Click to view your Slumber Hotel",
            "§7progress & quest log."
        ), (_, c) -> c.player().notImplemented());
        layout.slot(15, ItemStackCreator.getStack(
            "§aBed Wars Settings",
            Material.COMPARATOR,
            1,
            "§7Adjust game settings for Bed Wars."
        ), (_, c) -> c.player().notImplemented());
    }
}
