package net.swofty.type.skyblockgeneric.bazaar;

import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.type.skyblockgeneric.hunting.AttributeRegistry;

import java.util.Locale;

public final class AttributeBazaarItems {
    private AttributeBazaarItems() {
    }

    public static ItemType[] values() {
        return AttributeRegistry.values().stream().map(definition -> ItemType.valueOf("SHARD_"
                        + definition.shard().toUpperCase(Locale.ROOT).replaceAll("[^A-Z0-9]+", "_")
                        .replaceAll("^_|_$", "")))
                .toArray(ItemType[]::new);
    }
}
