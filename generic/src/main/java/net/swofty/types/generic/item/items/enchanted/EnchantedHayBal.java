package net.swofty.types.generic.item.items.enchanted;


import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.DefaultCraftable;
import net.swofty.types.generic.item.impl.Enchanted;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.item.impl.SkyBlockRecipe;
import net.swofty.types.generic.item.impl.recipes.ShapelessRecipe;

public class EnchantedHayBal implements Enchanted, Sellable, DefaultCraftable {
    @Override
    public double getSellValue() {
        return 7776;
    }

    @Override
    public SkyBlockRecipe<?> getRecipe() {
        return new ShapelessRecipe(SkyBlockRecipe.RecipeType.FARMING,
                new SkyBlockItem(ItemType.ENCHANTED_HAY_BAL), 1)
                .add(ItemType.HAY_BAL, 16)
                .add(ItemType.HAY_BAL, 16)
                .add(ItemType.HAY_BAL, 16)
                .add(ItemType.HAY_BAL, 16)
                .add(ItemType.HAY_BAL, 16)
                .add(ItemType.HAY_BAL, 16)
                .add(ItemType.HAY_BAL, 16)
                .add(ItemType.HAY_BAL, 16)
                .add(ItemType.HAY_BAL, 16);
    }
}