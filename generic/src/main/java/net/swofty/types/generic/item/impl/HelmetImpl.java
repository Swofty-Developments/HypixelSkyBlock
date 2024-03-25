package net.swofty.types.generic.item.impl;

import net.swofty.types.generic.item.ReforgeType;

public interface HelmetImpl extends ExtraRarityDisplay, Reforgable {
    default String getExtraRarityDisplay() {
        return " HELMET";
    }

    default ReforgeType getReforgeType() {
        return ReforgeType.ARMOR;
    }
}
