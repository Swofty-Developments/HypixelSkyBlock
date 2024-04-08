package net.swofty.types.generic.item.items.minion.foraging;

import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Minion;
import net.swofty.types.generic.minion.MinionRegistry;

public class DarkOakMinion implements CustomSkyBlockItem, Minion {
    @Override
    public MinionRegistry getMinionRegistry() {
        return MinionRegistry.DARK_OAK;
    }

    @Override
    public ItemType getBaseCraftMaterial() {
        return ItemType.DARK_OAK_LOG;
    }

    @Override
    public ItemType getEnchantedCraftMaterial() {
        return ItemType.ENCHANTED_DARK_OAK_WOOD;
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
