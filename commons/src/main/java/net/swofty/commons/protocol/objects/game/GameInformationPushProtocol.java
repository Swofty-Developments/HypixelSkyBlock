package net.swofty.commons.protocol.objects.game;

import net.swofty.commons.protocol.ServicePushProtocol;

import java.util.UUID;

public class GameInformationPushProtocol
        extends ServicePushProtocol<GameInformationPushProtocol.Request, GameInformationPushProtocol.Response> {

    public GameInformationPushProtocol() {
        super(Request.class, Response.class);
    }

    public record Request(UUID uuid, String gameId) {}

    public record Response() {}
}
