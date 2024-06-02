package net.swofty.types.generic.gui.inventory.inventories.sbmenu.levels.starter;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.levels.SkyBlockLevelCause;
import net.swofty.types.generic.levels.causes.NewAccessoryLevelCause;

import java.util.List;
import java.util.stream.Stream;

public class GUIStarterAccessories extends SkyBlockInventoryGUI {
    private List<NewAccessoryLevelCause> causes;

    public GUIStarterAccessories(ItemType... causes) {
        super("Starter -> Accessories", InventoryType.CHEST_6_ROW);

        this.causes = Stream.of(causes).map(SkyBlockLevelCause::getAccessoryCause).toList();
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {

    }
}
