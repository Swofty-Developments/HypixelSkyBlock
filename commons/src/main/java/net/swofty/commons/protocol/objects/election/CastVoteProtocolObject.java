package net.swofty.commons.protocol.objects.election;

import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;

import java.util.Map;
import java.util.UUID;

public class CastVoteProtocolObject
        extends ProtocolObject<CastVoteProtocolObject.CastVoteMessage,
        CastVoteProtocolObject.CastVoteResponse> {

    @Override
    public Serializer<CastVoteMessage> getSerializer() {
        return new JacksonSerializer<>(CastVoteMessage.class);
    }

    @Override
    public Serializer<CastVoteResponse> getReturnSerializer() {
        return new JacksonSerializer<>(CastVoteResponse.class);
    }

    public record CastVoteMessage(UUID accountId, String candidateName) {}

    public record CastVoteResponse(boolean success, Map<String, Long> tallies) {}
}
