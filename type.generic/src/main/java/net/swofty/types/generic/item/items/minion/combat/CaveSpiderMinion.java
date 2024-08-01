package net.swofty.types.generic.item.items.minion.combat;

import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Minion;
import net.swofty.types.generic.minion.MinionIngredient;
import net.swofty.types.generic.minion.MinionRegistry;

import java.util.List;

public class CaveSpiderMinion implements CustomSkyBlockItem, Minion {
    @Override
    public MinionRegistry getMinionRegistry() {
        return MinionRegistry.CAVE_SPIDER;
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
                new MinionIngredient(ItemTypeLinker.SPIDER_EYE, 10),
                new MinionIngredient(ItemTypeLinker.SPIDER_EYE, 20),
                new MinionIngredient(ItemTypeLinker.SPIDER_EYE, 40),
                new MinionIngredient(ItemTypeLinker.SPIDER_EYE, 64),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_SPIDER_EYE, 1),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_SPIDER_EYE, 3),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_SPIDER_EYE, 8),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_SPIDER_EYE, 16),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_SPIDER_EYE, 32),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_SPIDER_EYE, 64),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_FERMENTED_SPIDER_EYE, 2)
        );
    }
}
