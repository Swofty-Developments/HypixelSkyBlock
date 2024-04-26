package net.swofty.types.generic.utility;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.Sellable;

public record ItemPriceCalculator(SkyBlockItem item) {
    private static final Double BASE_PRICE = 1.0D; // Base price of all items

    public Double calculateCleanPrice() {
        double toReturn = BASE_PRICE;

        if (item.getGenericInstance() != null) {
            Object instance = item.getGenericInstance();

            if (instance instanceof Sellable sellable)
                toReturn += sellable.getSellValue();
        }

        return toReturn;
    }

    public Double calculateTotalPrice() {
        Double toStart = calculateCleanPrice();

        return toStart;
    }
}
