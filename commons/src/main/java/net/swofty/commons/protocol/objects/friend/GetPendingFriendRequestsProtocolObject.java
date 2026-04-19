package net.swofty.commons.protocol.objects.friend;

import net.swofty.commons.friend.PendingFriendRequest;
import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;

import java.util.List;
import java.util.UUID;

public class GetPendingFriendRequestsProtocolObject extends ProtocolObject
        <GetPendingFriendRequestsProtocolObject.GetPendingRequestsMessage,
                GetPendingFriendRequestsProtocolObject.GetPendingRequestsResponse> {

    @Override
    public Serializer<GetPendingRequestsMessage> getSerializer() {
        return new JacksonSerializer<>(GetPendingRequestsMessage.class);
    }

    @Override
    public Serializer<GetPendingRequestsResponse> getReturnSerializer() {
        return new JacksonSerializer<>(GetPendingRequestsResponse.class);
    }

    public record GetPendingRequestsMessage(UUID playerUuid) {
    }

    public record GetPendingRequestsResponse(List<PendingFriendRequest> requests) {
    }
}
