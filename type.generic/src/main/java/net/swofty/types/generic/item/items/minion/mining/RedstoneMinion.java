package net.swofty.types.generic.item.items.minion.mining;

import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Minion;
import net.swofty.types.generic.minion.MinionIngredient;
import net.swofty.types.generic.minion.MinionRegistry;

import java.util.List;

public class RedstoneMinion implements CustomSkyBlockItem, Minion {
    @Override
    public MinionRegistry getMinionRegistry() {
        return MinionRegistry.REDSTONE;
    }

    @Override
    public ItemTypeLinker getFirstBaseItem() {
        return ItemTypeLinker.WOODEN_PICKAXE;
    }

    @Override
    public boolean isByDefaultCraftable() {
        return false;
    }

    @Override
    public List<MinionIngredient> getMinionCraftingIngredients() {
        return List.of(
                new MinionIngredient(ItemTypeLinker.REDSTONE, 16),
                new MinionIngredient(ItemTypeLinker.REDSTONE, 32),
                new MinionIngredient(ItemTypeLinker.REDSTONE, 64),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_REDSTONE, 1),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_REDSTONE, 3),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_REDSTONE, 8),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_REDSTONE, 16),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_REDSTONE, 32),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_REDSTONE, 64),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_REDSTONE_BLOCK, 1),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_REDSTONE_BLOCK, 2)
        );
    }
}
