package net.swofty.type.skyblockgeneric.item.components;

import lombok.Getter;
import net.swofty.type.skyblockgeneric.item.SkyBlockItemComponent;

public class MinionFuelComponent extends SkyBlockItemComponent {
    @Getter
    private final double fuelPercentage;
    @Getter
    private final long fuelLastTimeInMS;
    @Getter
    private final double outputMultiplier;

    public MinionFuelComponent(double fuelPercentage, long fuelLastTimeInMS) {
        this(fuelPercentage, fuelLastTimeInMS, 1.0);
    }

    public MinionFuelComponent(double fuelPercentage, long fuelLastTimeInMS, double outputMultiplier) {
        this.fuelPercentage = fuelPercentage;
        this.fuelLastTimeInMS = fuelLastTimeInMS;
        this.outputMultiplier = outputMultiplier;
    }
}
