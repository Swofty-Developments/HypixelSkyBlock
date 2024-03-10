package net.swofty.types.generic.item.items.enchanted;

import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.Craftable;
import net.swofty.types.generic.item.impl.Enchanted;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.item.impl.SkyBlockRecipe;
import net.swofty.types.generic.item.impl.recipes.ShapelessRecipe;

public class EnchantedInkSac implements Enchanted, Sellable, Craftable {
    @Override
    public double getSellValue() {
        return 160;
    }

    @Override
    public SkyBlockRecipe<?> getRecipe() {
        return new ShapelessRecipe(SkyBlockRecipe.RecipeType.FISHING,
                new SkyBlockItem(ItemType.ENCHANTED_INK_SAC), 1)
                .add(ItemType.INK_SAC, 16)
                .add(ItemType.INK_SAC, 16)
                .add(ItemType.INK_SAC, 16)
                .add(ItemType.INK_SAC, 16)
                .add(ItemType.INK_SAC, 16);
    }
}
