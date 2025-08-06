package net.swofty.types.generic.utility;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.components.SellableComponent;

public record ItemPriceCalculator(SkyBlockItem item) {
    private static final Double BASE_PRICE = 1.0D; // Base price of all items

    public Double calculateCleanPrice() {
        double toReturn = BASE_PRICE;

        if (item.hasComponent(SellableComponent.class))
            toReturn += item.getComponent(SellableComponent.class).getSellValue();

        return toReturn;
    }

    public Double calculateTotalPrice() {

        return calculateCleanPrice();
    }
}
