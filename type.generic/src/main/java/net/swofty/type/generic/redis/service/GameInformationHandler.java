package net.swofty.type.generic.redis.service;

import net.swofty.commons.protocol.RedisProtocol;
import net.swofty.commons.protocol.objects.game.GameInformationPushProtocol;
import net.swofty.commons.protocol.objects.game.GameInformationPushProtocol.Request;
import net.swofty.commons.protocol.objects.game.GameInformationPushProtocol.Response;
import net.swofty.commons.redis.RedisMessageHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.swofty.commons.redis.RedisMessageContext;

public class GameInformationHandler implements RedisMessageHandler<Request, Response> {

    public static Map<UUID, String> game = new HashMap<>();

    private static final GameInformationPushProtocol PROTOCOL = new GameInformationPushProtocol();

    @Override
    public RedisProtocol<Request, Response> protocol() {
        return PROTOCOL;
    }

    @Override
    public Response handle(Request message, RedisMessageContext context) {
        game.put(message.uuid(), message.gameId());
        return new Response();
    }
}
