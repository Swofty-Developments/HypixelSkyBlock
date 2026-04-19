package net.swofty.commons.protocol.objects.proxy.to;

import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;

import java.util.List;
import java.util.Map;

public class RegisterTestFlowProtocol extends ProtocolObject<
        RegisterTestFlowProtocol.Request,
        RegisterTestFlowProtocol.Response> {

    @Override
    public Serializer<Request> getSerializer() {
        return new JacksonSerializer<>(Request.class);
    }

    @Override
    public Serializer<Response> getReturnSerializer() {
        return new JacksonSerializer<>(Response.class);
    }

    public record Request(String testFlowName, String handler, List<String> players,
                          List<Map<String, Object>> serverConfigs) {}
    public record Response(boolean success, String message, String error) {}
}
