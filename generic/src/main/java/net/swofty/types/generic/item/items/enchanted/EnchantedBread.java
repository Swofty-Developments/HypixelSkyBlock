package net.swofty.types.generic.item.items.enchanted;


import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.Craftable;
import net.swofty.types.generic.item.impl.Enchanted;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.item.impl.SkyBlockRecipe;
import net.swofty.types.generic.item.impl.recipes.ShapelessRecipe;

public class EnchantedBread implements Enchanted, Sellable, Craftable {

    @Override
    public double getSellValue() {
        return 60;
    }

    @Override
    public SkyBlockRecipe<?> getRecipe() {
        return new ShapelessRecipe(SkyBlockRecipe.RecipeType.FARMING,
                new SkyBlockItem(ItemType.ENCHANTED_BREAD), 1)
                .add(ItemType.WHEAT, 10)
                .add(ItemType.WHEAT, 10)
                .add(ItemType.WHEAT, 10)
                .add(ItemType.WHEAT, 10)
                .add(ItemType.WHEAT, 10)
                .add(ItemType.WHEAT, 10);
    }
}