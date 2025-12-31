package net.swofty.commons.murdermystery;

import lombok.Getter;

@Getter
public enum MurderMysteryLeaderboardPeriod {
    WEEKLY("weekly", "Weekly", 7),
    MONTHLY("monthly", "Monthly", 30),
    LIFETIME("lifetime", "Lifetime", -1);

    private final String key;
    private final String displayName;
    private final int days;

    MurderMysteryLeaderboardPeriod(String key, String displayName, int days) {
        this.key = key;
        this.displayName = displayName;
        this.days = days;
    }

    public MurderMysteryLeaderboardPeriod next() {
        MurderMysteryLeaderboardPeriod[] values = values();
        int nextOrdinal = (this.ordinal() + 1) % values.length;
        return values[nextOrdinal];
    }

    public MurderMysteryLeaderboardPeriod previous() {
        MurderMysteryLeaderboardPeriod[] values = values();
        int prevOrdinal = (this.ordinal() - 1 + values.length) % values.length;
        return values[prevOrdinal];
    }

    public static MurderMysteryLeaderboardPeriod fromKey(String key) {
        for (MurderMysteryLeaderboardPeriod period : values()) {
            if (period.key.equalsIgnoreCase(key)) {
                return period;
            }
        }
        return LIFETIME;
    }
}
