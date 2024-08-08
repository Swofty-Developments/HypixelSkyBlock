package net.swofty.types.generic.item.items.minion.mining;

import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Minion;
import net.swofty.types.generic.minion.MinionIngredient;
import net.swofty.types.generic.minion.MinionRegistry;

import java.util.List;

public class DiamondMinion implements CustomSkyBlockItem, Minion {
    @Override
    public MinionRegistry getMinionRegistry() {
        return MinionRegistry.DIAMOND;
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
                new MinionIngredient(ItemTypeLinker.DIAMOND, 10),
                new MinionIngredient(ItemTypeLinker.DIAMOND, 20),
                new MinionIngredient(ItemTypeLinker.DIAMOND, 40),
                new MinionIngredient(ItemTypeLinker.DIAMOND, 64),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_DIAMOND, 1),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_DIAMOND, 3),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_DIAMOND, 8),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_DIAMOND, 16),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_DIAMOND, 32),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_DIAMOND, 64),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_DIAMOND_BLOCK, 1)
        );
    }
}
