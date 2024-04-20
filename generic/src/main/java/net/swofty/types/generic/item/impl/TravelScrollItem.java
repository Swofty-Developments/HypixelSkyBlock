package net.swofty.types.generic.item.impl;

public interface TravelScrollItem extends ExtraRarityDisplay {

    default String getExtraRarityDisplay() {
        return " TRAVEL SCROLL";
    }

}
