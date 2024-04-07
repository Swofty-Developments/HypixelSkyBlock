package net.swofty.types.generic.item.items.enchanted;


import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.impl.Enchanted;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.item.impl.SkyBlockRecipe;
import net.swofty.types.generic.item.impl.DefaultCraftable;

public class EnchantedLapisLazuliBlock implements Enchanted, Sellable {
    @Override
    public double getSellValue() {
        return 25600;
    }

}