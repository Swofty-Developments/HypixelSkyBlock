package net.swofty.anticheat.event.packet;

import lombok.Getter;
import net.swofty.anticheat.engine.SwoftyPlayer;
import org.jetbrains.annotations.NotNull;

@Getter
public class PositionPacket extends SwoftyPacket {
    private final double x;
    private final double y;
    private final double z;
    private final boolean onGround;

    public PositionPacket(@NotNull SwoftyPlayer player, double x, double y, double z, boolean onGround) {
        super(player);
        this.x = x;
        this.y = y;
        this.z = z;
        this.onGround = onGround;
    }
}
