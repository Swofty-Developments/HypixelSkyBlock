package net.swofty.commons.protocol.objects;

import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;

public class PingProtocolObject extends ProtocolObject<
        PingProtocolObject.EmptyMessage,
        PingProtocolObject.EmptyMessage> {
    private static final Serializer<EmptyMessage> SERIALIZER =
            new JacksonSerializer<>(EmptyMessage.class);
    private static final Serializer<EmptyMessage> RETURN_SERIALIZER =
            new JacksonSerializer<>(EmptyMessage.class);

    @Override
    public Serializer<EmptyMessage> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public Serializer<EmptyMessage> getReturnSerializer() {
        return RETURN_SERIALIZER;
    }

    public record EmptyMessage() {}
}
