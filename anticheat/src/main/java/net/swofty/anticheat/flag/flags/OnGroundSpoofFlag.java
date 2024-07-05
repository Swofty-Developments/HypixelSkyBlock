package net.swofty.anticheat.flag.flags;

import net.swofty.anticheat.event.ListenerMethod;
import net.swofty.anticheat.event.events.PlayerPositionUpdateEvent;
import net.swofty.anticheat.flag.Flag;
import net.swofty.anticheat.math.Pos;

public class OnGroundSpoofFlag extends Flag {
    @ListenerMethod
    public void onPlayerPositionUpdate(PlayerPositionUpdateEvent event) {
        boolean packetOnGround = event.getCurrentTick().isOnGround();
        boolean trueOnGround = false;

        Pos currentPosition = event.getCurrentTick().getPos();
    }
}
