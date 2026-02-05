package net.swofty.type.game.game;

/**
 * Configuration for game countdown behavior.
 * Immutable record for clean configuration passing.
 */
public record CountdownConfig(
        int durationSeconds,
        int[] announcementTimes,
        int acceleratedDurationSeconds,
        int accelerationPlayerThreshold
) {
    /**
     * Default countdown configuration.
     */
    public static final CountdownConfig DEFAULT = new CountdownConfig(
            30,
            new int[]{30, 20, 15, 10, 5, 4, 3, 2, 1},
            10,
            -1 // No acceleration by default
    );

    /**
     * Creates a simple countdown config with just duration.
     */
    public static CountdownConfig simple(int durationSeconds) {
        return new CountdownConfig(durationSeconds, new int[]{10, 5, 4, 3, 2, 1}, durationSeconds, -1);
    }

    /**
     * Creates a countdown config with acceleration when enough players join.
     */
    public static CountdownConfig withAcceleration(int normalDuration, int acceleratedDuration, int playerThreshold) {
        return new CountdownConfig(
                normalDuration,
                new int[]{30, 20, 15, 10, 5, 4, 3, 2, 1},
                acceleratedDuration,
                playerThreshold
        );
    }

    /**
     * Checks if a given second value should trigger an announcement.
     */
    public boolean shouldAnnounce(int seconds) {
        for (int time : announcementTimes) {
            if (seconds == time) return true;
        }
        return false;
    }
}
