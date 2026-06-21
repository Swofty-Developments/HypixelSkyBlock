package net.swofty.commons.protocol.objects.proxy.to;

import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.RedisProtocol;
import net.swofty.commons.protocol.Serializer;

public class TestFlowServerReadyProtocol extends RedisProtocol<
        TestFlowServerReadyProtocol.Request,
        TestFlowServerReadyProtocol.Response> {
    private static final Serializer<Request> SERIALIZER =
            new JacksonSerializer<>(Request.class);
    private static final Serializer<Response> RETURN_SERIALIZER =
            new JacksonSerializer<>(Response.class);

    @Override
    public Serializer<Request> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public Serializer<Response> getReturnSerializer() {
        return RETURN_SERIALIZER;
    }

    public record Request(String testFlowName, String serverType, int serverIndex) {}
    public record Response(boolean success, String message, String error) {}
}
