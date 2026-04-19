package net.swofty.type.generic.redis.service;

import net.swofty.commons.protocol.ServicePushProtocol;
import net.swofty.commons.protocol.objects.messaging.SendMessagePushProtocol;
import net.swofty.commons.protocol.objects.messaging.SendMessagePushProtocol.Request;
import net.swofty.commons.protocol.objects.messaging.SendMessagePushProtocol.Response;
import net.swofty.proxyapi.redis.TypedServiceHandler;
import net.swofty.type.generic.HypixelGenericLoader;
import net.swofty.type.generic.user.HypixelPlayer;

public class TypedSendMessageHandler implements TypedServiceHandler<Request, Response> {

    private static final SendMessagePushProtocol PROTOCOL = new SendMessagePushProtocol();

    @Override
    public ServicePushProtocol<Request, Response> getProtocol() {
        return PROTOCOL;
    }

    @Override
    public Response onMessage(Request message) {
        try {
            HypixelPlayer player = HypixelGenericLoader.getLoadedPlayers().stream()
                    .filter(p -> p.getUuid().equals(message.playerUUID()))
                    .findFirst()
                    .orElse(null);

            if (player != null) {
                player.sendMessage(message.message());
                return new Response(true);
            }

            return new Response(false);
        } catch (Exception e) {
            return new Response(false);
        }
    }
}
