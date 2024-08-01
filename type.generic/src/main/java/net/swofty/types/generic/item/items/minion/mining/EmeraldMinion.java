package net.swofty.types.generic.item.items.minion.mining;

import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Minion;
import net.swofty.types.generic.minion.MinionIngredient;
import net.swofty.types.generic.minion.MinionRegistry;

import java.util.List;

public class EmeraldMinion implements CustomSkyBlockItem, Minion {
    @Override
    public MinionRegistry getMinionRegistry() {
        return MinionRegistry.EMERALD;
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
                new MinionIngredient(ItemTypeLinker.EMERALD, 10),
                new MinionIngredient(ItemTypeLinker.EMERALD, 20),
                new MinionIngredient(ItemTypeLinker.EMERALD, 40),
                new MinionIngredient(ItemTypeLinker.EMERALD, 64),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_EMERALD, 1),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_EMERALD, 3),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_EMERALD, 8),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_EMERALD, 16),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_EMERALD, 32),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_EMERALD, 64),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_EMERALD_BLOCK, 1)
        );
    }
}
