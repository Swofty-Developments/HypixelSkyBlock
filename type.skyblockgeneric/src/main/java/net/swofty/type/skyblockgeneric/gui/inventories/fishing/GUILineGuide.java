package net.swofty.type.skyblockgeneric.gui.inventories.fishing;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.Components;
import net.swofty.type.generic.gui.v2.DefaultState;
import net.swofty.type.generic.gui.v2.StatelessView;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.skyblockgeneric.fishing.FishingItemSupport;
import net.swofty.type.skyblockgeneric.fishing.FishingPartCategory;

public class GUILineGuide extends StatelessView {
    private static final int[] PART_SLOTS = {10, 11, 12, 13, 14, 15, 16};

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("Line", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.close(layout, 49);
        layout.slot(4, ItemStackCreator.getStackHead(
            "§9ꨃ Lines",
            "9a850a4f721bc150bb72b067e5074c8251058a6b9111691da315b393467c1aa9",
            1,
            "§9Lines §7grant stat bonuses to your rod everywhere."
        ));

        int index = 0;
        for (var part : FishingItemSupport.getRodParts()) {
            if (part.getComponent(net.swofty.type.skyblockgeneric.item.components.FishingRodPartComponent.class).getCategory() != FishingPartCategory.LINE || index >= PART_SLOTS.length) {
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
