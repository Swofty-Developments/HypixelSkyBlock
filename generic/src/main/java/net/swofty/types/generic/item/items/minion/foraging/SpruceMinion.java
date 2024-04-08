package net.swofty.types.generic.item.items.minion.foraging;

import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Minion;
import net.swofty.types.generic.minion.MinionRegistry;

public class SpruceMinion implements CustomSkyBlockItem, Minion {
    @Override
    public MinionRegistry getMinionRegistry() {
        return MinionRegistry.SPRUCE;
    }

    @Override
    public ItemType getBaseCraftMaterial() {
        return ItemType.SPRUCE_LOG;
    }

    @Override
    public ItemType getEnchantedCraftMaterial() {
        return ItemType.ENCHANTED_SPRUCE_WOOD;
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
