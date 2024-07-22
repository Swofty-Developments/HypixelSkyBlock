package net.swofty.types.generic.item.items.minion.farming;

import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Minion;
import net.swofty.types.generic.minion.MinionRegistry;

public class MelonMinion implements CustomSkyBlockItem, Minion {
    @Override
    public MinionRegistry getMinionRegistry() {
        return MinionRegistry.MELON;
    }

    @Override
    public ItemTypeLinker getBaseCraftMaterial() {
        return ItemTypeLinker.MELON_SLICE;
    }

    @Override
    public ItemTypeLinker getEnchantedCraftMaterial() {
        return ItemTypeLinker.ENCHANTED_MELON;
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
