package net.swofty.commons.skywars;

import lombok.Getter;

@Getter
public enum SkywarsLeaderboardPeriod {
    DAILY("daily", "Daily", 1),
    WEEKLY("weekly", "Weekly", 7),
    MONTHLY("monthly", "Monthly", 30),
    LIFETIME("lifetime", "Lifetime", -1);

    private final String key;
    private final String displayName;
    private final int days;

    SkywarsLeaderboardPeriod(String key, String displayName, int days) {
        this.key = key;
        this.displayName = displayName;
        this.days = days;
    }

    public SkywarsLeaderboardPeriod next() {
        SkywarsLeaderboardPeriod[] values = values();
        int nextOrdinal = (this.ordinal() + 1) % values.length;
        return values[nextOrdinal];
    }

    public SkywarsLeaderboardPeriod previous() {
        SkywarsLeaderboardPeriod[] values = values();
        int prevOrdinal = (this.ordinal() - 1 + values.length) % values.length;
        return values[prevOrdinal];
    }

    public static SkywarsLeaderboardPeriod fromKey(String key) {
        for (SkywarsLeaderboardPeriod period : values()) {
            if (period.key.equalsIgnoreCase(key)) {
                return period;
            }
        }
        return LIFETIME;
    }
}
