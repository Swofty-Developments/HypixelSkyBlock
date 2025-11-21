package net.swofty.anticheat.flag.flags;

import net.swofty.anticheat.event.ListenerMethod;
import net.swofty.anticheat.event.events.PlayerPositionUpdateEvent;
import net.swofty.anticheat.flag.Flag;
import net.swofty.anticheat.math.Pos;
import net.swofty.anticheat.math.Vel;
import net.swofty.anticheat.world.Block;
import net.swofty.anticheat.world.PlayerWorld;

public class JesusFlag extends Flag {

    @ListenerMethod
    public void onPlayerPositionUpdate(PlayerPositionUpdateEvent event) {
        Pos currentPos = event.getCurrentTick().getPos();
        Vel currentVel = event.getCurrentTick().getVel();
        boolean onGround = event.getCurrentTick().isOnGround();

        // If player says they're on ground, check if it's actually water/lava
        if (onGround) {
            checkWaterWalking(event, currentPos, currentVel);
        }

        // Also check if player is at water level but not sinking
        checkFloatingOnWater(event, currentPos, currentVel);
    }

    private void checkWaterWalking(PlayerPositionUpdateEvent event, Pos pos, Vel vel) {
        PlayerWorld world = event.getPlayer().getWorld();

        // Check block below player
        int blockX = (int) Math.floor(pos.x());
        int blockY = (int) Math.floor(pos.y() - 0.1); // Just below feet
        int blockZ = (int) Math.floor(pos.z());

        world.getBlock(blockX, blockY, blockZ).thenAccept(block -> {
            if (isLiquid(block)) {
                // Player is "on ground" but ground is liquid = Jesus/WaterWalk

                // Check if they're actually moving on it
                double horizontalSpeed = Math.sqrt(vel.x() * vel.x() + vel.z() * vel.z());

                if (horizontalSpeed > 0.05) {
                    // Walking on water
                    double certainty = Math.min(0.95, 0.7 + horizontalSpeed * 2);
                    event.getPlayer().flag(net.swofty.anticheat.flag.FlagType.JESUS, certainty);
                } else {
                    // Standing on water (could be edge case)
                    event.getPlayer().flag(net.swofty.anticheat.flag.FlagType.JESUS, 0.6);
                }
            }
        });
    }

    private void checkFloatingOnWater(PlayerPositionUpdateEvent event, Pos pos, Vel vel) {
        PlayerWorld world = event.getPlayer().getWorld();

        int blockX = (int) Math.floor(pos.x());
        int blockY = (int) Math.floor(pos.y());
        int blockZ = (int) Math.floor(pos.z());

        world.getBlock(blockX, blockY, blockZ).thenAccept(block -> {
            if (isLiquid(block)) {
                // Player is inside liquid block
                // Check if they're floating (not sinking)

                if (event.getPreviousTick() != null) {
                    Pos prevPos = event.getPreviousTick().getPos();
                    double yChange = pos.y() - prevPos.y();

                    // In water, player should be sinking or swimming (Y should change)
                    // If Y is constant or increasing without swimming motion, it's Jesus
                    if (Math.abs(yChange) < 0.01) {
                        // Not sinking or moving vertically = floating
                        event.getPlayer().flag(net.swofty.anticheat.flag.FlagType.JESUS, 0.75);
                    } else if (yChange > 0 && Math.abs(vel.y()) < 0.05) {
                        // Rising without proper swim motion
                        event.getPlayer().flag(net.swofty.anticheat.flag.FlagType.JESUS, 0.8);
                    }
                }

                // Additional check: Moving too fast in water
                double horizontalSpeed = Math.sqrt(vel.x() * vel.x() + vel.z() * vel.z());

                // Normal swim speed is ~0.08 blocks/tick
                // Anything over 0.15 in water is suspicious
                if (horizontalSpeed > 0.15) {
                    double certainty = Math.min(0.9, 0.6 + (horizontalSpeed - 0.15) * 3);
                    event.getPlayer().flag(net.swofty.anticheat.flag.FlagType.JESUS, certainty);
                }
            }
        });
    }

    private boolean isLiquid(Block block) {
        // Check if block is water or lava
        // This would need to be implemented based on actual Block class
        // For now, we'll assume a method exists or should be added
        return block != null; // Placeholder - should check block type
    }
}
