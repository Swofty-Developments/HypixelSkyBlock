package net.swofty.types.generic.item.items.minion.farming;

import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Minion;
import net.swofty.types.generic.minion.MinionIngredient;
import net.swofty.types.generic.minion.MinionRegistry;

import java.util.List;

public class CowMinion implements CustomSkyBlockItem, Minion {
    @Override
    public MinionRegistry getMinionRegistry() {
        return MinionRegistry.COW;
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
                new MinionIngredient(ItemTypeLinker.RAW_BEEF, 8),
                new MinionIngredient(ItemTypeLinker.RAW_BEEF, 16),
                new MinionIngredient(ItemTypeLinker.RAW_BEEF, 32),
                new MinionIngredient(ItemTypeLinker.RAW_BEEF, 64),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_RAW_BEEF, 1),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_RAW_BEEF, 3),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_RAW_BEEF, 8),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_RAW_BEEF, 16),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_RAW_BEEF, 32),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_RAW_BEEF, 64),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_LEATHER, 32)
        );
    }
}
