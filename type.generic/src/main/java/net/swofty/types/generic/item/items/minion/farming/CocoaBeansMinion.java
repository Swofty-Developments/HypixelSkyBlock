package net.swofty.types.generic.item.items.minion.farming;

import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Minion;
import net.swofty.types.generic.minion.MinionIngredient;
import net.swofty.types.generic.minion.MinionRegistry;

import java.util.List;

public class CocoaBeansMinion implements CustomSkyBlockItem, Minion {
    @Override
    public MinionRegistry getMinionRegistry() {
        return MinionRegistry.COCOA_BEANS;
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
                new MinionIngredient(ItemTypeLinker.COCOA_BEANS, 10),
                new MinionIngredient(ItemTypeLinker.COCOA_BEANS, 20),
                new MinionIngredient(ItemTypeLinker.COCOA_BEANS, 40),
                new MinionIngredient(ItemTypeLinker.COCOA_BEANS, 64),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_COCOA_BEANS, 1),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_COCOA_BEANS, 3),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_COCOA_BEANS, 8),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_COCOA_BEANS, 16),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_COCOA_BEANS, 32),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_COCOA_BEANS, 64),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_COOKIE, 1)
        );
    }
}
