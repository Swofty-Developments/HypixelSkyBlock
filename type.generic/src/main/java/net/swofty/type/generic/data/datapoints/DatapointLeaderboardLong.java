package net.swofty.type.generic.data.datapoints;

import lombok.Getter;
import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.type.generic.data.Datapoint;
import net.swofty.type.generic.leaderboard.LeaderboardTracked;

/**
 * A Long datapoint that automatically syncs to a Redis leaderboard.
 * Use this for numeric values that should be ranked (XP, coins, kills, etc.)
 *
 * @see LeaderboardTracked
 */
public class DatapointLeaderboardLong extends Datapoint<Long> implements LeaderboardTracked {
    private static final JacksonSerializer<Long> serializer = new JacksonSerializer<>(Long.class);

    @Getter
    private final String leaderboardKey;

    /**
     * Create a leaderboard-tracked Long datapoint.
     * @param key The datapoint key (for MongoDB storage)
     * @param value The default value
     * @param leaderboardKey The leaderboard key (e.g., "bedwars:experience")
     */
    public DatapointLeaderboardLong(String key, Long value, String leaderboardKey) {
        super(key, value, serializer);
        this.leaderboardKey = leaderboardKey;
    }

    /**
     * Create a leaderboard-tracked Long datapoint with null default value.
     * @param key The datapoint key
     * @param leaderboardKey The leaderboard key
     */
    public DatapointLeaderboardLong(String key, String leaderboardKey) {
        super(key, null, serializer);
        this.leaderboardKey = leaderboardKey;
    }

    @Override
    public double getLeaderboardScore() {
        return value == null ? 0.0 : value.doubleValue();
    }

    @Override
    public Datapoint<Long> deepClone() {
        DatapointLeaderboardLong clone = new DatapointLeaderboardLong(getKey(), value, leaderboardKey);
        clone.dataHandler = this.dataHandler;
        clone.data = this.data;
        return clone;
    }
}
