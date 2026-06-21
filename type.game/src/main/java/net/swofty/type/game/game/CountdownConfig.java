package net.swofty.type.game.game;

import java.util.Arrays;
import java.util.Objects;

/**
 * Controls countdown timing and announcements.
 */
public record CountdownConfig(
        int durationSeconds,
        int[] announcementTimes,
        int acceleratedDurationSeconds,
        int accelerationPlayerThreshold
) {
    public static final CountdownConfig DEFAULT = new CountdownConfig(
            30,
            new int[]{30, 20, 15, 10, 5, 4, 3, 2, 1},
            10,
        -1 // no accel
    );

    public CountdownConfig {
        if (durationSeconds < 0) {
            throw new IllegalArgumentException("durationSeconds must not be negative");
        }
        if (acceleratedDurationSeconds < 0) {
            throw new IllegalArgumentException("acceleratedDurationSeconds must not be negative");
        }
        announcementTimes = Arrays.copyOf(
            Objects.requireNonNull(announcementTimes, "announcementTimes"),
            announcementTimes.length
        );
    }

    @Override
    public int[] announcementTimes() {
        return Arrays.copyOf(announcementTimes, announcementTimes.length);
    }

    public static CountdownConfig simple(int durationSeconds) {
        return new CountdownConfig(durationSeconds, new int[]{10, 5, 4, 3, 2, 1}, durationSeconds, -1);
    }

    public static CountdownConfig withAcceleration(int normalDuration, int acceleratedDuration, int playerThreshold) {
        return new CountdownConfig(
                normalDuration,
                new int[]{30, 20, 15, 10, 5, 4, 3, 2, 1},
                acceleratedDuration,
                playerThreshold
        );
    }

    public boolean shouldAnnounce(int seconds) {
        for (int announcementTime : announcementTimes) {
            if (announcementTime == seconds) {
                return true;
            }
        }
        return false;
    }
}
