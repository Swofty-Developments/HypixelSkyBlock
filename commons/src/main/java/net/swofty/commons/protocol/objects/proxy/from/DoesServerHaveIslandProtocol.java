package net.swofty.commons.protocol.objects.proxy.from;

import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;

public class DoesServerHaveIslandProtocol extends ProtocolObject<
        DoesServerHaveIslandProtocol.Request,
        DoesServerHaveIslandProtocol.Response> {

    @Override
    public Serializer<Request> getSerializer() {
        return new JacksonSerializer<>(Request.class);
    }

    @Override
    public Serializer<Response> getReturnSerializer() {
        return new JacksonSerializer<>(Response.class);
    }

    public record Request(String islandUuid) {}
    public record Response(boolean serverHasIt) {}
}
