package net.swofty.type.backwaterbayou.gui;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.Components;
import net.swofty.type.generic.gui.v2.DefaultState;
import net.swofty.type.generic.gui.v2.StatelessView;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.skyblockgeneric.fishing.FishingItemCatalog;
import net.swofty.type.skyblockgeneric.fishing.RodPartDefinition;
import net.swofty.type.skyblockgeneric.gui.inventories.fishing.FishingGuideStackFactory;

public class GUIHook extends StatelessView {
    private static final int[] PART_SLOTS = {10, 11, 12, 13, 14, 15, 16};

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("Hook", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.close(layout, 49);
        layout.slot(4, ItemStackCreator.getStackHead(
            "§9ථ Hooks",
            "9809753cbab0380c7a1c18925faf9b51e44caadd1e5748542b0f23835f4ef64e",
            1,
            "§9Hooks §7change what your rod is better at catching."
        ));

        int index = 0;
        for (RodPartDefinition part : FishingItemCatalog.getRodParts()) {
            if (part.category() != RodPartDefinition.PartCategory.HOOK || index >= PART_SLOTS.length) {
                continue;
            }
            layout.slot(PART_SLOTS[index++], FishingGuideStackFactory.buildRodPartStack(part));
        }
        layout.slot(48, ItemStackCreator.getStack(
            "§aGo Back",
            Material.ARROW,
            1,
            "§7To Rod Part Guide"
        ), (_, viewCtx) -> viewCtx.pop());
    }
}
