package net.swofty.types.generic.item.impl;

import net.minestom.server.coordinate.Vec;
import net.swofty.types.generic.item.ReforgeType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.user.SkyBlockPlayer;

public interface BowImpl extends ExtraRarityDisplay, QuiverDisplayOnHold, Reforgable {
    default String getExtraRarityDisplay() {
        return " BOW";
    }

    default ReforgeType getReforgeType() {
        return ReforgeType.BOWS;
    }

    void onBowShoot(SkyBlockPlayer player, SkyBlockItem item);

    default Vec calculateArrowVelocity(float playerPitch, float playerYaw) {
        // Normalize the drawback value to a range of 0.0 to 1.0
        double normalizedDrawback = (double) 4 / 5.0;

        // Calculate the arrow speed based on the drawback value
        double arrowSpeed = 3.0 * normalizedDrawback;

        // Convert player's pitch and yaw to radians
        double pitchRadians = Math.toRadians(playerPitch);
        double yawRadians = Math.toRadians(playerYaw);

        // Calculate the arrow's velocity components
        double velocityX = -Math.sin(yawRadians) * arrowSpeed;
        double velocityY = -Math.sin(pitchRadians) * arrowSpeed;
        double velocityZ = Math.cos(yawRadians) * Math.cos(pitchRadians) * arrowSpeed;

        // Create and return the velocity vector
        return new Vec(velocityX, velocityY, velocityZ).mul(14, 10, 14);
    }
}
