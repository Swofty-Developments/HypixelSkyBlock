package net.swofty.anticheat.event.packet;

import net.swofty.anticheat.engine.SwoftyPlayer;

public class IsOnGroundPacket extends SwoftyPacket {
    private boolean onGround;

    public IsOnGroundPacket(SwoftyPlayer player, boolean onGround) {
        super(player);
        this.onGround = onGround;
    }

    public boolean isOnGround() {
        return onGround;
    }
}
