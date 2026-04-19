package net.swofty.commons.protocol.objects.friend;

import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;

import java.util.UUID;
import org.jetbrains.annotations.Nullable;

public class AreFriendsProtocolObject extends ProtocolObject
        <AreFriendsProtocolObject.AreFriendsMessage,
                AreFriendsProtocolObject.AreFriendsResponse> {

    @Override
    public Serializer<AreFriendsMessage> getSerializer() {
        return new JacksonSerializer<>(AreFriendsMessage.class);
    }

    @Override
    public Serializer<AreFriendsResponse> getReturnSerializer() {
        return new JacksonSerializer<>(AreFriendsResponse.class);
    }

    public record AreFriendsMessage(UUID player1, UUID player2) {
    }

    public record AreFriendsResponse(boolean areFriends, boolean success, @Nullable String error) {
    }
}
