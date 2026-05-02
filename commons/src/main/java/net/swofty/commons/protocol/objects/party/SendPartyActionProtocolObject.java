package net.swofty.commons.protocol.objects.party;

import net.swofty.commons.party.PartyAction;
import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;
import org.jetbrains.annotations.Nullable;

public class SendPartyActionProtocolObject extends ProtocolObject<
        SendPartyActionProtocolObject.Request,
        SendPartyActionProtocolObject.Response> {

    @Override
    public Serializer<Request> getSerializer() {
        return new JacksonSerializer<>(Request.class);
    }

    @Override
    public Serializer<Response> getReturnSerializer() {
        return new JacksonSerializer<>(Response.class);
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
