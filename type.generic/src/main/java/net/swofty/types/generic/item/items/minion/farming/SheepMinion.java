package net.swofty.types.generic.item.items.minion.farming;

import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Minion;
import net.swofty.types.generic.minion.MinionIngredient;
import net.swofty.types.generic.minion.MinionRegistry;

import java.util.List;

public class SheepMinion implements CustomSkyBlockItem, Minion {
    @Override
    public MinionRegistry getMinionRegistry() {
        return MinionRegistry.SHEEP;
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
                new MinionIngredient(ItemTypeLinker.MUTTON, 8),
                new MinionIngredient(ItemTypeLinker.MUTTON, 16),
                new MinionIngredient(ItemTypeLinker.MUTTON, 32),
                new MinionIngredient(ItemTypeLinker.MUTTON, 64),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_MUTTON, 1),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_MUTTON, 3),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_MUTTON, 8),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_MUTTON, 16),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_MUTTON, 32),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_MUTTON, 64),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_COOKED_MUTTON, 1)
        );
    }
}
