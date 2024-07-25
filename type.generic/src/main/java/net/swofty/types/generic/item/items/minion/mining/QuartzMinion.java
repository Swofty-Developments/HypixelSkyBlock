package net.swofty.types.generic.item.items.minion.mining;

import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Minion;
import net.swofty.types.generic.minion.MinionIngredient;
import net.swofty.types.generic.minion.MinionRegistry;

import java.util.List;

public class QuartzMinion implements CustomSkyBlockItem, Minion {
    @Override
    public MinionRegistry getMinionRegistry() {
        return MinionRegistry.QUARTZ;
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
                new MinionIngredient(ItemTypeLinker.QUARTZ, 10),
                new MinionIngredient(ItemTypeLinker.QUARTZ, 20),
                new MinionIngredient(ItemTypeLinker.QUARTZ, 40),
                new MinionIngredient(ItemTypeLinker.QUARTZ, 64),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_QUARTZ, 1),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_QUARTZ, 3),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_QUARTZ, 8),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_QUARTZ, 16),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_QUARTZ, 32),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_QUARTZ, 64),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_QUARTZ_BLOCK, 1)
        );
    }
}
