package net.swofty.anticheat.event.packet;

import lombok.Getter;
import net.swofty.anticheat.engine.SwoftyPlayer;

@Getter
public class IsOnGroundPacket extends SwoftyPacket {
    private final boolean onGround;

    public IsOnGroundPacket(SwoftyPlayer player, boolean onGround) {
        super(player);
        this.onGround = onGround;
    }

}
