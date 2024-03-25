package net.swofty.types.generic.item.impl;

import net.swofty.types.generic.item.ReforgeType;

public interface SwordImpl extends ExtraRarityDisplay, Reforgable {
    default String getExtraRarityDisplay() {
        return " SWORD";
    }

    default ReforgeType getReforgeType() {
        return ReforgeType.SWORDS;
    }
}
