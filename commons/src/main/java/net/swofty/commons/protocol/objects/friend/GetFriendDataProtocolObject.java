package net.swofty.commons.protocol.objects.friend;

import net.swofty.commons.friend.FriendData;
import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;

import java.util.UUID;
import org.jetbrains.annotations.Nullable;

public class GetFriendDataProtocolObject extends ProtocolObject
        <GetFriendDataProtocolObject.GetFriendDataMessage,
                GetFriendDataProtocolObject.GetFriendDataResponse> {
    private static final Serializer<GetFriendDataMessage> SERIALIZER =
            new JacksonSerializer<>(GetFriendDataMessage.class);
    private static final Serializer<GetFriendDataResponse> RETURN_SERIALIZER =
            new JacksonSerializer<>(GetFriendDataResponse.class);

    @Override

    public Serializer<GetFriendDataMessage> getSerializer() {

        return SERIALIZER;

    }

    @Override

    public Serializer<GetFriendDataResponse> getReturnSerializer() {

        return RETURN_SERIALIZER;

    }

    public record GetFriendDataMessage(UUID playerUuid) {
    }

    public record GetFriendDataResponse(FriendData friendData, boolean success, @Nullable String error) {
    }
}
