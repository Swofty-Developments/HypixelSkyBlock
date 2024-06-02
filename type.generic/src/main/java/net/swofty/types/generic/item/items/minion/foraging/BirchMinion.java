package net.swofty.types.generic.item.items.minion.foraging;

import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Minion;
import net.swofty.types.generic.minion.MinionRegistry;

public class BirchMinion implements CustomSkyBlockItem, Minion {
    @Override
    public MinionRegistry getMinionRegistry() {
        return MinionRegistry.BIRCH;
    }

    @Override
    public ItemType getBaseCraftMaterial() {
        return ItemType.BIRCH_LOG;
    }

    @Override
    public ItemType getEnchantedCraftMaterial() {
        return ItemType.ENCHANTED_BIRCH_WOOD;
    }

    @Override
    public ItemType getFirstBaseItem() {
        return ItemType.WOODEN_AXE;
    }

    @Override
    public boolean isByDefaultCraftable() {
        return false;
    }

}
