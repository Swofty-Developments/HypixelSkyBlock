package net.swofty.types.generic.item.items.minion.farming;

import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Minion;
import net.swofty.types.generic.minion.MinionIngredient;
import net.swofty.types.generic.minion.MinionRegistry;

import java.util.List;

public class CactusMinion implements CustomSkyBlockItem, Minion {
    @Override
    public MinionRegistry getMinionRegistry() {
        return MinionRegistry.CACTUS;
    }

    @Override
    public ItemTypeLinker getFirstBaseItem() {
        return ItemTypeLinker.WOODEN_HOE;
    }

    @Override
    public boolean isByDefaultCraftable() {
        return false;
    }

    @Override
    public List<MinionIngredient> getMinionCraftingIngredients() {
        return List.of(
                new MinionIngredient(ItemTypeLinker.CACTUS, 16),
                new MinionIngredient(ItemTypeLinker.CACTUS, 32),
                new MinionIngredient(ItemTypeLinker.CACTUS, 64),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_CACTUS_GREEN, 1),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_CACTUS_GREEN, 3),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_CACTUS_GREEN, 8),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_CACTUS_GREEN, 16),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_CACTUS_GREEN, 32),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_CACTUS_GREEN, 64),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_CACTUS, 1),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_CACTUS, 2)
        );
    }
}
