package net.swofty.commons.protocol.objects.friend;

import net.swofty.commons.friend.FriendEvent;
import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.RedisProtocol;
import net.swofty.commons.protocol.Serializer;
import org.jetbrains.annotations.Nullable;

public class SendFriendEventToServiceProtocol extends RedisProtocol
        <SendFriendEventToServiceProtocol.SendFriendEventToServiceMessage,
                SendFriendEventToServiceProtocol.SendFriendEventToServiceResponse> {
    private static final Serializer<SendFriendEventToServiceMessage> SERIALIZER =
            new JacksonSerializer<>(SendFriendEventToServiceMessage.class);
    private static final Serializer<SendFriendEventToServiceResponse> RETURN_SERIALIZER =
            new JacksonSerializer<>(SendFriendEventToServiceResponse.class);

    @Override

    public Serializer<SendFriendEventToServiceMessage> getSerializer() {

        return SERIALIZER;

    }

    @Override

    public Serializer<SendFriendEventToServiceResponse> getReturnSerializer() {

        return RETURN_SERIALIZER;

    }

    public record SendFriendEventToServiceMessage(FriendEvent event) {
    }

    public record SendFriendEventToServiceResponse(boolean success, @Nullable String error) {
    }
}
