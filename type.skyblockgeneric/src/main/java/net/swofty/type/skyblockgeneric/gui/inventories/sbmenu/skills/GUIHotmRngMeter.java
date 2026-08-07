package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.skills;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.Components;
import net.swofty.type.generic.gui.v2.DefaultState;
import net.swofty.type.generic.gui.v2.StatelessView;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ViewContext;

public class GUIHotmRngMeter extends StatelessView {
    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("Crystal Nucleus RNG Meter", InventoryType.CHEST_3_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.fill(layout);
        Components.close(layout, 22);
        layout.slot(11, ItemStackCreator.getStack("§dProgress", Material.PAPER, 1,
                "§7The selected drop is guaranteed when", "§7the meter reaches §d1M Nucleus XP§7.", "", "§7Progress: §d1.1%", "§d§m                         §f  §d11,000§5/§d1M"));
        layout.slot(13, ItemStackCreator.getStack("§6Divan's Alloy", Material.GOLD_BLOCK, 1,
                "§7Selected RNG Drop", "", "§eThis meter is currently a stub."));
        layout.slot(15, ItemStackCreator.getStack("§dCrystal Nucleus RNG Meter", Material.PAPER, 1,
                "§7Complete the Crystal Nucleus to gain", "§91,000 Nucleus XP§7 toward this meter.", "", "§eClick to view!"));
        Components.backOrClose(layout, 18, ctx);
    }
}
