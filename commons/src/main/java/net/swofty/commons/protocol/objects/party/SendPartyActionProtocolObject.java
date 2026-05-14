package net.swofty.commons.protocol.objects.party;

import net.swofty.commons.party.PartyAction;
import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;
import org.jetbrains.annotations.Nullable;

public class SendPartyActionProtocolObject extends ProtocolObject<
        SendPartyActionProtocolObject.Request,
        SendPartyActionProtocolObject.Response> {
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

    public record Request(PartyAction action) {}

    public record Response(boolean success, @Nullable String error) {
        public static Response ok() {
            return new Response(true, null);
        }

        public static Response failure(String error) {
            return new Response(false, error);
        }
    }
}
