package net.swofty.type.skyblockgeneric.item.crafting;

import net.swofty.commons.skyblock.item.ItemType;

import java.util.Arrays;
import java.util.List;

public enum ExchangeableType {
    WOOD_TYPES(ItemType.OAK_WOOD, ItemType.OAK_LOG);

    private final List<ItemType> exchangeableMaterials;

    ExchangeableType(ItemType... materials) {
        this.exchangeableMaterials = Arrays.asList(materials);
    }

    public static boolean isExchangeable(ItemType material1, ItemType material2) {
        return Arrays.stream(values())
                .anyMatch(exchangeableType -> exchangeableType.exchangeableMaterials.contains(material1)
                        && exchangeableType.exchangeableMaterials.contains(material2));
    }
}