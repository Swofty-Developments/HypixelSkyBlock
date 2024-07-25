package net.swofty.types.generic.item.items.minion.fishing;

import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Minion;
import net.swofty.types.generic.minion.MinionIngredient;
import net.swofty.types.generic.minion.MinionRegistry;

import java.util.List;

public class FishingMinion implements CustomSkyBlockItem, Minion {
    @Override
    public MinionRegistry getMinionRegistry() {
        return MinionRegistry.FISHING;
    }

    @Override
    public ItemTypeLinker getFirstBaseItem() {
        return ItemTypeLinker.FISHING_ROD;
    }

    @Override
    public boolean isByDefaultCraftable() {
        return false;
    }

    @Override
    public List<MinionIngredient> getMinionCraftingIngredients() {
        return List.of(
                new MinionIngredient(ItemTypeLinker.COD, 8),
                new MinionIngredient(ItemTypeLinker.COD, 16),
                new MinionIngredient(ItemTypeLinker.COD, 32),
                new MinionIngredient(ItemTypeLinker.COD, 64),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_COD, 1),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_COD, 3),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_COD, 8),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_COD, 16),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_COD, 32),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_COD, 64),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_COOKED_COD, 1)
        );
    }
}
