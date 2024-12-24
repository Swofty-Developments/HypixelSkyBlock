package net.swofty.types.generic.item.components;

import lombok.Getter;
import net.swofty.types.generic.item.SkyBlockItemComponent;

@Getter
public class MinionUpgradeComponent extends SkyBlockItemComponent {
    private final double speedIncrease;

    public MinionUpgradeComponent(double speedIncrease) {
        addInheritedComponent(new TrackedUniqueComponent());

        this.speedIncrease = speedIncrease;
    }
}
