package net.swofty.anticheat.prediction.compensation;

import lombok.Data;
import net.swofty.anticheat.math.Pos;
import net.swofty.anticheat.math.Vel;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Transaction-based lag compensation system
 * Tracks player state at different transaction IDs to handle packet reordering and lag
 */
public class LagCompensator {

    // Store player states keyed by transaction ID
    private final Map<UUID, Map<Integer, PlayerSnapshot>> playerSnapshots = new ConcurrentHashMap<>();

    // Track transaction times for ping calculation
    private final Map<UUID, Map<Integer, Long>> transactionTimes = new ConcurrentHashMap<>();

    /**
     * Record a player snapshot at a transaction ID
     */
    public void recordSnapshot(UUID playerId, int transactionId, Pos position, Vel velocity, long timestamp) {
        playerSnapshots.computeIfAbsent(playerId, k -> new ConcurrentHashMap<>())
            .put(transactionId, new PlayerSnapshot(position, velocity, timestamp));

        transactionTimes.computeIfAbsent(playerId, k -> new ConcurrentHashMap<>())
            .put(transactionId, timestamp);

        // Clean old snapshots (keep last 100)
        cleanOldSnapshots(playerId);
    }

    /**
     * Get player state at a specific transaction
     */
    public PlayerSnapshot getSnapshot(UUID playerId, int transactionId) {
        Map<Integer, PlayerSnapshot> snapshots = playerSnapshots.get(playerId);
        if (snapshots == null) return null;
        return snapshots.get(transactionId);
    }

    /**
     * Calculate transaction ping
     */
    public long getTransactionPing(UUID playerId, int transactionId, long responseTime) {
        Map<Integer, Long> times = transactionTimes.get(playerId);
        if (times == null) return -1;

        Long sentTime = times.get(transactionId);
        if (sentTime == null) return -1;

        return responseTime - sentTime;
    }

    /**
     * Get compensation offset for a given ping
     * Returns how far the player could have moved during the ping delay
     */
    public double getCompensationOffset(long ping, Vel velocity) {
        // Calculate how far player moves in ping time
        double ticksDelay = ping / 50.0; // 50ms per tick
        double distance = velocity.length() * ticksDelay;

        // Add some margin for uncertainty
        return distance * 1.1;
    }

    /**
     * Determine if we should skip checking due to lag spike
     */
    public boolean shouldSkipDueToLag(UUID playerId, long currentPing) {
        // If ping is over 500ms, too much uncertainty
        if (currentPing > 500) return true;

        // If ping is fluctuating heavily, skip
        Map<Integer, Long> times = transactionTimes.get(playerId);
        if (times == null || times.size() < 5) return false;

        List<Long> recentPings = new ArrayList<>(times.values());
        Collections.sort(recentPings);

        if (recentPings.size() < 5) return false;

        // Get last 5 pings
        List<Long> lastFive = recentPings.subList(Math.max(0, recentPings.size() - 5), recentPings.size());

        // Calculate variance
        double avg = lastFive.stream().mapToLong(Long::longValue).average().orElse(0);
        double variance = 0;
        for (long ping : lastFive) {
            variance += Math.pow(ping - avg, 2);
        }
        variance /= lastFive.size();

        // If variance is high, lag is unstable
        return variance > 10000; // Standard deviation > 100ms
    }

    private void cleanOldSnapshots(UUID playerId) {
        Map<Integer, PlayerSnapshot> snapshots = playerSnapshots.get(playerId);
        if (snapshots != null && snapshots.size() > 100) {
            // Remove oldest snapshots
            List<Map.Entry<Integer, PlayerSnapshot>> entries = new ArrayList<>(snapshots.entrySet());
            entries.sort(Comparator.comparingLong(e -> e.getValue().getTimestamp()));

            for (int i = 0; i < entries.size() - 100; i++) {
                snapshots.remove(entries.get(i).getKey());
            }
        }

        Map<Integer, Long> times = transactionTimes.get(playerId);
        if (times != null && times.size() > 100) {
            List<Map.Entry<Integer, Long>> entries = new ArrayList<>(times.entrySet());
            entries.sort(Comparator.comparingLong(Map.Entry::getValue));

            for (int i = 0; i < entries.size() - 100; i++) {
                times.remove(entries.get(i).getKey());
            }
        }
    }

    @Data
    public static class PlayerSnapshot {
        private final Pos position;
        private final Vel velocity;
        private final long timestamp;
    }
}
