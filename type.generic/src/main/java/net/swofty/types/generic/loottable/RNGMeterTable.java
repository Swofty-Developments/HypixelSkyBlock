package net.swofty.types.generic.loottable;

import lombok.NonNull;

public abstract class RNGMeterTable extends SkyBlockLootTable {
    public abstract @NonNull MeterType getRNGMeterType();

    public enum MeterType {
        SLAYER,
        CATACOMBS
    }
}
