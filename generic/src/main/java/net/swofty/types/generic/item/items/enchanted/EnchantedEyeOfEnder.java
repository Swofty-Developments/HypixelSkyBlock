package net.swofty.types.generic.item.items.enchanted;

import net.swofty.types.generic.item.impl.Enchanted;
import net.swofty.types.generic.item.impl.Sellable;

public class EnchantedEyeOfEnder implements Enchanted, Sellable {
    @Override
    public double getSellValue() {
        return 3520;
    }
}