package net.swofty.types.generic.item.items.minion.combat;

import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Minion;
import net.swofty.types.generic.minion.MinionRegistry;

public class BlazeMinion implements CustomSkyBlockItem, Minion {
    @Override
    public MinionRegistry getMinionRegistry() {
        return MinionRegistry.BLAZE;
    }

    @Override
    public ItemTypeLinker getBaseCraftMaterial() {
        return ItemTypeLinker.BLAZE_ROD;
    }

    @Override
    public ItemTypeLinker getEnchantedCraftMaterial() {
        return ItemTypeLinker.ENCHANTED_BLAZE_POWDER;
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
