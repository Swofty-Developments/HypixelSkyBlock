package net.swofty.types.generic.item.components;

import lombok.Getter;
import net.swofty.commons.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.SkyBlockItemComponent;
import net.swofty.types.generic.user.SkyBlockPlayer;

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

    private void onInteract(SkyBlockPlayer player, SkyBlockItem item) {
        // TODO
    }
}
