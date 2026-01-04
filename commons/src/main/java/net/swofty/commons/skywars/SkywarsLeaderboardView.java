package net.swofty.commons.skywars;

import lombok.Getter;

@Getter
public enum SkywarsLeaderboardView {
    TOP_10("top_10", "Top 10"),
    PLAYERS_AROUND_YOU("around_you", "Players Around You");

    private final String key;
    private final String displayName;

    SkywarsLeaderboardView(String key, String displayName) {
        this.key = key;
        this.displayName = displayName;
    }

    public SkywarsLeaderboardView next() {
        SkywarsLeaderboardView[] values = values();
        int nextOrdinal = (this.ordinal() + 1) % values.length;
        return values[nextOrdinal];
    }

    public SkywarsLeaderboardView previous() {
        SkywarsLeaderboardView[] values = values();
        int prevOrdinal = (this.ordinal() - 1 + values.length) % values.length;
        return values[prevOrdinal];
    }

    public static SkywarsLeaderboardView fromKey(String key) {
        for (SkywarsLeaderboardView view : values()) {
            if (view.key.equalsIgnoreCase(key)) {
                return view;
            }
        }
        return TOP_10;
    }
}
