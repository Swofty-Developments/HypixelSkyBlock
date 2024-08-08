package net.swofty.types.generic.item.items.minion.mining;

import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Minion;
import net.swofty.types.generic.minion.MinionIngredient;
import net.swofty.types.generic.minion.MinionRegistry;

import java.util.List;

public class HardStoneMinion implements CustomSkyBlockItem, Minion {
    @Override
    public MinionRegistry getMinionRegistry() {
        return MinionRegistry.HARD_STONE;
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
                new MinionIngredient(ItemTypeLinker.HARD_STONE, 32),
                new MinionIngredient(ItemTypeLinker.HARD_STONE, 64),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_HARD_STONE, 1),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_HARD_STONE, 2),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_HARD_STONE, 4),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_HARD_STONE, 8),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_HARD_STONE, 16),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_HARD_STONE, 32),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_HARD_STONE, 64),
                new MinionIngredient(ItemTypeLinker.CONCENTRATED_STONE, 1),
                new MinionIngredient(ItemTypeLinker.CONCENTRATED_STONE, 2)
        );
    }
}
