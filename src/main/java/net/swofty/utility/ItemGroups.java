package net.swofty.utility;

import lombok.Getter;
import net.minestom.server.item.Material;
import net.swofty.item.ItemType;

import java.util.Arrays;
import java.util.List;

@Getter
public enum ItemGroups {
    TOOLS("Tools"),
    PICKAXE("Mining Tools"),
    SWORD("Melee Weapon"),
    FISHING_WEAPON("Melee Weapon"),
    LONG_SWORD("Melee Weapon"),
    ARMOR("Armor"),
    FISHING_ROD("Tools"),
    GAUNTLET("Melee Weapon"),
    DRILL("Tools"),
    ;

    private final String displayName;

    ItemGroups(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return StringUtility.toNormalCase(super.toString().toLowerCase());
    }
}
