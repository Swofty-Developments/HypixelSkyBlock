package net.swofty.commons.protocol.objects.proxy.to;

import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;

import java.util.Map;

public class PlayerHandlerProtocol extends ProtocolObject<
        PlayerHandlerProtocol.Request,
        PlayerHandlerProtocol.Response> {

    @Override
    public Serializer<Request> getSerializer() {
        return new JacksonSerializer<>(Request.class);
    }

    @Override
    public Serializer<Response> getReturnSerializer() {
        return new JacksonSerializer<>(Response.class);
    }

    public enum Action {
        TRANSFER,
        TELEPORT,
        BANK_HASH,
        VERSION,
        IS_ONLINE,
        EVENT,
        REFRESH_COOP_DATA,
        MESSAGE,
        TRANSFER_WITH_UUID,
        GET_SERVER,
        LIMBO
    }

    public record Request(String uuid, Action action, Map<String, Object> data) {}
    public record Response(Map<String, Object> data) {}
}
