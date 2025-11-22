package net.swofty.anticheat.flag.flags;

import net.swofty.anticheat.event.ListenerMethod;
import net.swofty.anticheat.event.events.PlayerPositionUpdateEvent;
import net.swofty.anticheat.flag.Flag;
import net.swofty.anticheat.math.Pos;
import net.swofty.anticheat.math.Vel;

public class OnGroundSpoofFlag extends Flag {
    @ListenerMethod
    public void onPlayerPositionUpdate(PlayerPositionUpdateEvent event) {
        boolean packetOnGround = event.getCurrentTick().isOnGround();
        Vel currentVel = event.getCurrentTick().getVel();
        Pos currentPosition = event.getCurrentTick().getPos();

        // Determine if player is truly on ground by checking Y velocity and position
        boolean trueOnGround = false;

        // Pattern 1: Y velocity near zero indicates on ground
        if (Math.abs(currentVel.y()) < 0.01) {
            trueOnGround = true;
        }

        // Pattern 2: Check previous tick Y velocity pattern
        if (event.getPreviousTick() != null) {
            Vel previousVel = event.getPreviousTick().getVel();

            // If Y velocity is consistent with being on ground (not falling/jumping)
            if (Math.abs(currentVel.y()) < 0.1 && Math.abs(previousVel.y()) < 0.5) {
                trueOnGround = true;
            }

            // If player was falling and suddenly stopped, they're on ground
            if (previousVel.y() < -0.1 && Math.abs(currentVel.y()) < 0.01) {
                trueOnGround = true;
            }
        }

        // Pattern 3: Check if Y position is on a block boundary
        double yFraction = currentPosition.y() - Math.floor(currentPosition.y());
        if (yFraction < 0.01 || yFraction > 0.99) {
            // Y position suggests standing on block
            trueOnGround = true;
        }

        // Now compare packet claim vs reality
        if (packetOnGround && !trueOnGround) {
            // Client claims on ground but isn't
            // This is used by some fly hacks to avoid fall damage
            double certainty = 0.75;

            // Higher certainty if player is high in the air
            if (Math.abs(currentVel.y()) > 0.3) {
                certainty = 0.9;
            }

            event.getPlayer().flag(net.swofty.anticheat.flag.FlagType.ON_GROUND_SPOOF, certainty);
        } else if (!packetOnGround && trueOnGround) {
            // Client claims not on ground but is
            // Less severe but still suspicious (could be used for some exploits)
            event.getPlayer().flag(net.swofty.anticheat.flag.FlagType.ON_GROUND_SPOOF, 0.6);
        }
    }
}
