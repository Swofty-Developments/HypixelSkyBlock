package net.swofty.commons.protocol.objects.proxy.to;

import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;
import org.jetbrains.annotations.Nullable;

public class PlayerCountProtocol extends ProtocolObject<
        PlayerCountProtocol.Request,
        PlayerCountProtocol.Response> {

    @Override
    public Serializer<Request> getSerializer() {
        return new JacksonSerializer<>(Request.class);
    }

    @Override
    public Serializer<Response> getReturnSerializer() {
        return new JacksonSerializer<>(Response.class);
    }

    public enum LookupType { ALL, TYPE, UUID }

    public record Request(LookupType lookupType, String lookupValue) {}
    public record Response(int playerCount, boolean success, @Nullable String error) {}
}
