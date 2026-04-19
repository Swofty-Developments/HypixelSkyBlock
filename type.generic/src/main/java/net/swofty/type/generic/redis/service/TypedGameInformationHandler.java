package net.swofty.type.generic.redis.service;

import net.swofty.commons.protocol.ServicePushProtocol;
import net.swofty.commons.protocol.objects.game.GameInformationPushProtocol;
import net.swofty.commons.protocol.objects.game.GameInformationPushProtocol.Request;
import net.swofty.commons.protocol.objects.game.GameInformationPushProtocol.Response;
import net.swofty.proxyapi.redis.TypedServiceHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TypedGameInformationHandler implements TypedServiceHandler<Request, Response> {

    public static Map<UUID, String> game = new HashMap<>();

    private static final GameInformationPushProtocol PROTOCOL = new GameInformationPushProtocol();

    @Override
    public ServicePushProtocol<Request, Response> getProtocol() {
        return PROTOCOL;
    }

    @Override
    public Response onMessage(Request message) {
        game.put(message.uuid(), message.gameId());
        return new Response();
    }
}
