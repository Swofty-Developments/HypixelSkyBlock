package net.swofty.types.generic.item.items.minion.mining;

import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Minion;
import net.swofty.types.generic.minion.MinionIngredient;
import net.swofty.types.generic.minion.MinionRegistry;

import java.util.List;

public class CobblestoneMinion implements CustomSkyBlockItem, Minion {
    @Override
    public MinionRegistry getMinionRegistry() {
        return MinionRegistry.COBBLESTONE;
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
                new MinionIngredient(ItemTypeLinker.COBBLESTONE, 10),
                new MinionIngredient(ItemTypeLinker.COBBLESTONE, 20),
                new MinionIngredient(ItemTypeLinker.COBBLESTONE, 40),
                new MinionIngredient(ItemTypeLinker.COBBLESTONE, 64),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_COBBLESTONE, 1),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_COBBLESTONE, 2),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_COBBLESTONE, 4),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_COBBLESTONE, 8),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_COBBLESTONE, 16),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_COBBLESTONE, 32),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_COBBLESTONE, 64)
        );
    }
}
