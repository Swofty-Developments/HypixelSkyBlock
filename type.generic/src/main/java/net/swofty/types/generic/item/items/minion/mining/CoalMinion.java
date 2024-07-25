package net.swofty.types.generic.item.items.minion.mining;

import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Minion;
import net.swofty.types.generic.minion.MinionIngredient;
import net.swofty.types.generic.minion.MinionRegistry;

import java.util.List;

public class CoalMinion implements CustomSkyBlockItem, Minion {
    @Override
    public MinionRegistry getMinionRegistry() {
        return MinionRegistry.COAL;
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
                new MinionIngredient(ItemTypeLinker.COAL, 10),
                new MinionIngredient(ItemTypeLinker.COAL, 20),
                new MinionIngredient(ItemTypeLinker.COAL, 40),
                new MinionIngredient(ItemTypeLinker.COAL, 64),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_COAL, 1),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_COAL, 3),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_COAL, 8),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_COAL, 16),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_COAL, 32),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_COAL, 64),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_COAL_BLOCK, 1)
        );
    }
}
