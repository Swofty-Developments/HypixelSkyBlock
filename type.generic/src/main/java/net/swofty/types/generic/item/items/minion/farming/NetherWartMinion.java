package net.swofty.types.generic.item.items.minion.farming;

import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Minion;
import net.swofty.types.generic.minion.MinionIngredient;
import net.swofty.types.generic.minion.MinionRegistry;

import java.util.List;

public class NetherWartMinion implements CustomSkyBlockItem, Minion {
    @Override
    public MinionRegistry getMinionRegistry() {
        return MinionRegistry.NETHER_WART;
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
                new MinionIngredient(ItemTypeLinker.NETHER_WART, 10),
                new MinionIngredient(ItemTypeLinker.NETHER_WART, 20),
                new MinionIngredient(ItemTypeLinker.NETHER_WART, 40),
                new MinionIngredient(ItemTypeLinker.NETHER_WART, 64),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_NETHER_WART, 1),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_NETHER_WART, 2),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_NETHER_WART, 4),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_NETHER_WART, 8),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_NETHER_WART, 16),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_NETHER_WART, 32),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_NETHER_WART, 64)
        );
    }
}
