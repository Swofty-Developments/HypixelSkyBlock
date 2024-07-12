package net.swofty.anticheat.event.packet;

import lombok.Getter;
import net.swofty.anticheat.engine.SwoftyPlayer;

@Getter
public class RequestPingPacket extends SwoftyPacket {
    private final int requestId;

    public RequestPingPacket(SwoftyPlayer player, int requestId) {
        super(player);

        this.requestId = requestId;
    }
}
