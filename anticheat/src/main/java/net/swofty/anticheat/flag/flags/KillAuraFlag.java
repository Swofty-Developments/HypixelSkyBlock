package net.swofty.anticheat.flag.flags;

import net.swofty.anticheat.event.ListenerMethod;
import net.swofty.anticheat.event.events.PlayerAttackEvent;
import net.swofty.anticheat.flag.Flag;
import net.swofty.anticheat.math.Pos;

import java.util.*;

public class KillAuraFlag extends Flag {
    // Track attack patterns per player
    private static final Map<UUID, AttackData> attackData = new HashMap<>();

    private static class AttackData {
        List<AttackInfo> attacks = new ArrayList<>();
        static final int MAX_ATTACKS = 30;

        void addAttack(UUID targetId, Pos attackerPos, Pos targetPos, long timestamp) {
            attacks.add(new AttackInfo(targetId, attackerPos, targetPos, timestamp));
            if (attacks.size() > MAX_ATTACKS) {
                attacks.removeFirst();
            }
        }
    }

    private static class AttackInfo {
        UUID targetId;
        Pos attackerPos;
        Pos targetPos;
        long timestamp;

        AttackInfo(UUID targetId, Pos attackerPos, Pos targetPos, long timestamp) {
            this.targetId = targetId;
            this.attackerPos = attackerPos;
            this.targetPos = targetPos;
            this.timestamp = timestamp;
        }
    }

    @ListenerMethod
    public void onPlayerAttack(PlayerAttackEvent event) {
        UUID attackerId = event.getAttacker().getUuid();
        AttackData data = attackData.computeIfAbsent(attackerId, k -> new AttackData());

        data.addAttack(
            event.getTargetUuid(),
            event.getAttacker().getCurrentTick().getPos(),
            event.getTargetPosition(),
            event.getTimestamp()
        );

        if (data.attacks.size() < 5) return;

        // Pattern 1: Multi-aura (attacking multiple entities rapidly)
        long recentTime = event.getTimestamp() - 1000; // Last 1 second
        Set<UUID> recentTargets = new HashSet<>();
        int recentAttacks = 0;

        for (AttackInfo attack : data.attacks) {
            if (attack.timestamp > recentTime) {
                recentTargets.add(attack.targetId);
                recentAttacks++;
            }
        }

        // Attacking 3+ different entities in 1 second is suspicious
        if (recentTargets.size() >= 3 && recentAttacks >= 5) {
            double certainty = Math.min(0.95, 0.6 + (recentTargets.size() * 0.1));
            event.getAttacker().flag(net.swofty.anticheat.flag.FlagType.KILLAURA, certainty);
        }

        // Pattern 2: 360-degree attacks (attacking entities behind/beside without looking)
        int suspiciousAngleCount = 0;
        List<AttackInfo> recentAttackList = data.attacks.subList(
            Math.max(0, data.attacks.size() - 10),
            data.attacks.size()
        );

        for (AttackInfo attack : recentAttackList) {
            // Calculate angle between look direction and target
            Pos lookDirection = attack.attackerPos.withLookAt(attack.targetPos);
            float yawDiff = Math.abs(attack.attackerPos.yaw() - lookDirection.yaw());

            // Normalize
            if (yawDiff > 180) yawDiff = 360 - yawDiff;

            // Attacking with >90 degree angle is suspicious
            if (yawDiff > 90) {
                suspiciousAngleCount++;
            }
        }

        // More than 40% of attacks at bad angles = killaura
        if ((double) suspiciousAngleCount / recentAttackList.size() > 0.4) {
            double certainty = Math.min(0.9, 0.5 + suspiciousAngleCount * 0.05);
            event.getAttacker().flag(net.swofty.anticheat.flag.FlagType.KILLAURA, certainty);
        }

        // Pattern 3: Perfect tracking (always hitting center of hitbox)
        // This requires more sophisticated hitbox analysis
        int perfectHits = 0;
        for (AttackInfo attack : recentAttackList) {
            // Check if attack position is suspiciously close to center
            double dx = Math.abs(attack.targetPos.x() - Math.floor(attack.targetPos.x()) - 0.5);
            double dz = Math.abs(attack.targetPos.z() - Math.floor(attack.targetPos.z()) - 0.5);

            // Hitting within 0.1 blocks of exact center repeatedly is suspicious
            if (dx < 0.1 && dz < 0.1) {
                perfectHits++;
            }
        }

        if (perfectHits > 6) {
            double certainty = Math.min(0.85, 0.5 + perfectHits * 0.04);
            event.getAttacker().flag(net.swofty.anticheat.flag.FlagType.KILLAURA, certainty);
        }

        // Pattern 4: Constant attack rate (autoclicker + aura)
        if (recentAttackList.size() >= 8) {
            List<Long> intervals = new ArrayList<>();
            for (int i = 1; i < recentAttackList.size(); i++) {
                intervals.add(recentAttackList.get(i).timestamp - recentAttackList.get(i - 1).timestamp);
            }

            // Calculate variance
            double avg = intervals.stream().mapToLong(Long::longValue).average().orElse(0);
            double variance = 0;
            for (long interval : intervals) {
                variance += Math.pow(interval - avg, 2);
            }
            variance /= intervals.size();
            double stdDev = Math.sqrt(variance);

            // Very consistent timing + multiple targets = aura + autoclicker
            if (stdDev < 20 && recentTargets.size() >= 2) {
                event.getAttacker().flag(net.swofty.anticheat.flag.FlagType.KILLAURA, 0.9);
            }
        }
    }
}
