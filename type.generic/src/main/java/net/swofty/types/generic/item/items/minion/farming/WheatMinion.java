package net.swofty.types.generic.item.items.minion.farming;

import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Minion;
import net.swofty.types.generic.minion.MinionIngredient;
import net.swofty.types.generic.minion.MinionRegistry;

import java.util.List;

public class WheatMinion implements CustomSkyBlockItem, Minion {
    @Override
    public MinionRegistry getMinionRegistry() {
        return MinionRegistry.WHEAT;
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
                new MinionIngredient(ItemTypeLinker.WHEAT, 10),
                new MinionIngredient(ItemTypeLinker.WHEAT, 20),
                new MinionIngredient(ItemTypeLinker.WHEAT, 40),
                new MinionIngredient(ItemTypeLinker.WHEAT, 64),
                new MinionIngredient(ItemTypeLinker.HAY_BALE, 12),
                new MinionIngredient(ItemTypeLinker.HAY_BALE, 24),
                new MinionIngredient(ItemTypeLinker.HAY_BALE, 48),
                new MinionIngredient(ItemTypeLinker.HAY_BALE, 64),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_HAY_BALE, 1),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_HAY_BALE, 2),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_HAY_BALE, 4)
        );
    }
}
