package net.swofty.anticheat.event.packet;

import lombok.Getter;

import java.util.UUID;

@Getter
public class SteerVehiclePacket extends SwoftyPacket {
    private final float sideways;
    private final float forward;
    private final boolean jump;
    private final boolean unmount;

    public SteerVehiclePacket(UUID uuid, float sideways, float forward, boolean jump, boolean unmount) {
        super(uuid);
        this.sideways = sideways;
        this.forward = forward;
        this.jump = jump;
        this.unmount = unmount;
    }
}
