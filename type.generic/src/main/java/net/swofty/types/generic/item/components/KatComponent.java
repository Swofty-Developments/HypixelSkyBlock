package net.swofty.types.generic.item.components;

import lombok.Getter;
import net.swofty.types.generic.item.SkyBlockItemComponent;

public class KatComponent extends SkyBlockItemComponent {
    @Getter
    private final int reducedDays;

    public KatComponent(int reducedDays) {
        this.reducedDays = reducedDays;
    }
}