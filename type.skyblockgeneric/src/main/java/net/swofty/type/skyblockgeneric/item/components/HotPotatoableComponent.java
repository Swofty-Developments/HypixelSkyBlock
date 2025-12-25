package net.swofty.type.skyblockgeneric.item.components;

import lombok.Getter;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.commons.skyblock.item.PotatoType;
import net.swofty.type.skyblockgeneric.item.SkyBlockItemComponent;

import java.util.Map;

@Getter
public class HotPotatoableComponent extends SkyBlockItemComponent {
    private final PotatoType potatoType;

    private final Map<ItemType, Integer> applicableItems;

    public HotPotatoableComponent(PotatoType type) {
        this.potatoType = type;

        applicableItems = Map.of(ItemType.HOT_POTATO_BOOK, 10, ItemType.FUMING_POTATO_BOOK, 5);// new ApplicableItem[]{new ApplicableItem(ItemType.HOT_POTATO_BOOK, 10), new ApplicableItem(ItemType.FUMING_POTATO_BOOK, 5)};
    }

    public HotPotatoableComponent(PotatoType type, Map<ItemType, Integer> applicableItems) {
        this.potatoType = type;
        this.applicableItems = applicableItems;
    }

    public boolean canApply(ItemType type) {
        return applicableItems.containsKey(type);
    }

    public int getMax(ItemType type) {
        return applicableItems.getOrDefault(type, 0);
    }
}
