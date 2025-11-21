package net.swofty.anticheat.event.packet;

import lombok.Getter;
import net.swofty.anticheat.engine.SwoftyPlayer;

@Getter
public class RotationPacket extends SwoftyPacket {
    private final float yaw;
    private final float pitch;
    private final boolean onGround;

    public RotationPacket(SwoftyPlayer player, float yaw, float pitch, boolean onGround) {
        super(player);
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
    }

}
