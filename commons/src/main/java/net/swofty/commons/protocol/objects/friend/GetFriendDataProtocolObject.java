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

    @Override
    public Serializer<GetFriendDataMessage> getSerializer() {
        return new JacksonSerializer<>(GetFriendDataMessage.class);
    }

    @Override
    public Serializer<GetFriendDataResponse> getReturnSerializer() {
        return new JacksonSerializer<>(GetFriendDataResponse.class);
    }

    public record GetFriendDataMessage(UUID playerUuid) {
    }

    public record GetFriendDataResponse(FriendData friendData, boolean success, @Nullable String error) {
    }
}
