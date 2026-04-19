package net.swofty.commons.protocol.objects.proxy.to;

import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;

public class PunishPlayerProtocol extends ProtocolObject<
        PunishPlayerProtocol.Request,
        PunishPlayerProtocol.Response> {

    @Override
    public Serializer<Request> getSerializer() {
        return new JacksonSerializer<>(Request.class);
    }

    @Override
    public Serializer<Response> getReturnSerializer() {
        return new JacksonSerializer<>(Response.class);
    }

    public record Request(String target, String type, String id, long expiresAt,
                          String reasonBan, String reasonMute, String tags) {}
    public record Response() {}
}
