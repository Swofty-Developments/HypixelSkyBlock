package net.swofty.types.generic.item.items.minion.farming;

import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Minion;
import net.swofty.types.generic.minion.MinionIngredient;
import net.swofty.types.generic.minion.MinionRegistry;

import java.util.List;

public class MushroomMinion implements CustomSkyBlockItem, Minion {
    @Override
    public MinionRegistry getMinionRegistry() {
        return MinionRegistry.MUSHROOM;
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
                new MinionIngredient(ItemTypeLinker.RED_MUSHROOM, 10),
                new MinionIngredient(ItemTypeLinker.RED_MUSHROOM, 20),
                new MinionIngredient(ItemTypeLinker.RED_MUSHROOM, 40),
                new MinionIngredient(ItemTypeLinker.RED_MUSHROOM, 64),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_RED_MUSHROOM, 1),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_RED_MUSHROOM, 2),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_RED_MUSHROOM, 4),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_RED_MUSHROOM, 8),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_RED_MUSHROOM, 16),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_RED_MUSHROOM, 32),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_RED_MUSHROOM, 64)
        );
    }
}
