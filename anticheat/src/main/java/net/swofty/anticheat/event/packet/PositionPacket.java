package net.swofty.anticheat.event.packet;

import net.swofty.anticheat.engine.SwoftyPlayer;
import org.jetbrains.annotations.NotNull;

public class PositionPacket extends SwoftyPacket {
    private double x;
    private double y;
    private double z;
    private boolean onGround;

    public PositionPacket(@NotNull SwoftyPlayer player, double x, double y, double z, boolean onGround) {
        super(player);
        this.x = x;
        this.y = y;
        this.z = z;
        this.onGround = onGround;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public boolean isOnGround() {
        return onGround;
    }
}
