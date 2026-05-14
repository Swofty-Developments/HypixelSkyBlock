package net.swofty.commons.protocol.objects.friend;

import net.swofty.commons.friend.PendingFriendRequest;
import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;

import java.util.List;
import java.util.UUID;
import org.jetbrains.annotations.Nullable;

public class GetPendingFriendRequestsProtocolObject extends ProtocolObject
        <GetPendingFriendRequestsProtocolObject.GetPendingRequestsMessage,
                GetPendingFriendRequestsProtocolObject.GetPendingRequestsResponse> {
    private static final Serializer<GetPendingRequestsMessage> SERIALIZER =
            new JacksonSerializer<>(GetPendingRequestsMessage.class);
    private static final Serializer<GetPendingRequestsResponse> RETURN_SERIALIZER =
            new JacksonSerializer<>(GetPendingRequestsResponse.class);

    @Override

    public Serializer<GetPendingRequestsMessage> getSerializer() {

        return SERIALIZER;

    }

    @Override

    public Serializer<GetPendingRequestsResponse> getReturnSerializer() {

        return RETURN_SERIALIZER;

    }

    public record GetPendingRequestsMessage(UUID playerUuid) {
    }

    public record GetPendingRequestsResponse(List<PendingFriendRequest> requests, boolean success, @Nullable String error) {
    }
}
