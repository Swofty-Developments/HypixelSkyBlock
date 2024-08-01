package net.swofty.types.generic.item.items.minion.fishing;

import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Minion;
import net.swofty.types.generic.minion.MinionIngredient;
import net.swofty.types.generic.minion.MinionRegistry;

import java.util.List;

public class ClayMinion implements CustomSkyBlockItem, Minion {
    @Override
    public MinionRegistry getMinionRegistry() {
        return MinionRegistry.CLAY;
    }

    @Override
    public ItemTypeLinker getFirstBaseItem() {
        return ItemTypeLinker.WOODEN_SHOVEL;
    }

    @Override
    public boolean isByDefaultCraftable() {
        return false;
    }

    @Override
    public List<MinionIngredient> getMinionCraftingIngredients() {
        return List.of(
                new MinionIngredient(ItemTypeLinker.CLAY_BALL, 10),
                new MinionIngredient(ItemTypeLinker.CLAY_BALL, 20),
                new MinionIngredient(ItemTypeLinker.CLAY_BALL, 40),
                new MinionIngredient(ItemTypeLinker.CLAY_BALL, 64),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_CLAY, 1),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_CLAY, 2),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_CLAY, 4),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_CLAY, 8),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_CLAY, 16),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_CLAY, 32),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_CLAY, 64)
        );
    }
}
