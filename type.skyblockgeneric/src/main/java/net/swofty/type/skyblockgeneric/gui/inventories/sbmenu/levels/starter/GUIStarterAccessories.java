package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.levels.starter;

import net.minestom.server.inventory.InventoryType;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.type.generic.gui.v2.*;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.skyblockgeneric.levels.SkyBlockLevelCause;
import net.swofty.type.skyblockgeneric.levels.causes.NewAccessoryLevelCause;

import java.util.List;
import java.util.stream.Stream;

public class GUIStarterAccessories extends StatelessView {
    private final List<NewAccessoryLevelCause> causes;

    public GUIStarterAccessories(ItemType... causes) {
        this.causes = Stream.of(causes).map(SkyBlockLevelCause::getAccessoryCause).toList();
    }

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("Starter -> Accessories", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.fill(layout);
        Components.close(layout, 49);
        Components.back(layout, 48, ctx);
        // TODO: Add accessory-related content using causes
    }
}
