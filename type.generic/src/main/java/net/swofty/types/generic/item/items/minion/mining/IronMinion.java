package net.swofty.types.generic.item.items.minion.mining;

import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Minion;
import net.swofty.types.generic.minion.MinionIngredient;
import net.swofty.types.generic.minion.MinionRegistry;

import java.util.List;

public class IronMinion implements CustomSkyBlockItem, Minion {
    @Override
    public MinionRegistry getMinionRegistry() {
        return MinionRegistry.IRON;
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
                new MinionIngredient(ItemTypeLinker.IRON_INGOT, 10),
                new MinionIngredient(ItemTypeLinker.IRON_INGOT, 20),
                new MinionIngredient(ItemTypeLinker.IRON_INGOT, 40),
                new MinionIngredient(ItemTypeLinker.IRON_INGOT, 64),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_IRON_INGOT, 1),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_IRON_INGOT, 3),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_IRON_INGOT, 8),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_IRON_INGOT, 16),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_IRON_INGOT, 32),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_IRON_INGOT, 64),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_IRON_BLOCK, 1)
        );
    }
}
