package net.swofty.type.generic.leaderboard;

import java.util.Map;

/**
 * Interface for datapoints that hold map-based data where each key has its own leaderboard.
 * Implement this for datapoints like Collections, Skills, etc.
 *
 * Example usage for SkyBlock Collections:
 * <pre>
 * public class DatapointCollection extends SkyBlockDatapoint<PlayerCollection>
 *         implements MapLeaderboardTracked {
 *
 *     @Override
 *     public String getLeaderboardPrefix() {
 *         return "skyblock:collection";
 *     }
 *
 *     @Override
 *     public Map<String, Double> getAllLeaderboardScores() {
 *         Map<String, Double> scores = new HashMap<>();
 *         for (Map.Entry<ItemType, Integer> entry : value.getItems().entrySet()) {
 *             scores.put(entry.getKey().name(), entry.getValue().doubleValue());
 *         }
 *         return scores;
 *     }
 * }
 * </pre>
 *
 * This will create leaderboards like:
 * - skyblock:collection:WHEAT
 * - skyblock:collection:IRON_INGOT
 * - skyblock:collection:DIAMOND
 */
public interface MapLeaderboardTracked {

    /**
     * Get the prefix for all leaderboard keys from this datapoint.
     * @return The leaderboard prefix (e.g., "skyblock:collection")
     */
    String getLeaderboardPrefix();

    /**
     * Get all leaderboard scores from the current value.
     * @return Map of key suffix to score (e.g., {"WHEAT" -> 5000.0, "IRON_INGOT" -> 2500.0})
     */
    Map<String, Double> getAllLeaderboardScores();

    /**
     * Get the full leaderboard key for a specific entry.
     * Default implementation: prefix + ":" + keySuffix
     * @param keySuffix The suffix for this entry (e.g., "WHEAT")
     * @return The full leaderboard key (e.g., "skyblock:collection:WHEAT")
     */
    default String getLeaderboardKeyFor(String keySuffix) {
        return getLeaderboardPrefix() + ":" + keySuffix;
    }

    /**
     * Get only the scores that changed between old and new values.
     * Override this for optimized updates that only sync changed entries.
     *
     * @param oldValue The previous value
     * @param newValue The new value
     * @return Map of changed key suffixes to their new scores
     */
    default Map<String, Double> getChangedScores(Object oldValue, Object newValue) {
        // Default: return all scores (subclasses can override for efficiency)
        return getAllLeaderboardScores();
    }
}
