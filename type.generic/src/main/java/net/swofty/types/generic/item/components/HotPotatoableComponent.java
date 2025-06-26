package net.swofty.types.generic.item.components;

import lombok.Getter;
import net.swofty.commons.item.PotatoType;
import net.swofty.types.generic.item.SkyBlockItemComponent;

@Getter
public class HotPotatoableComponent extends SkyBlockItemComponent {
    private final PotatoType potatoType;

    private AppliableItem[] appliableItems;

    public HotPotatoableComponent(PotatoType type) {
        this.potatoType = type;

        appliableItems = {new AppliableItem(ItemType.HOT_POTATO_BOOK, 10), new AppliableItem(ItemType.FUMING_POTATO_BOOK, 5)};
    }

    public record AppliableItem(ItemType itemType, int max)
}
