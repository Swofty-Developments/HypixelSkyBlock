package net.swofty.types.generic.item.items.minion.farming;

import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Minion;
import net.swofty.types.generic.minion.MinionIngredient;
import net.swofty.types.generic.minion.MinionRegistry;

import java.util.List;

public class PotatoMinion implements CustomSkyBlockItem, Minion {
    @Override
    public MinionRegistry getMinionRegistry() {
        return MinionRegistry.POTATO;
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
                new MinionIngredient(ItemTypeLinker.POTATO, 16),
                new MinionIngredient(ItemTypeLinker.POTATO, 32),
                new MinionIngredient(ItemTypeLinker.POTATO, 64),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_POTATO, 1),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_POTATO, 3),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_POTATO, 8),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_POTATO, 16),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_POTATO, 32),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_POTATO, 64),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_BAKED_POTATO, 1),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_BAKED_POTATO, 2)
        );
    }
}
