package net.swofty.anticheat.flag.flags;

import net.swofty.anticheat.event.ListenerMethod;
import net.swofty.anticheat.event.events.PlayerPositionUpdateEvent;
import net.swofty.anticheat.flag.Flag;
import net.swofty.anticheat.math.Pos;
import net.swofty.anticheat.math.Vel;

public class OnGroundSpoofFlag extends Flag {
    @ListenerMethod
    public void onPlayerPositionUpdate(PlayerPositionUpdateEvent event) {
        // Disabled: heuristic-based detection has too many false positives
        // Needs actual collision checking against world blocks to be accurate
    }
}
