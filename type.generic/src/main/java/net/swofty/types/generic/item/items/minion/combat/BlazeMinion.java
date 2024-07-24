package net.swofty.types.generic.item.items.minion.combat;

import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Minion;
import net.swofty.types.generic.minion.MinionIngredient;
import net.swofty.types.generic.minion.MinionRegistry;

import java.util.List;

public class BlazeMinion implements CustomSkyBlockItem, Minion {
    @Override
    public MinionRegistry getMinionRegistry() {
        return MinionRegistry.BLAZE;
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
                new MinionIngredient(ItemTypeLinker.BLAZE_ROD, 10),
                new MinionIngredient(ItemTypeLinker.BLAZE_ROD, 20),
                new MinionIngredient(ItemTypeLinker.BLAZE_ROD, 40),
                new MinionIngredient(ItemTypeLinker.BLAZE_ROD, 64),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_BLAZE_POWDER, 1),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_BLAZE_POWDER, 3),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_BLAZE_POWDER, 8),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_BLAZE_POWDER, 16),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_BLAZE_POWDER, 32),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_BLAZE_POWDER, 64),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_BLAZE_ROD, 1)
        );
    }
}
