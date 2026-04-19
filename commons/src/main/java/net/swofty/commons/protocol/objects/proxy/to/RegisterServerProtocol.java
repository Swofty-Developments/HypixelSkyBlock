package net.swofty.commons.protocol.objects.proxy.to;

import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;

public class RegisterServerProtocol extends ProtocolObject<
        RegisterServerProtocol.Request,
        RegisterServerProtocol.Response> {

    @Override
    public Serializer<Request> getSerializer() {
        return new JacksonSerializer<>(Request.class);
    }

    @Override
    public Serializer<Response> getReturnSerializer() {
        return new JacksonSerializer<>(Response.class);
    }

    public record Request(String type, int maxPlayers, String host, Integer port,
                          Boolean isTestFlow, String testFlowName, String testFlowIndex, String testFlowTotal) {}
    public record Response(String host, int port) {}
}
