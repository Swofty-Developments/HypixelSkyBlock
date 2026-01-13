package net.swofty.type.skyblockgeneric.item.components;

import lombok.Getter;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.bags.GUISack;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.SkyBlockItemComponent;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.List;

public class SackComponent extends SkyBlockItemComponent {
    @Getter
    private final List<ItemType> validItems;
    @Getter
    private final int maxCapacity;

    public SackComponent(List<String> items, int maxCapacity) {
        this.validItems = items.stream()
                .map(ItemType::valueOf)
                .toList();
        this.maxCapacity = maxCapacity;
        addInheritedComponent(new TrackedUniqueComponent());
        addInheritedComponent(new InteractableComponent(this::onInteract, null, null));
    }

    private void onInteract(SkyBlockPlayer player, SkyBlockItem item) {
        player.openView(new GUISack(item.getItemType(), false));
    }
}
