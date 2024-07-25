package net.swofty.types.generic.item.items.minion.farming;

import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Minion;
import net.swofty.types.generic.minion.MinionIngredient;
import net.swofty.types.generic.minion.MinionRegistry;

import java.util.List;

public class MelonMinion implements CustomSkyBlockItem, Minion {
    @Override
    public MinionRegistry getMinionRegistry() {
        return MinionRegistry.MELON;
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
                new MinionIngredient(ItemTypeLinker.MELON_SLICE, 32),
                new MinionIngredient(ItemTypeLinker.MELON_SLICE, 64),
                new MinionIngredient(ItemTypeLinker.MELON_BLOCK, 16),
                new MinionIngredient(ItemTypeLinker.MELON_BLOCK, 32),
                new MinionIngredient(ItemTypeLinker.MELON_BLOCK, 64),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_MELON, 8),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_MELON, 16),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_MELON, 32),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_MELON, 64),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_MELON_BLOCK, 1),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_MELON_BLOCK, 2)
        );
    }
}
