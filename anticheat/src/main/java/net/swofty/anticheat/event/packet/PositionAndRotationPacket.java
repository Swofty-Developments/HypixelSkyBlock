package net.swofty.anticheat.event.packet;

import net.swofty.anticheat.engine.SwoftyPlayer;
import net.swofty.anticheat.math.Pos;

public class PositionAndRotationPacket extends SwoftyPacket {
    private Pos pos;
    private boolean onGround;

    public PositionAndRotationPacket(SwoftyPlayer player, Pos pos, boolean onGround) {
        super(player);
        this.pos = pos;
        this.onGround = onGround;
    }

    public Pos getPos() {
        return pos;
    }

    public boolean isOnGround() {
        return onGround;
    }
}
