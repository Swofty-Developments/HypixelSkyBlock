package net.swofty.types.generic.item.items.minion.combat;

import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Minion;
import net.swofty.types.generic.minion.MinionIngredient;
import net.swofty.types.generic.minion.MinionRegistry;

import java.util.List;

public class SpiderMinion implements CustomSkyBlockItem, Minion {
    @Override
    public MinionRegistry getMinionRegistry() {
        return MinionRegistry.SPIDER;
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
                new MinionIngredient(ItemTypeLinker.STRING, 10),
                new MinionIngredient(ItemTypeLinker.STRING, 20),
                new MinionIngredient(ItemTypeLinker.STRING, 40),
                new MinionIngredient(ItemTypeLinker.STRING, 64),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_STRING, 1),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_STRING, 2),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_STRING, 4),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_STRING, 8),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_STRING, 16),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_STRING, 32),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_STRING, 64)
        );
    }
}
