package net.swofty.types.generic.item.items.minion.farming;

import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Minion;
import net.swofty.types.generic.minion.MinionIngredient;
import net.swofty.types.generic.minion.MinionRegistry;

import java.util.List;

public class ChickenMinion implements CustomSkyBlockItem, Minion {
    @Override
    public MinionRegistry getMinionRegistry() {
        return MinionRegistry.CHICKEN;
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
                new MinionIngredient(ItemTypeLinker.RAW_CHICKEN, 8),
                new MinionIngredient(ItemTypeLinker.RAW_CHICKEN, 16),
                new MinionIngredient(ItemTypeLinker.RAW_CHICKEN, 32),
                new MinionIngredient(ItemTypeLinker.RAW_CHICKEN, 64),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_RAW_CHICKEN, 1),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_RAW_CHICKEN, 2),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_RAW_CHICKEN, 4),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_RAW_CHICKEN, 8),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_RAW_CHICKEN, 16),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_RAW_CHICKEN, 32),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_RAW_CHICKEN, 64)
        );
    }
}
