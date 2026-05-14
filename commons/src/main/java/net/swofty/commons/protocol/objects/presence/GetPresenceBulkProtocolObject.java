package net.swofty.commons.protocol.objects.presence;

import net.swofty.commons.presence.PresenceInfo;
import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;

import java.util.List;
import java.util.UUID;
import org.jetbrains.annotations.Nullable;

public class GetPresenceBulkProtocolObject extends ProtocolObject<
        GetPresenceBulkProtocolObject.GetPresenceBulkMessage,
        GetPresenceBulkProtocolObject.GetPresenceBulkResponse> {
    private static final Serializer<GetPresenceBulkMessage> SERIALIZER =
            new JacksonSerializer<>(GetPresenceBulkMessage.class);
    private static final Serializer<GetPresenceBulkResponse> RETURN_SERIALIZER =
            new JacksonSerializer<>(GetPresenceBulkResponse.class);

    @Override
    public Serializer<GetPresenceBulkMessage> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public Serializer<GetPresenceBulkResponse> getReturnSerializer() {
        return RETURN_SERIALIZER;
    }

    public record GetPresenceBulkMessage(List<UUID> uuids) {}

    public record GetPresenceBulkResponse(List<PresenceInfo> presence, boolean success, @Nullable String error) {}
}
