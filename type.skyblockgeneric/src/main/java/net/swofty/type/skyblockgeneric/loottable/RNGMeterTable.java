package net.swofty.type.skyblockgeneric.loottable;

import lombok.NonNull;

public abstract class RNGMeterTable extends SkyBlockLootTable {
    public abstract @NonNull MeterType getRNGMeterType();

    public enum MeterType {
        SLAYER,
        CATACOMBS
    }
}
