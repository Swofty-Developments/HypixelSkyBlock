package net.swofty.types.generic.item.items.minion.farming;

import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Minion;
import net.swofty.types.generic.minion.MinionIngredient;
import net.swofty.types.generic.minion.MinionRegistry;

import java.util.List;

public class RabbitMinion implements CustomSkyBlockItem, Minion {
    @Override
    public MinionRegistry getMinionRegistry() {
        return MinionRegistry.RABBIT;
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
                new MinionIngredient(ItemTypeLinker.RABBIT, 8),
                new MinionIngredient(ItemTypeLinker.RABBIT, 16),
                new MinionIngredient(ItemTypeLinker.RABBIT, 32),
                new MinionIngredient(ItemTypeLinker.RABBIT, 64),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_RABBIT_FOOT, 4),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_RABBIT_FOOT, 8),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_RABBIT_FOOT, 16),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_RABBIT_FOOT, 32),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_RABBIT_FOOT, 64),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_RABBIT_HIDE, 32),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_RABBIT_HIDE, 64)
        );
    }
}
