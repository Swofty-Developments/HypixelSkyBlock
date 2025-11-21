package net.swofty.anticheat.flag.flags;

import net.swofty.anticheat.event.ListenerMethod;
import net.swofty.anticheat.event.events.PlayerAttackEvent;
import net.swofty.anticheat.flag.Flag;

import java.util.*;

public class AutoClickerFlag extends Flag {
    // Track click timings per player
    private static final Map<UUID, ClickData> clickData = new HashMap<>();

    private static class ClickData {
        List<Long> clickTimes = new ArrayList<>();
        static final int MAX_SAMPLES = 50;

        void addClick(long timestamp) {
            clickTimes.add(timestamp);
            if (clickTimes.size() > MAX_SAMPLES) {
                clickTimes.removeFirst();
            }
        }

        List<Long> getIntervals() {
            List<Long> intervals = new ArrayList<>();
            for (int i = 1; i < clickTimes.size(); i++) {
                intervals.add(clickTimes.get(i) - clickTimes.get(i - 1));
            }
            return intervals;
        }
    }

    @ListenerMethod
    public void onPlayerAttack(PlayerAttackEvent event) {
        UUID uuid = event.getAttacker().getUuid();
        long timestamp = event.getTimestamp();

        ClickData data = clickData.computeIfAbsent(uuid, k -> new ClickData());
        data.addClick(timestamp);

        if (data.clickTimes.size() < 20) {
            return; // Need more samples
        }

        List<Long> intervals = data.getIntervals();

        // Calculate statistics
        double avgInterval = intervals.stream().mapToLong(Long::longValue).average().orElse(0);
        double stdDev = calculateStandardDeviation(intervals, avgInterval);

        // Pattern 1: Too consistent (autoclicker has very low variance)
        // Human clicks vary, autoclickers are consistent
        if (stdDev < 5.0 && avgInterval < 100) {
            // Very low standard deviation = very consistent = likely autoclicker
            double certainty = Math.min(0.95, 0.7 + (5.0 - stdDev) / 10.0);
            event.getAttacker().flag(net.swofty.anticheat.flag.FlagType.AUTOCLICKER, certainty);
        }

        // Pattern 2: Impossibly fast CPS (>20 CPS is suspicious, >25 is nearly impossible)
        double cps = 1000.0 / avgInterval;
        if (cps > 20) {
            double certainty = Math.min(0.95, 0.6 + (cps - 20) * 0.05);
            event.getAttacker().flag(net.swofty.anticheat.flag.FlagType.AUTOCLICKER, certainty);
        }

        // Pattern 3: Perfect rhythm (intervals are exact multiples)
        if (isPerfectRhythm(intervals)) {
            event.getAttacker().flag(net.swofty.anticheat.flag.FlagType.AUTOCLICKER, 0.85);
        }

        // Clean up old data
        if (data.clickTimes.size() >= ClickData.MAX_SAMPLES) {
            data.clickTimes.subList(0, 10).clear();
        }
    }

    private double calculateStandardDeviation(List<Long> values, double mean) {
        double sumSquaredDiff = 0;
        for (long value : values) {
            double diff = value - mean;
            sumSquaredDiff += diff * diff;
        }
        return Math.sqrt(sumSquaredDiff / values.size());
    }

    private boolean isPerfectRhythm(List<Long> intervals) {
        if (intervals.size() < 10) return false;

        // Check if most intervals are within 2ms of each other
        Map<Long, Integer> intervalCounts = new HashMap<>();
        for (long interval : intervals) {
            // Round to nearest 5ms
            long rounded = (interval / 5) * 5;
            intervalCounts.put(rounded, intervalCounts.getOrDefault(rounded, 0) + 1);
        }

        // If >80% of intervals are the same (rounded), it's suspiciously perfect
        int maxCount = intervalCounts.values().stream().max(Integer::compareTo).orElse(0);
        return (double) maxCount / intervals.size() > 0.8;
    }
}
