package net.swofty.types.generic.item.items.minion.mining;

import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Minion;
import net.swofty.types.generic.minion.MinionIngredient;
import net.swofty.types.generic.minion.MinionRegistry;

import java.util.List;

public class EndStoneMinion implements CustomSkyBlockItem, Minion {
    @Override
    public MinionRegistry getMinionRegistry() {
        return MinionRegistry.ENDSTONE;
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
                new MinionIngredient(ItemTypeLinker.END_STONE, 10),
                new MinionIngredient(ItemTypeLinker.END_STONE, 20),
                new MinionIngredient(ItemTypeLinker.END_STONE, 40),
                new MinionIngredient(ItemTypeLinker.END_STONE, 64),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_ENDSTONE, 1),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_ENDSTONE, 2),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_ENDSTONE, 4),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_ENDSTONE, 8),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_ENDSTONE, 16),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_ENDSTONE, 32),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_ENDSTONE, 64)
        );
    }
}
