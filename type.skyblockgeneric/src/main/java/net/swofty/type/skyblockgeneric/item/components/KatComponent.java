package net.swofty.type.skyblockgeneric.item.components;

import lombok.Getter;
import net.swofty.type.skyblockgeneric.item.SkyBlockItemComponent;

public class KatComponent extends SkyBlockItemComponent {
    @Getter
    private final int reducedDays;

    public KatComponent(int reducedDays) {
        this.reducedDays = reducedDays;
        addInheritedComponent(new TrackedUniqueComponent());
    }
}