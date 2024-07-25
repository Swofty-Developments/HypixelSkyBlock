package net.swofty.types.generic.item.items.minion.combat;

import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Minion;
import net.swofty.types.generic.minion.MinionIngredient;
import net.swofty.types.generic.minion.MinionRegistry;

import java.util.List;

public class CreeperMinion implements CustomSkyBlockItem, Minion {
    @Override
    public MinionRegistry getMinionRegistry() {
        return MinionRegistry.CREEPER;
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
                new MinionIngredient(ItemTypeLinker.GUNPOWDER, 10),
                new MinionIngredient(ItemTypeLinker.GUNPOWDER, 20),
                new MinionIngredient(ItemTypeLinker.GUNPOWDER, 40),
                new MinionIngredient(ItemTypeLinker.GUNPOWDER, 64),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_GUNPOWDER, 1),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_GUNPOWDER, 3),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_GUNPOWDER, 8),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_GUNPOWDER, 16),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_GUNPOWDER, 32),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_GUNPOWDER, 64),
                new MinionIngredient(ItemTypeLinker.ENCHANTED_FIREWORK_ROCKET, 2)
        );
    }
}
