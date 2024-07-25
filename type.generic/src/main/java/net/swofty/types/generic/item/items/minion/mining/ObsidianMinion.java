package net.swofty.types.generic.item.items.minion.mining;

import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Minion;
import net.swofty.types.generic.minion.MinionIngredient;
import net.swofty.types.generic.minion.MinionRegistry;

import java.util.List;

public class ObsidianMinion implements CustomSkyBlockItem, Minion {
    @Override
    public MinionRegistry getMinionRegistry() {
        return MinionRegistry.OBSIDIAN;
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
                new MinionIngredient(ItemTypeLinker.OBSIDIAN, 10),
                new MinionIngredient(ItemTypeLinker.OBSIDIAN, 20),
                new MinionIngredient(ItemTypeLinker.OBSIDIAN, 40),
                new MinionIngredient(ItemTypeLinker.OBSIDIAN, 64),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_OBSIDIAN, 1),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_OBSIDIAN, 2),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_OBSIDIAN, 4),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_OBSIDIAN, 8),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_OBSIDIAN, 16),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_OBSIDIAN, 32),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_OBSIDIAN, 64)
        );
    }
}
