package net.swofty.types.generic.item.items.minion.farming;

import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Minion;
import net.swofty.types.generic.minion.MinionRegistry;

public class WheatMinion implements CustomSkyBlockItem, Minion {
    @Override
    public MinionRegistry getMinionRegistry() {
        return MinionRegistry.WHEAT;
    }

    @Override
    public ItemType getBaseCraftMaterial() {
        return ItemType.WHEAT;
    }

    @Override
    public ItemType getEnchantedCraftMaterial() {
        return ItemType.ENCHANTED_BREAD;
    }

    @Override
    public ItemType getFirstBaseItem() {
        return ItemType.WOODEN_HOE;
    }

    @Override
    public boolean isByDefaultCraftable() {
        return false;
    }
}
