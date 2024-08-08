package net.swofty.types.generic.item.items.minion.farming;

import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Minion;
import net.swofty.types.generic.minion.MinionIngredient;
import net.swofty.types.generic.minion.MinionRegistry;

import java.util.List;

public class SugarCaneMinion implements CustomSkyBlockItem, Minion {
    @Override
    public MinionRegistry getMinionRegistry() {
        return MinionRegistry.SUGAR_CANE;
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
                new MinionIngredient(ItemTypeLinker.SUGAR_CANE, 16),
                new MinionIngredient(ItemTypeLinker.SUGAR_CANE, 32),
                new MinionIngredient(ItemTypeLinker.SUGAR_CANE, 64),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_SUGAR, 1),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_SUGAR, 3),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_SUGAR, 8),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_SUGAR, 16),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_SUGAR, 32),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_SUGAR, 64),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_SUGAR_CANE, 1),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_SUGAR_CANE, 2)
        );
    }
}
