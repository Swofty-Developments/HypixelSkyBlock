package net.swofty.commons.zombies;

import lombok.Getter;

@Getter
public enum ZombiesLeaderboardView {
    TOP_10("top_10", "Top 10"),
    PLAYERS_AROUND_YOU("around_you", "Players Around You");

    private final String key;
    private final String displayName;

    ZombiesLeaderboardView(String key, String displayName) {
        this.key = key;
        this.displayName = displayName;
    }

    public ZombiesLeaderboardView next() {
        ZombiesLeaderboardView[] values = values();
        int nextOrdinal = (this.ordinal() + 1) % values.length;
        return values[nextOrdinal];
    }

    public ZombiesLeaderboardView previous() {
        ZombiesLeaderboardView[] values = values();
        int prevOrdinal = (this.ordinal() - 1 + values.length) % values.length;
        return values[prevOrdinal];
    }

    public static ZombiesLeaderboardView fromKey(String key) {
        for (ZombiesLeaderboardView view : values()) {
            if (view.key.equalsIgnoreCase(key)) {
                return view;
            }
        }
        return TOP_10;
    }
}
