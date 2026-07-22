package net.swofty.type.galatea.gui;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.*;
import net.swofty.type.generic.gui.v2.context.ViewContext;

public class GUIDavid extends StatelessView {

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("David", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.fill(layout);
        Components.close(layout, 49);

        layout.slot(20, ItemStackCreator.getStack(
                "§aHunting Tools",
                Material.GOLDEN_AXE,
                1,
                "§7You don't know which Hunting Gear to",
                "§7buy, or how to use them? I’ll",
                "§7explain everything!",
                "",
                "§eClick to open!"
        ));
        layout.slot(22, ItemStackCreator.getStack(
                "§aHunting Lessons",
                Material.LEAD,
                1,
                "§7You still have questions regarding",
                "§7Shards, Attributes, or Fusions? I can",
                "§7explain it all!",
                "",
                "§eClick to open!"
        ));
        layout.slot(24, ItemStackCreator.getStackHead(
                "§aDavid's Cloak",
                "3582fcde68921b14bf6ebda6d63fec81b2c0e668e2fc2fbfe9af6f4b7d72e2f2",
                1,
                "§7The more you Hunt, the stronger my",
                "§7cloak gets, check it out!",
                "",
                "§eClick to view!"
        ));
    }
}
