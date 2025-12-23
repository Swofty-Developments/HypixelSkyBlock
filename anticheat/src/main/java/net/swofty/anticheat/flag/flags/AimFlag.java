package net.swofty.anticheat.flag.flags;

import net.swofty.anticheat.engine.PlayerTickInformation;
import net.swofty.anticheat.event.ListenerMethod;
import net.swofty.anticheat.event.events.PlayerAttackEvent;
import net.swofty.anticheat.event.events.PlayerPositionUpdateEvent;
import net.swofty.anticheat.flag.Flag;
import net.swofty.anticheat.math.Pos;

import java.util.*;

public class AimFlag extends Flag {
    // Track rotation changes per player
    private static final Map<UUID, RotationData> rotationData = new HashMap<>();

    private static class RotationData {
        List<Float> yawChanges = new ArrayList<>();
        List<Float> pitchChanges = new ArrayList<>();
        static final int MAX_SAMPLES = 20;

        void addRotation(float yawChange, float pitchChange) {
            yawChanges.add(Math.abs(yawChange));
            pitchChanges.add(Math.abs(pitchChange));

            if (yawChanges.size() > MAX_SAMPLES) {
                yawChanges.removeFirst();
                pitchChanges.removeFirst();
            }
        }
    }

    @ListenerMethod
    public void onPlayerPositionUpdate(PlayerPositionUpdateEvent event) {
        // Disabled: heuristic-based detection has too many false positives
    }

    @ListenerMethod
    public void onPlayerAttack(PlayerAttackEvent event) {
        // Disabled: heuristic-based detection has too many false positives
    }

    private boolean isStaticAim(List<Float> changes) {
        // Check if there are long periods with zero rotation
        int staticCount = 0;
        for (float change : changes) {
            if (change < 0.01) {
                staticCount++;
            }
        }
        // More than 70% static is suspicious during active movement
        return (double) staticCount / changes.size() > 0.7;
    }

    private boolean hasRoboticPattern(List<Float> changes) {
        if (changes.size() < 10) return false;

        // Check for repeated exact values
        Map<Float, Integer> valueCounts = new HashMap<>();
        for (float change : changes) {
            if (change > 0.1) { // Ignore very small changes
                // Round to 2 decimal places
                float rounded = Math.round(change * 100f) / 100f;
                valueCounts.put(rounded, valueCounts.getOrDefault(rounded, 0) + 1);
            }
        }

        // If any value repeats more than 40% of the time, it's robotic
        int maxCount = valueCounts.values().stream().max(Integer::compareTo).orElse(0);
        return (double) maxCount / changes.size() > 0.4;
    }
}
