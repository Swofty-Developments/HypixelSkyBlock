package net.swofty.types.generic.item.items.enchanted;


import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.Craftable;
import net.swofty.types.generic.item.impl.Enchanted;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.item.impl.SkyBlockRecipe;
import net.swofty.types.generic.item.impl.recipes.ShapelessRecipe;

public class EnchantedGlisteringMelon implements Enchanted, Sellable, Craftable {
    @Override
    public double getSellValue() {
        return 1000;
    }

    @Override
    public SkyBlockRecipe<?> getRecipe() {
        return new ShapelessRecipe(SkyBlockRecipe.RecipeType.FARMING,
                new SkyBlockItem(ItemType.ENCHANTED_GLISTERING_MELON), 1)
                .add(ItemType.GLISTERING_MELON, 32)
                .add(ItemType.GLISTERING_MELON, 32)
                .add(ItemType.GLISTERING_MELON, 32)
                .add(ItemType.GLISTERING_MELON, 32)
                .add(ItemType.GLISTERING_MELON, 32)
                .add(ItemType.GLISTERING_MELON, 32)
                .add(ItemType.GLISTERING_MELON, 32)
                .add(ItemType.GLISTERING_MELON, 32)
                .add(ItemType.GLISTERING_MELON, 32);
    }
}