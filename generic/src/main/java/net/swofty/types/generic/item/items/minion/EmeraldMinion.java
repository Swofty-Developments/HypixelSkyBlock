package net.swofty.types.generic.item.items.minion;

import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Minion;
import net.swofty.types.generic.minion.MinionRegistry;

public class EmeraldMinion implements CustomSkyBlockItem, Minion {
    @Override
    public MinionRegistry getMinionRegistry() {
        return MinionRegistry.EMERALD;
    }

    @Override
    public ItemType getBaseCraftMaterial() {
        return ItemType.EMERALD;
    }

    @Override
    public ItemType getEnchantedCraftMaterial() {
        return ItemType.ENCHANTED_EMERALD_BLOCK;
    }

    @Override
    public ItemType getFirstBaseItem() {
        return ItemType.IRON_PICKAXE;
    }

}
