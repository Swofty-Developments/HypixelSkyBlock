package net.swofty.types.generic.item.items.minion.foraging;

import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Minion;
import net.swofty.types.generic.minion.MinionIngredient;
import net.swofty.types.generic.minion.MinionRegistry;

import java.util.List;

public class AcaciaMinion implements CustomSkyBlockItem, Minion {
    @Override
    public MinionRegistry getMinionRegistry() {
        return MinionRegistry.ACACIA;
    }

    @Override
    public ItemTypeLinker getFirstBaseItem() {
        return ItemTypeLinker.WOODEN_AXE;
    }

    @Override
    public boolean isByDefaultCraftable() {
        return false;
    }

    @Override
    public List<MinionIngredient> getMinionCraftingIngredients() {
        return List.of(
                new MinionIngredient(ItemTypeLinker.ACACIA_LOG, 10),
                new MinionIngredient(ItemTypeLinker.ACACIA_LOG, 20),
                new MinionIngredient(ItemTypeLinker.ACACIA_LOG, 40),
                new MinionIngredient(ItemTypeLinker.ACACIA_LOG, 64),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_ACACIA_LOG, 1),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_ACACIA_LOG, 2),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_ACACIA_LOG, 4),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_ACACIA_LOG, 8),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_ACACIA_LOG, 16),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_ACACIA_LOG, 32),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_ACACIA_LOG, 64)
        );
    }
}
