package net.swofty.types.generic.item.items.minion.mining;

import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Minion;
import net.swofty.types.generic.minion.MinionIngredient;
import net.swofty.types.generic.minion.MinionRegistry;

import java.util.List;

public class GravelMinion implements CustomSkyBlockItem, Minion {
    @Override
    public MinionRegistry getMinionRegistry() {
        return MinionRegistry.GRAVEL;
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
                new MinionIngredient(ItemTypeLinker.GRAVEL, 10),
                new MinionIngredient(ItemTypeLinker.GRAVEL, 20),
                new MinionIngredient(ItemTypeLinker.GRAVEL, 40),
                new MinionIngredient(ItemTypeLinker.GRAVEL, 64),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_FLINT, 1),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_FLINT, 2),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_FLINT, 4),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_FLINT, 8),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_FLINT, 16),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_FLINT, 32),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_FLINT, 64)
        );
    }
}
