package net.swofty.types.generic.item.items.minion.farming;

import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Minion;
import net.swofty.types.generic.minion.MinionRegistry;

public class MushroomMinion implements CustomSkyBlockItem, Minion {
    @Override
    public MinionRegistry getMinionRegistry() {
        return MinionRegistry.MUSHROOM;
    }

    @Override
    public ItemTypeLinker getBaseCraftMaterial() {
        return ItemTypeLinker.BROWN_MUSHROOM;
    }

    @Override
    public ItemTypeLinker getEnchantedCraftMaterial() {
        return ItemTypeLinker.ENCHANTED_BROWN_MUSHROOM;
    }

    @Override
    public ItemTypeLinker getFirstBaseItem() {
        return ItemTypeLinker.WOODEN_HOE;
    }

    @Override
    public boolean isByDefaultCraftable() {
        return false;
    }
}
