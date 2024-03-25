package net.swofty.types.generic.item.impl;

import net.swofty.types.generic.item.ReforgeType;

public interface BowImpl extends ExtraRarityDisplay, QuiverDisplayOnHold, Reforgable {
    default String getExtraRarityDisplay() {
        return " BOW";
    }

    default ReforgeType getReforgeType() {
        return ReforgeType.BOWS;
    }
}
