package net.swofty.commons.murdermystery;

import lombok.Getter;

@Getter
public enum MurderMysteryLeaderboardView {
    TOP_10("top_10", "Top 10"),
    PLAYERS_AROUND_YOU("around_you", "Players Around You");

    private final String key;
    private final String displayName;

    MurderMysteryLeaderboardView(String key, String displayName) {
        this.key = key;
        this.displayName = displayName;
    }

    public MurderMysteryLeaderboardView next() {
        MurderMysteryLeaderboardView[] values = values();
        int nextOrdinal = (this.ordinal() + 1) % values.length;
        return values[nextOrdinal];
    }

    public MurderMysteryLeaderboardView previous() {
        MurderMysteryLeaderboardView[] values = values();
        int prevOrdinal = (this.ordinal() - 1 + values.length) % values.length;
        return values[prevOrdinal];
    }

    public static MurderMysteryLeaderboardView fromKey(String key) {
        for (MurderMysteryLeaderboardView view : values()) {
            if (view.key.equalsIgnoreCase(key)) {
                return view;
            }
        }
        return TOP_10;
    }
}
