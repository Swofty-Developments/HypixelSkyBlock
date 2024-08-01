package net.swofty.types.generic.item.items.minion.combat;

import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Minion;
import net.swofty.types.generic.minion.MinionIngredient;
import net.swofty.types.generic.minion.MinionRegistry;

import java.util.List;

public class MagmaCubeMinion implements CustomSkyBlockItem, Minion {
    @Override
    public MinionRegistry getMinionRegistry() {
        return MinionRegistry.MAGMA_CUBE;
    }

    @Override
    public ItemTypeLinker getFirstBaseItem() {
        return ItemTypeLinker.WOODEN_SWORD;
    }

    @Override
    public boolean isByDefaultCraftable() {
        return false;
    }

    @Override
    public List<MinionIngredient> getMinionCraftingIngredients() {
        return List.of(
                new MinionIngredient(ItemTypeLinker.MAGMA_CREAM, 10),
                new MinionIngredient(ItemTypeLinker.MAGMA_CREAM, 20),
                new MinionIngredient(ItemTypeLinker.MAGMA_CREAM, 40),
                new MinionIngredient(ItemTypeLinker.MAGMA_CREAM, 64),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_MAGMA_CREAM, 1),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_MAGMA_CREAM, 2),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_MAGMA_CREAM, 4),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_MAGMA_CREAM, 8),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_MAGMA_CREAM, 16),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_MAGMA_CREAM, 32),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_MAGMA_CREAM, 64)
        );
    }
}
