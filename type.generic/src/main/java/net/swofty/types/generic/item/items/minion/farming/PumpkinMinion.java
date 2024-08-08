package net.swofty.types.generic.item.items.minion.farming;

import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Minion;
import net.swofty.types.generic.minion.MinionIngredient;
import net.swofty.types.generic.minion.MinionRegistry;

import java.util.List;

public class PumpkinMinion implements CustomSkyBlockItem, Minion {
    @Override
    public MinionRegistry getMinionRegistry() {
        return MinionRegistry.PUMPKIN;
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
                new MinionIngredient(ItemTypeLinker.PUMPKIN, 10),
                new MinionIngredient(ItemTypeLinker.PUMPKIN, 20),
                new MinionIngredient(ItemTypeLinker.PUMPKIN, 40),
                new MinionIngredient(ItemTypeLinker.PUMPKIN, 64),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_PUMPKIN, 1),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_PUMPKIN, 2),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_PUMPKIN, 4),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_PUMPKIN, 8),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_PUMPKIN, 16),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_PUMPKIN, 32),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_PUMPKIN, 64)
        );
    }
}
