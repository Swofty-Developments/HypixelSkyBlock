package net.swofty.commons.protocol.objects.election;

import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;

import java.util.UUID;
import org.jetbrains.annotations.Nullable;

public class GetPlayerVoteProtocolObject
        extends ProtocolObject<GetPlayerVoteProtocolObject.GetPlayerVoteMessage,
        GetPlayerVoteProtocolObject.GetPlayerVoteResponse> {
    private static final Serializer<GetPlayerVoteMessage> SERIALIZER =
            new JacksonSerializer<>(GetPlayerVoteMessage.class);
    private static final Serializer<GetPlayerVoteResponse> RETURN_SERIALIZER =
            new JacksonSerializer<>(GetPlayerVoteResponse.class);

    @Override
    public Serializer<GetPlayerVoteMessage> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public Serializer<GetPlayerVoteResponse> getReturnSerializer() {
        return RETURN_SERIALIZER;
    }

    public record GetPlayerVoteMessage(UUID accountId) {}

    public record GetPlayerVoteResponse(String candidateName, boolean success, @Nullable String error) {}
}
