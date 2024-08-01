package net.swofty.types.generic.item.items.minion.farming;

import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Minion;
import net.swofty.types.generic.minion.MinionIngredient;
import net.swofty.types.generic.minion.MinionRegistry;

import java.util.List;

public class CarrotMinion implements CustomSkyBlockItem, Minion {
    @Override
    public MinionRegistry getMinionRegistry() {
        return MinionRegistry.CARROT;
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
                new MinionIngredient(ItemTypeLinker.CARROT, 16),
                new MinionIngredient(ItemTypeLinker.CARROT, 32),
                new MinionIngredient(ItemTypeLinker.CARROT, 64),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_CARROT, 1),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_CARROT, 3),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_CARROT, 8),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_CARROT, 16),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_CARROT, 32),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_CARROT, 64),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_GOLDEN_CARROT, 1),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_GOLDEN_CARROT, 2)
        );
    }
}
