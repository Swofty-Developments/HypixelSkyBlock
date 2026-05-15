package net.swofty.commons.protocol.objects;

import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;

public class PingProtocolObject extends ProtocolObject<
        PingProtocolObject.EmptyMessage,
        PingProtocolObject.EmptyMessage> {

    @Override
    public Serializer<EmptyMessage> getSerializer() {
        return new JacksonSerializer<>(EmptyMessage.class);
    }

    @Override
    public Serializer<EmptyMessage> getReturnSerializer() {
        return new JacksonSerializer<>(EmptyMessage.class);
    }

    public record EmptyMessage() {}
}
