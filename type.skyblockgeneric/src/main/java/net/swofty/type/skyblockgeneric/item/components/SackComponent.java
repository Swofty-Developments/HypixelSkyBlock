package net.swofty.type.skyblockgeneric.item.components;

import lombok.Getter;
import net.swofty.commons.item.ItemType;
import net.swofty.type.generic.item.SkyBlockItem;
import net.swofty.type.generic.item.SkyBlockItemComponent;
import net.swofty.type.generic.user.HypixelPlayer;

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
        addInheritedComponent(new InteractableComponent(this::onInteract, this::onInteract, null));
    }

    private void onInteract(HypixelPlayer player, SkyBlockItem item) {
        // TODO
    }
}
