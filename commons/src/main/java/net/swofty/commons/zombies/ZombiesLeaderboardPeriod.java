package net.swofty.commons.zombies;

import lombok.Getter;

@Getter
public enum ZombiesLeaderboardPeriod {
    DAILY("daily", "Daily", 1),
    WEEKLY("weekly", "Weekly", 7),
    MONTHLY("monthly", "Monthly", 30),
    LIFETIME("lifetime", "Lifetime", -1);

    private final String key;
    private final String displayName;
    private final int days;

    ZombiesLeaderboardPeriod(String key, String displayName, int days) {
        this.key = key;
        this.displayName = displayName;
        this.days = days;
    }

    public ZombiesLeaderboardPeriod next() {
        ZombiesLeaderboardPeriod[] values = values();
        int nextOrdinal = (this.ordinal() + 1) % values.length;
        return values[nextOrdinal];
    }

    public ZombiesLeaderboardPeriod previous() {
        ZombiesLeaderboardPeriod[] values = values();
        int prevOrdinal = (this.ordinal() - 1 + values.length) % values.length;
        return values[prevOrdinal];
    }

    public static ZombiesLeaderboardPeriod fromKey(String key) {
        for (ZombiesLeaderboardPeriod period : values()) {
            if (period.key.equalsIgnoreCase(key)) {
                return period;
            }
        }
        return LIFETIME;
    }
}
