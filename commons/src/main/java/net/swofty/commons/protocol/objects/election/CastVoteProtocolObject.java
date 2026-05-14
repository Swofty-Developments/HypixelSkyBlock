package net.swofty.commons.protocol.objects.election;

import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;

import java.util.Map;
import java.util.UUID;
import org.jetbrains.annotations.Nullable;

public class CastVoteProtocolObject
        extends ProtocolObject<CastVoteProtocolObject.CastVoteMessage,
        CastVoteProtocolObject.CastVoteResponse> {
    private static final Serializer<CastVoteMessage> SERIALIZER =
            new JacksonSerializer<>(CastVoteMessage.class);
    private static final Serializer<CastVoteResponse> RETURN_SERIALIZER =
            new JacksonSerializer<>(CastVoteResponse.class);

    @Override
    public Serializer<CastVoteMessage> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public Serializer<CastVoteResponse> getReturnSerializer() {
        return RETURN_SERIALIZER;
    }

    public record CastVoteMessage(UUID accountId, String candidateName) {}

    public record CastVoteResponse(boolean success, Map<String, Long> tallies, @Nullable String error) {}
}
