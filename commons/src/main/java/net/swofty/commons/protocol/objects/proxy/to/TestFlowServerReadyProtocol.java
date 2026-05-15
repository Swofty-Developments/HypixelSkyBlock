package net.swofty.commons.protocol.objects.proxy.to;

import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;

public class TestFlowServerReadyProtocol extends ProtocolObject<
        TestFlowServerReadyProtocol.Request,
        TestFlowServerReadyProtocol.Response> {

    @Override
    public Serializer<Request> getSerializer() {
        return new JacksonSerializer<>(Request.class);
    }

    @Override
    public Serializer<Response> getReturnSerializer() {
        return new JacksonSerializer<>(Response.class);
    }

    public record Request(String testFlowName, String serverType, int serverIndex) {}
    public record Response(boolean success, String message, String error) {}
}
