package net.swofty.anticheat.prediction.modifier.impl;

import net.swofty.anticheat.math.Vel;
import net.swofty.anticheat.prediction.PlayerContext;
import net.swofty.anticheat.prediction.modifier.VelocityModifier;

/**
 * Bubble column modifier
 * Upward bubble columns push player up, downward ones pull down
 */
public class BubbleColumnModifier extends VelocityModifier {

    private static final double UPWARD_PUSH = 0.1;
    private static final double DOWNWARD_PULL = -0.3;

    @Override
    public Vel apply(Vel currentVel, PlayerContext context) {
        boolean isUpward = context.getCustomData("bubbleColumnUpward");

        double verticalModifier = isUpward ? UPWARD_PUSH : DOWNWARD_PULL;

        return new Vel(
            currentVel.x(),
            currentVel.y() + verticalModifier,
            currentVel.z()
        );
    }

    @Override
    public boolean shouldApply(PlayerContext context) {
        return context.isInBubbleColumn();
    }

    @Override
    public int getPriority() {
        return 120;
    }

    @Override
    public String getName() {
        return "Bubble Column";
    }
}
