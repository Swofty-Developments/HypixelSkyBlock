package net.swofty.types.generic.item.impl.recipes;

import net.swofty.types.generic.item.ItemTypeLinker;

import java.util.Arrays;
import java.util.List;

public enum ExchangeableType {
    WOOD_TYPES(ItemTypeLinker.OAK_WOOD, ItemTypeLinker.OAK_LOG);

    private final List<ItemTypeLinker> exchangeableMaterials;

    ExchangeableType(ItemTypeLinker... materials) {
        this.exchangeableMaterials = Arrays.asList(materials);
    }

    public static boolean isExchangeable(ItemTypeLinker material1, ItemTypeLinker material2) {
        return Arrays.stream(values())
                .anyMatch(exchangeableType -> exchangeableType.exchangeableMaterials.contains(material1)
                        && exchangeableType.exchangeableMaterials.contains(material2));
    }
}