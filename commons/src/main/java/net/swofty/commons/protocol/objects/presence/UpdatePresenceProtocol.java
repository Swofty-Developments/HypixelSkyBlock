package net.swofty.commons.protocol.objects.presence;

import net.swofty.commons.presence.PresenceInfo;
import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.RedisProtocol;
import net.swofty.commons.protocol.Serializer;
import org.jetbrains.annotations.Nullable;

public class UpdatePresenceProtocol extends RedisProtocol<
        UpdatePresenceProtocol.UpdatePresenceMessage,
        UpdatePresenceProtocol.UpdatePresenceResponse> {
    private static final Serializer<UpdatePresenceMessage> SERIALIZER =
            new JacksonSerializer<>(UpdatePresenceMessage.class);
    private static final Serializer<UpdatePresenceResponse> RETURN_SERIALIZER =
            new JacksonSerializer<>(UpdatePresenceResponse.class);

    @Override
    public Serializer<UpdatePresenceMessage> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public Serializer<UpdatePresenceResponse> getReturnSerializer() {
        return RETURN_SERIALIZER;
    }

    public record UpdatePresenceMessage(PresenceInfo presence) {}

    public record UpdatePresenceResponse(boolean success, @Nullable String error) {}
}
