package net.swofty.commons.protocol.objects.election;

import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;

import java.util.UUID;
import org.jetbrains.annotations.Nullable;

public class GetPlayerVoteProtocolObject
        extends ProtocolObject<GetPlayerVoteProtocolObject.GetPlayerVoteMessage,
        GetPlayerVoteProtocolObject.GetPlayerVoteResponse> {

    @Override
    public Serializer<GetPlayerVoteMessage> getSerializer() {
        return new JacksonSerializer<>(GetPlayerVoteMessage.class);
    }

    @Override
    public Serializer<GetPlayerVoteResponse> getReturnSerializer() {
        return new JacksonSerializer<>(GetPlayerVoteResponse.class);
    }

    public record GetPlayerVoteMessage(UUID accountId) {}

    public record GetPlayerVoteResponse(String candidateName, boolean success, @Nullable String error) {}
}
