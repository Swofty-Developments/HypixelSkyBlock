package net.swofty.type.skyblockgeneric.item.components;

import lombok.Getter;
import net.swofty.type.generic.item.SkyBlockItemComponent;

public class MinionFuelComponent extends SkyBlockItemComponent {
    @Getter
    private final double fuelPercentage;
    @Getter
    private final long fuelLastTimeInMS;

    public MinionFuelComponent(double fuelPercentage, long fuelLastTimeInMS) {
        this.fuelPercentage = fuelPercentage;
        this.fuelLastTimeInMS = fuelLastTimeInMS;
    }
}