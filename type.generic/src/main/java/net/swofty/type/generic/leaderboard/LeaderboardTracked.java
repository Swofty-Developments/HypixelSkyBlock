package net.swofty.type.generic.leaderboard;

/**
 * Interface for datapoints that should be tracked on a single leaderboard.
 * Implement this in datapoints that hold simple numeric values (Long, Double, Integer).
 *
 * Example usage:
 * <pre>
 * public class DatapointLeaderboardLong extends DatapointLong implements LeaderboardTracked {
 *     private final String leaderboardKey;
 *
 *     public DatapointLeaderboardLong(String key, Long value, String leaderboardKey) {
 *         super(key, value);
 *         this.leaderboardKey = leaderboardKey;
 *     }
 *
 *     @Override
 *     public String getLeaderboardKey() { return leaderboardKey; }
 *
 *     @Override
 *     public double getLeaderboardScore() { return value.doubleValue(); }
 * }
 * </pre>
 */
public interface LeaderboardTracked {

    /**
     * Get the leaderboard key for this datapoint.
     * @return The leaderboard key (e.g., "bedwars:experience"), or null if not tracked
     */
    String getLeaderboardKey();

    /**
     * Get the numeric score for this datapoint's current value.
     * @return The score as a double
     */
    double getLeaderboardScore();
}
