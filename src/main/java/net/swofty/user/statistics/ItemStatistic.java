package net.swofty.user.statistics;

import lombok.Getter;

@Getter
public enum ItemStatistic {
    DAMAGE("Damage", true, "+", ""),
    DEFENSE("Defense", false, "+", ""),
    HEALTH("Health", true, "+", ""),
    STRENGTH("Strength", true, "+", ""),
    INTELLIGENCE("Intelligence", false, "+", ""),
    MINING_SPEED("Mining Speed", false, "+", ""),
    ;

    private final String displayName;
    private final boolean isRed;
    private final String prefix;
    private final String suffix;

    ItemStatistic(String displayName, boolean isRed, String prefix, String suffix) {
        this.displayName = displayName;
        this.isRed = isRed;
        this.prefix = prefix;
        this.suffix = suffix;
    }
}