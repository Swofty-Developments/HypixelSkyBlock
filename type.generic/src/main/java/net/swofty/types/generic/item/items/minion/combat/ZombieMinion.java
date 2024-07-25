package net.swofty.types.generic.item.items.minion.combat;

import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Minion;
import net.swofty.types.generic.minion.MinionIngredient;
import net.swofty.types.generic.minion.MinionRegistry;

import java.util.List;

public class ZombieMinion implements CustomSkyBlockItem, Minion {
    @Override
    public MinionRegistry getMinionRegistry() {
        return MinionRegistry.ZOMBIE;
    }

    @Override
    public ItemTypeLinker getFirstBaseItem() {
        return ItemTypeLinker.WOODEN_SWORD;
    }

    @Override
    public boolean isByDefaultCraftable() {
        return false;
    }

    @Override
    public List<MinionIngredient> getMinionCraftingIngredients() {
        return List.of(
                new MinionIngredient(ItemTypeLinker.ROTTEN_FLESH, 10),
                new MinionIngredient(ItemTypeLinker.ROTTEN_FLESH, 20),
                new MinionIngredient(ItemTypeLinker.ROTTEN_FLESH, 40),
                new MinionIngredient(ItemTypeLinker.ROTTEN_FLESH, 64),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_ROTTEN_FLESH, 1),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_ROTTEN_FLESH, 2),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_ROTTEN_FLESH, 4),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_ROTTEN_FLESH, 8),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_ROTTEN_FLESH, 16),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_ROTTEN_FLESH, 32),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_ROTTEN_FLESH, 64)
        );
    }
}
