package net.swofty.type.skyblockgeneric.item.components;

import lombok.Getter;
import net.swofty.type.generic.item.SkyBlockItemComponent;

public class SellableComponent extends SkyBlockItemComponent {
    @Getter
    private final double sellValue;

    public SellableComponent(double sellValue) {
        this.sellValue = sellValue;
    }
}