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
        if (event.getPreviousTick() == null) return;

        Pos currentPos = event.getCurrentTick().getPos();
        Pos previousPos = event.getPreviousTick().getPos();

        float yawChange = Math.abs(currentPos.yaw() - previousPos.yaw());
        float pitchChange = Math.abs(currentPos.pitch() - previousPos.pitch());

        // Normalize yaw change (handle wrapping)
        if (yawChange > 180) {
            yawChange = 360 - yawChange;
        }

        UUID uuid = event.getPlayer().getUuid();
        RotationData data = rotationData.computeIfAbsent(uuid, k -> new RotationData());
        data.addRotation(yawChange, pitchChange);

        if (data.yawChanges.size() < 10) return;

        // Pattern 1: Impossible rotation speed (>180 degrees per tick is suspicious)
        if (yawChange > 180 || pitchChange > 90) {
            double certainty = Math.min(0.9, 0.6 + Math.max(yawChange, pitchChange) / 360.0);
            event.getPlayer().flag(net.swofty.anticheat.flag.FlagType.AIM, certainty);
        }

        // Pattern 2: Perfect lock-on (no micro-adjustments)
        // Human aim has small variations, aimbots lock perfectly
        if (isStaticAim(data.yawChanges) && isStaticAim(data.pitchChanges)) {
            event.getPlayer().flag(net.swofty.anticheat.flag.FlagType.AIM, 0.75);
        }

        // Pattern 3: Robotic patterns (same rotation amount repeatedly)
        if (hasRoboticPattern(data.yawChanges) || hasRoboticPattern(data.pitchChanges)) {
            event.getPlayer().flag(net.swofty.anticheat.flag.FlagType.AIM, 0.8);
        }
    }

    @ListenerMethod
    public void onPlayerAttack(PlayerAttackEvent event) {
        // Check if player snapped to target
        Pos attackerPos = event.getAttacker().getCurrentTick().getPos();
        Pos targetPos = event.getTargetPosition();

        // Calculate required look angle to hit target
        Pos requiredLook = attackerPos.withLookAt(targetPos);

        // Check how close the player's actual look is to perfect
        float yawDiff = Math.abs(attackerPos.yaw() - requiredLook.yaw());
        float pitchDiff = Math.abs(attackerPos.pitch() - requiredLook.pitch());

        // Normalize yaw difference
        if (yawDiff > 180) yawDiff = 360 - yawDiff;

        // Perfect aim (within 0.5 degrees) every time is suspicious
        if (yawDiff < 0.5 && pitchDiff < 0.5) {
            UUID uuid = event.getAttacker().getUuid();
            RotationData data = rotationData.get(uuid);

            if (data != null && data.yawChanges.size() >= 5) {
                // Check if last few rotations were also perfect
                long perfectCount = data.yawChanges.stream()
                    .filter(change -> change < 1.0)
                    .count();

                if (perfectCount >= 3) {
                    event.getAttacker().flag(net.swofty.anticheat.flag.FlagType.AIM, 0.85);
                }
            }
        }
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
