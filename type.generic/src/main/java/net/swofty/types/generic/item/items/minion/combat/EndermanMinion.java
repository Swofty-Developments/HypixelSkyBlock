package net.swofty.types.generic.item.items.minion.combat;

import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Minion;
import net.swofty.types.generic.minion.MinionIngredient;
import net.swofty.types.generic.minion.MinionRegistry;

import java.util.List;

public class EndermanMinion implements CustomSkyBlockItem, Minion {
    @Override
    public MinionRegistry getMinionRegistry() {
        return MinionRegistry.ENDERMAN;
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
                new MinionIngredient(ItemTypeLinker.ENDER_PEARL, 8),
                new MinionIngredient(ItemTypeLinker.ENDER_PEARL, 16),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_ENDER_PEARL, 1),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_ENDER_PEARL, 3),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_ENDER_PEARL, 6),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_ENDER_PEARL, 12),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_EYE_OF_ENDER, 1),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_EYE_OF_ENDER, 3),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_EYE_OF_ENDER, 6),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_EYE_OF_ENDER, 12),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_EYE_OF_ENDER, 24)
        );
    }
}
