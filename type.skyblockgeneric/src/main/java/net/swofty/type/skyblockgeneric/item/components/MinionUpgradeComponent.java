package net.swofty.type.skyblockgeneric.item.components;

import lombok.Getter;
import net.swofty.type.skyblockgeneric.item.SkyBlockItemComponent;

@Getter
public class MinionUpgradeComponent extends SkyBlockItemComponent {
    private final double speedIncrease;

    public MinionUpgradeComponent(double speedIncrease) {
        addInheritedComponent(new TrackedUniqueComponent());

        this.speedIncrease = speedIncrease;
    }
}
