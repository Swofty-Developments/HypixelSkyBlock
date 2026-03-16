package net.swofty.type.skyblockgeneric.fishing;

import net.swofty.type.skyblockgeneric.enchantment.SkyBlockEnchantment;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;

public final class FishingItemBootstrap {
    private FishingItemBootstrap() {
    }

    public static void applyDefaults(SkyBlockItem item) {
        if (item.getAttributeHandler().getPotentialType() == null) {
            return;
        }

        FishingRodDefinition definition = FishingItemCatalog.getRod(item.getAttributeHandler().getPotentialType().name());
        if (definition == null) {
            return;
        }

        if (item.getAttributeHandler().getEnchantments().findAny().isEmpty()) {
            definition.enchantments().forEach((type, level) ->
                item.getAttributeHandler().addEnchantment(new SkyBlockEnchantment(type, level)));
        }
    }
}
