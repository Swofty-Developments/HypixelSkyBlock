package net.swofty.commons.protocol.objects.friend;

import net.swofty.commons.friend.FriendEvent;
import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;
import org.jetbrains.annotations.Nullable;

public class SendFriendEventToServiceProtocolObject extends ProtocolObject
        <SendFriendEventToServiceProtocolObject.SendFriendEventToServiceMessage,
                SendFriendEventToServiceProtocolObject.SendFriendEventToServiceResponse> {

    @Override
    public Serializer<SendFriendEventToServiceMessage> getSerializer() {
        return new JacksonSerializer<>(SendFriendEventToServiceMessage.class);
    }

    @Override
    public Serializer<SendFriendEventToServiceResponse> getReturnSerializer() {
        return new JacksonSerializer<>(SendFriendEventToServiceResponse.class);
    }

    public record SendFriendEventToServiceMessage(FriendEvent event) {
    }

    public record SendFriendEventToServiceResponse(boolean success, @Nullable String error) {
    }
}
