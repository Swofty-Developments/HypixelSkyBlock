package net.swofty.anticheat.event.packet;

import net.swofty.anticheat.engine.SwoftyPlayer;

public class RotationPacket extends SwoftyPacket {
    private float yaw;
    private float pitch;
    private boolean onGround;

    public RotationPacket(SwoftyPlayer player, float yaw, float pitch, boolean onGround) {
        super(player);
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public boolean isOnGround() {
        return onGround;
    }
}
