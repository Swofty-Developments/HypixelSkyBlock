package net.swofty.types.generic.item.items.minion.mining;

import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Minion;
import net.swofty.types.generic.minion.MinionIngredient;
import net.swofty.types.generic.minion.MinionRegistry;

import java.util.List;

public class GlowstoneMinion implements CustomSkyBlockItem, Minion {
    @Override
    public MinionRegistry getMinionRegistry() {
        return MinionRegistry.GLOWSTONE;
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
                new MinionIngredient(ItemTypeLinker.GLOWSTONE_DUST, 16),
                new MinionIngredient(ItemTypeLinker.GLOWSTONE_DUST, 32),
                new MinionIngredient(ItemTypeLinker.GLOWSTONE_DUST, 64),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_GLOWSTONE_DUST, 1),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_GLOWSTONE_DUST, 3),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_GLOWSTONE_DUST, 8),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_GLOWSTONE_DUST, 16),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_GLOWSTONE_DUST, 32),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_GLOWSTONE_DUST, 64),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_GLOWSTONE, 1),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_GLOWSTONE, 2)
        );
    }
}
