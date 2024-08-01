package net.swofty.types.generic.item.items.minion.combat;

import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Minion;
import net.swofty.types.generic.minion.MinionIngredient;
import net.swofty.types.generic.minion.MinionRegistry;

import java.util.List;

public class GhastMinion implements CustomSkyBlockItem, Minion {
    @Override
    public MinionRegistry getMinionRegistry() {
        return MinionRegistry.GHAST;
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
                new MinionIngredient(ItemTypeLinker.GHAST_TEAR, 8),
                new MinionIngredient(ItemTypeLinker.GHAST_TEAR, 16),
                new MinionIngredient(ItemTypeLinker.GHAST_TEAR, 32),
                new MinionIngredient(ItemTypeLinker.GHAST_TEAR, 64),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_GHAST_TEAR, 32),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_GHAST_TEAR, 64),
                new MinionIngredient(ItemTypeLinker.SILVER_FANG, 4),
                new MinionIngredient(ItemTypeLinker.SILVER_FANG, 8),
                new MinionIngredient(ItemTypeLinker.SILVER_FANG, 16),
                new MinionIngredient(ItemTypeLinker.SILVER_FANG, 32),
                new MinionIngredient(ItemTypeLinker.SILVER_FANG, 64)
        );
    }
}
