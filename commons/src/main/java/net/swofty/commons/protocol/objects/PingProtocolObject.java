package net.swofty.commons.protocol.objects;

import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;
import net.swofty.commons.protocol.objects.bazaar.BazaarSellProtocolObject;

public class PingProtocolObject extends ProtocolObject<
        PingProtocolObject.EmptyMessage,
        PingProtocolObject.EmptyMessage> {

    @Override
    public Serializer<EmptyMessage> getSerializer() {
        return new Serializer<EmptyMessage>() {
            @Override
            public String serialize(EmptyMessage value) {
                return "";
            }

            @Override
            public EmptyMessage deserialize(String json) {
                return new EmptyMessage();
            }

            @Override
            public EmptyMessage clone(EmptyMessage value) {
                return new EmptyMessage();
            }
        };
    }

    @Override
    public Serializer<EmptyMessage> getReturnSerializer() {
        return new Serializer<EmptyMessage>() {
            @Override
            public String serialize(EmptyMessage value) {
                return "";
            }

            @Override
            public EmptyMessage deserialize(String json) {
                return new EmptyMessage();
            }

            @Override
            public EmptyMessage clone(EmptyMessage value) {
                return new EmptyMessage();
            }
        };
    }

    public record EmptyMessage() {}
}
