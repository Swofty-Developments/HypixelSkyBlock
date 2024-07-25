package net.swofty.types.generic.item.items.minion.combat;

import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Minion;
import net.swofty.types.generic.minion.MinionIngredient;
import net.swofty.types.generic.minion.MinionRegistry;

import java.util.List;

public class SlimeMinion implements CustomSkyBlockItem, Minion {
    @Override
    public MinionRegistry getMinionRegistry() {
        return MinionRegistry.SLIME;
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
                new MinionIngredient(ItemTypeLinker.SLIME_BALL, 10),
                new MinionIngredient(ItemTypeLinker.SLIME_BALL, 20),
                new MinionIngredient(ItemTypeLinker.SLIME_BALL, 40),
                new MinionIngredient(ItemTypeLinker.SLIME_BALL, 64),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_SLIME_BALL, 1),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_SLIME_BALL, 3),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_SLIME_BALL, 8),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_SLIME_BALL, 16),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_SLIME_BALL, 32),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_SLIME_BALL, 64),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_SLIME_BLOCK, 1)
        );
    }
}
