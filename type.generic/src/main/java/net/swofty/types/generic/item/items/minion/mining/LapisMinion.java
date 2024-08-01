package net.swofty.types.generic.item.items.minion.mining;

import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Minion;
import net.swofty.types.generic.minion.MinionIngredient;
import net.swofty.types.generic.minion.MinionRegistry;

import java.util.List;

public class LapisMinion implements CustomSkyBlockItem, Minion {
    @Override
    public MinionRegistry getMinionRegistry() {
        return MinionRegistry.LAPIS;
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
                new MinionIngredient(ItemTypeLinker.LAPIS_LAZULI, 32),
                new MinionIngredient(ItemTypeLinker.LAPIS_LAZULI, 64),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_LAPIS_LAZULI, 1),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_LAPIS_LAZULI, 3),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_LAPIS_LAZULI, 8),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_LAPIS_LAZULI, 16),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_LAPIS_LAZULI, 32),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_LAPIS_LAZULI, 64),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_LAPIS_LAZULI_BLOCK, 1),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_LAPIS_LAZULI_BLOCK, 2),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_LAPIS_LAZULI_BLOCK, 4)
        );
    }
}
