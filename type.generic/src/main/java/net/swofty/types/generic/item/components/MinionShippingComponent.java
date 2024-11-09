package net.swofty.types.generic.item.components;

import lombok.Getter;
import net.swofty.types.generic.item.SkyBlockItemComponent;

public class MinionShippingComponent extends SkyBlockItemComponent {
    @Getter
    private final double percentageOfOriginalPrice;

    public MinionShippingComponent(double percentageOfOriginalPrice) {
        this.percentageOfOriginalPrice = percentageOfOriginalPrice;
        addInheritedComponent(new TrackedUniqueComponent());
    }
}