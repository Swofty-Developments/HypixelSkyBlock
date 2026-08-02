package net.swofty.type.ravengardgeneric.item.statistics;

import lombok.Getter;

/**
 * The stats an item can carry, mirroring SkyBlock's ItemStatistic: display name, tooltip order
 * and the pack's stat icon. Values render in the stat colour with the label in white, as every
 * captured tooltip shows.
 */
@Getter
public enum RavengardItemStatistic {
    DAMAGE("Damage", 0xE22F),
    RANGED_DAMAGE("Ranged Damage", 0xE230),
    ATTACK_SPEED("Attack Speed", 0xE233),
    DEFENSE("Defense", 0xE231),
    RELOAD("Reload", 0xE232),
    COOLDOWN("Cooldown", 0xE22E);

    private final String displayName;
    private final int iconGlyph;

    RavengardItemStatistic(String displayName, int iconGlyph) {
        this.displayName = displayName;
        this.iconGlyph = iconGlyph;
    }

    public static RavengardItemStatistic fromKey(String key) {
        for (RavengardItemStatistic value : values()) {
            if (value.name().equalsIgnoreCase(key)) {
                return value;
            }
        }
        return null;
    }
}
