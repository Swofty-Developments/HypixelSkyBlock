package net.swofty.types.generic.item.items.minion.mining;

import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Minion;
import net.swofty.types.generic.minion.MinionIngredient;
import net.swofty.types.generic.minion.MinionRegistry;

import java.util.List;

public class SandMinion implements CustomSkyBlockItem, Minion {
    @Override
    public MinionRegistry getMinionRegistry() {
        return MinionRegistry.SAND;
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
                new MinionIngredient(ItemTypeLinker.SAND, 10),
                new MinionIngredient(ItemTypeLinker.SAND, 20),
                new MinionIngredient(ItemTypeLinker.SAND, 40),
                new MinionIngredient(ItemTypeLinker.SAND, 64),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_SAND, 1),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_SAND, 2),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_SAND, 4),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_SAND, 8),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_SAND, 16),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_SAND, 32),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_SAND, 64)
        );
    }
}
