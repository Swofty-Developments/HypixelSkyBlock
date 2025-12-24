package net.swofty.commons.skyblock.item.reforge;

import lombok.Getter;

@Getter
public enum ReforgeType {
    SWORDS("Swords"),
    BOWS("Bows"),
    ARMOR("Armor"),
    EQUIPMENT("Equipment"),
    FISHING_RODS("Fishing Rods"),
    PICKAXES("Pickaxes"),
    AXES("Axes"),
    HOES("Hoes"),
    VACUUMS("Vacuums");

    private final String displayName;

    ReforgeType(String displayName) {
        this.displayName = displayName;
    }
}