package net.swofty.types.generic.item.items.minion.mining;

import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Minion;
import net.swofty.types.generic.minion.MinionRegistry;

public class RedstoneMinion implements CustomSkyBlockItem, Minion {
    @Override
    public MinionRegistry getMinionRegistry() {
        return MinionRegistry.REDSTONE;
    }

    @Override
    public ItemType getBaseCraftMaterial() {
        return ItemType.REDSTONE;
    }

    @Override
    public ItemType getEnchantedCraftMaterial() {
        return ItemType.ENCHANTED_REDSTONE;
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
