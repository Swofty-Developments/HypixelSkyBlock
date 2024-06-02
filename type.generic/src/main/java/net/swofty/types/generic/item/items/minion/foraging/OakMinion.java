package net.swofty.types.generic.item.items.minion.foraging;

import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Minion;
import net.swofty.types.generic.minion.MinionRegistry;

public class OakMinion implements CustomSkyBlockItem, Minion {
    @Override
    public MinionRegistry getMinionRegistry() {
        return MinionRegistry.OAK;
    }

    @Override
    public ItemType getBaseCraftMaterial() {
        return ItemType.OAK_LOG;
    }

    @Override
    public ItemType getEnchantedCraftMaterial() {
        return ItemType.ENCHANTED_OAK_WOOD;
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
