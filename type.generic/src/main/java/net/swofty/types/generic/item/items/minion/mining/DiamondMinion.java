package net.swofty.types.generic.item.items.minion.mining;

import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Minion;
import net.swofty.types.generic.minion.MinionRegistry;

public class DiamondMinion implements CustomSkyBlockItem, Minion {
    @Override
    public MinionRegistry getMinionRegistry() {
        return MinionRegistry.DIAMOND;
    }

    @Override
    public ItemType getBaseCraftMaterial() {
        return ItemType.DIAMOND;
    }

    @Override
    public ItemType getEnchantedCraftMaterial() {
        return ItemType.ENCHANTED_DIAMOND_BLOCK;
    }

    @Override
    public ItemType getFirstBaseItem() {
        return ItemType.WOODEN_PICKAXE;
    }

    @Override
    public boolean isByDefaultCraftable() {
        return false;
    }

}
