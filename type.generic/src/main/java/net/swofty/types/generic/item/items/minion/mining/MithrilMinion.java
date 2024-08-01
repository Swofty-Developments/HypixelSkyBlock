package net.swofty.types.generic.item.items.minion.mining;

import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Minion;
import net.swofty.types.generic.minion.MinionIngredient;
import net.swofty.types.generic.minion.MinionRegistry;

import java.util.List;

public class MithrilMinion implements CustomSkyBlockItem, Minion {
    @Override
    public MinionRegistry getMinionRegistry() {
        return MinionRegistry.MITHRIL;
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
                new MinionIngredient(ItemTypeLinker.MITHRIL, 10),
                new MinionIngredient(ItemTypeLinker.MITHRIL, 20),
                new MinionIngredient(ItemTypeLinker.MITHRIL, 40),
                new MinionIngredient(ItemTypeLinker.MITHRIL, 64),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_MITHRIL, 1),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_MITHRIL, 3),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_MITHRIL, 8),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_MITHRIL, 16),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_MITHRIL, 32),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_MITHRIL, 64),
                new MinionIngredient(ItemTypeLinker.REFINED_MITHRIL, 1)
        );
    }
}
