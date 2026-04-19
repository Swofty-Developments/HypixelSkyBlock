package net.swofty.commons.protocol.objects.proxy.from;

import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;

public class GivePlayersOriginTypeProtocol extends ProtocolObject<
        GivePlayersOriginTypeProtocol.Request,
        GivePlayersOriginTypeProtocol.Response> {

    @Override
    public Serializer<Request> getSerializer() {
        return new JacksonSerializer<>(Request.class);
    }

    @Override
    public Serializer<Response> getReturnSerializer() {
        return new JacksonSerializer<>(Response.class);
    }

    public record Request(String uuid, String originType) {}
    public record Response() {}
}
