package net.swofty.types.generic.item.items.minion;

import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Minion;
import net.swofty.types.generic.minion.MinionRegistry;

public class ObsidianMinion implements CustomSkyBlockItem, Minion {
    @Override
    public MinionRegistry getMinionRegistry() {
        return MinionRegistry.OBSIDIAN;
    }

    @Override
    public ItemType getBaseCraftMaterial() {
        return ItemType.OBSIDIAN;
    }

    @Override
    public ItemType getEnchantedCraftMaterial() {
        return ItemType.ENCHANTED_OBSIDIAN;
    }

    @Override
    public ItemType getFirstBaseItem() {
        return ItemType.WOODEN_PICKAXE;
    }
//hm
}
