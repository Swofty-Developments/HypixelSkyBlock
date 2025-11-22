package net.swofty.anticheat.flag.flags;

import net.swofty.anticheat.event.ListenerMethod;
import net.swofty.anticheat.event.events.PlayerAttackEvent;
import net.swofty.anticheat.flag.Flag;
import net.swofty.anticheat.math.Pos;

public class ReachFlag extends Flag {
    // Vanilla Minecraft reach is 3.0 blocks, with some tolerance for lag
    private static final double MAX_REACH = 3.1;
    private static final double SUSPICIOUS_REACH = 3.5;

    @ListenerMethod
    public void onPlayerAttack(PlayerAttackEvent event) {
        Pos attackerPos = event.getAttacker().getCurrentTick().getPos();
        Pos targetPos = event.getTargetPosition();

        // Calculate 3D distance
        double distance = calculateDistance(attackerPos, targetPos);

        // Account for player hitbox (0.6 blocks wide, 1.8 blocks tall)
        // and ping compensation
        long ping = event.getAttacker().getPing();
        double pingCompensation = (ping / 1000.0) * 0.28; // Max sprint speed compensation

        double effectiveDistance = distance - pingCompensation;

        if (effectiveDistance > MAX_REACH) {
            // Calculate certainty based on how far over the limit
            double excess = effectiveDistance - MAX_REACH;

            // 0.1 blocks over = 60%, 0.5+ blocks over = 95%
            double certainty;
            if (effectiveDistance > SUSPICIOUS_REACH) {
                certainty = 0.95;
            } else {
                certainty = Math.min(0.95, 0.6 + (excess * 0.7));
            }

            event.getAttacker().flag(net.swofty.anticheat.flag.FlagType.REACH, certainty);
        }
    }

    private double calculateDistance(Pos pos1, Pos pos2) {
        double dx = pos1.x() - pos2.x();
        double dy = pos1.y() - pos2.y();
        double dz = pos1.z() - pos2.z();
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }
}
