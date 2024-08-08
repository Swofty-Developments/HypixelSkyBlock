package net.swofty.types.generic.item.items.minion.combat;

import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Minion;
import net.swofty.types.generic.minion.MinionIngredient;
import net.swofty.types.generic.minion.MinionRegistry;

import java.util.List;

public class SkeletonMinion implements CustomSkyBlockItem, Minion {
    @Override
    public MinionRegistry getMinionRegistry() {
        return MinionRegistry.SKELETON;
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
                new MinionIngredient(ItemTypeLinker.BONE, 10),
                new MinionIngredient(ItemTypeLinker.BONE, 20),
                new MinionIngredient(ItemTypeLinker.BONE, 40),
                new MinionIngredient(ItemTypeLinker.BONE, 64),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_BONE, 1),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_BONE, 2),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_BONE, 4),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_BONE, 8),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_BONE, 16),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_BONE, 32),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_BONE, 64)
        );
    }
}
