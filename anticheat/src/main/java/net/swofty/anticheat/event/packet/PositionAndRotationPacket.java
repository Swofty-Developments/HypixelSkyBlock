package net.swofty.anticheat.event.packet;

import lombok.Getter;
import net.swofty.anticheat.engine.SwoftyPlayer;
import net.swofty.anticheat.math.Pos;

@Getter
public class PositionAndRotationPacket extends SwoftyPacket {
    private final Pos pos;
    private final boolean onGround;

    public PositionAndRotationPacket(SwoftyPlayer player, Pos pos, boolean onGround) {
        super(player);
        this.pos = pos;
        this.onGround = onGround;
    }

}
