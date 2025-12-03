package net.swofty.anticheat.event.packet;

import lombok.Getter;
import net.swofty.anticheat.engine.SwoftyPlayer;

@Getter
public class PingResponsePacket extends SwoftyPacket {
    private final long requestId;

    public PingResponsePacket(SwoftyPlayer player, long requestId) {
        super(player);

        this.requestId = requestId;
    }
}
