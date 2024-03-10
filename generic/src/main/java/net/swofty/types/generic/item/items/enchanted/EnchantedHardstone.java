package net.swofty.types.generic.item.items.enchanted;


import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.Enchanted;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.item.impl.SkyBlockRecipe;
import net.swofty.types.generic.item.impl.Craftable;
import net.swofty.types.generic.item.impl.recipes.ShapelessRecipe;

public class EnchantedHardstone implements Enchanted, Sellable, Craftable {
    @Override
    public double getSellValue() {
        return 576;
    }

    @Override
    public SkyBlockRecipe<?> getRecipe() {
        return new ShapelessRecipe(SkyBlockRecipe.RecipeType.MINING,
                new SkyBlockItem(ItemType.ENCHANTED_HARD_STONE), 1)
                .add(ItemType.HARD_STONE, 64)
                .add(ItemType.HARD_STONE, 64)
                .add(ItemType.HARD_STONE, 64)
                .add(ItemType.HARD_STONE, 64)
                .add(ItemType.HARD_STONE, 64)
                .add(ItemType.HARD_STONE, 64)
                .add(ItemType.HARD_STONE, 64)
                .add(ItemType.HARD_STONE, 64)
                .add(ItemType.HARD_STONE, 64);
    }
}