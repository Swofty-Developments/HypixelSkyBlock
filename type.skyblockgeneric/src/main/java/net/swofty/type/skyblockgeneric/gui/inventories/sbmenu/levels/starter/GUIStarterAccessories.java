package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.levels.starter;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.swofty.commons.item.ItemType;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.skyblockgeneric.levels.SkyBlockLevelCause;
import net.swofty.type.skyblockgeneric.levels.causes.NewAccessoryLevelCause;

import java.util.List;
import java.util.stream.Stream;

public class GUIStarterAccessories extends HypixelInventoryGUI {
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
