package net.swofty.types.generic.item.items.minion.farming;

import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Minion;
import net.swofty.types.generic.minion.MinionRegistry;

public class ChickenMinion implements CustomSkyBlockItem, Minion {
    @Override
    public MinionRegistry getMinionRegistry() {
        return MinionRegistry.CHICKEN;
    }

    @Override
    public ItemTypeLinker getBaseCraftMaterial() {
        return ItemTypeLinker.CHICKEN;
    }

    @Override
    public ItemTypeLinker getEnchantedCraftMaterial() {
        return ItemTypeLinker.ENCHANTED_RAW_CHICKEN;
    }

    @Override
    public ItemTypeLinker getFirstBaseItem() {
        return ItemTypeLinker.WOODEN_SWORD;
    }

    @Override
    public boolean isByDefaultCraftable() {
        return false;
    }
}
