package net.swofty.commons.protocol.objects.presence;

import net.swofty.commons.presence.PresenceInfo;
import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;

import java.util.List;
import java.util.UUID;

public class GetPresenceBulkProtocolObject extends ProtocolObject<
        GetPresenceBulkProtocolObject.GetPresenceBulkMessage,
        GetPresenceBulkProtocolObject.GetPresenceBulkResponse> {

    @Override
    public Serializer<GetPresenceBulkMessage> getSerializer() {
        return new JacksonSerializer<>(GetPresenceBulkMessage.class);
    }

    @Override
    public Serializer<GetPresenceBulkResponse> getReturnSerializer() {
        return new JacksonSerializer<>(GetPresenceBulkResponse.class);
    }

    public record GetPresenceBulkMessage(List<UUID> uuids) {}

    public record GetPresenceBulkResponse(List<PresenceInfo> presence) {}
}
