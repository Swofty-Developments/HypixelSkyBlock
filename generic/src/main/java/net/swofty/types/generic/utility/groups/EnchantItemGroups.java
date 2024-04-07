package net.swofty.types.generic.utility.groups;

import lombok.Getter;
import net.swofty.types.generic.utility.StringUtility;

@Getter
public enum EnchantItemGroups {
    TOOLS("Tools"),
    PICKAXE("Mining Tools"),
    SWORD("Melee Weapon"),
    FISHING_WEAPON("Melee Weapon"),
    LONG_SWORD("Melee Weapon"),
    ARMOR("Armor"),
    FISHING_ROD("Tools"),
    GAUNTLET("Melee Weapon"),
    DRILL("Tools"),
    BOW("Bow"),
    HOE("Hoe"),
    ;

    private final String displayName;

    EnchantItemGroups(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return StringUtility.toNormalCase(super.toString().toLowerCase());
    }
}
