package net.swofty.types.generic.item.items.minion.mining;

import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Minion;
import net.swofty.types.generic.minion.MinionRegistry;

public class SandMinion implements CustomSkyBlockItem, Minion {
    @Override
    public MinionRegistry getMinionRegistry() {
        return MinionRegistry.SAND;
    }

    @Override
    public ItemType getBaseCraftMaterial() {
        return ItemType.SAND;
    }

    @Override
    public ItemType getEnchantedCraftMaterial() {
        return ItemType.ENCHANTED_SAND;
    }

    @Override
    public ItemType getFirstBaseItem() {
        return ItemType.WOODEN_SHOVEL;
    }

    @Override
    public boolean isByDefaultCraftable() {
        return false;
    }
}
