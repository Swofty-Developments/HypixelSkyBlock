package net.swofty.types.generic.item.items.minion.mining;

import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Minion;
import net.swofty.types.generic.minion.MinionIngredient;
import net.swofty.types.generic.minion.MinionRegistry;

import java.util.List;

public class SnowMinion implements CustomSkyBlockItem, Minion {
    @Override
    public MinionRegistry getMinionRegistry() {
        return MinionRegistry.SNOW;
    }

    @Override
    public ItemTypeLinker getFirstBaseItem() {
        return ItemTypeLinker.WOODEN_SHOVEL;
    }

    @Override
    public boolean isByDefaultCraftable() {
        return false;
    }

    @Override
    public List<MinionIngredient> getMinionCraftingIngredients() {
        return List.of(
                new MinionIngredient(ItemTypeLinker.SNOW_BLOCK, 4),
                new MinionIngredient(ItemTypeLinker.SNOW_BLOCK, 8),
                new MinionIngredient(ItemTypeLinker.SNOW_BLOCK, 16),
                new MinionIngredient(ItemTypeLinker.SNOW_BLOCK, 32),
                new MinionIngredient(ItemTypeLinker.SNOW_BLOCK, 64),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_SNOW_BLOCK, 1),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_SNOW_BLOCK, 2),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_SNOW_BLOCK, 4),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_SNOW_BLOCK, 8),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_SNOW_BLOCK, 16)
        );
    }
}
