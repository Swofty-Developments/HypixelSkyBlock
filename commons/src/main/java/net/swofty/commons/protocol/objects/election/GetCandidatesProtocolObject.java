package net.swofty.commons.protocol.objects.election;

import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;

import java.util.List;
import org.jetbrains.annotations.Nullable;

public class GetCandidatesProtocolObject
        extends ProtocolObject<GetCandidatesProtocolObject.GetCandidatesMessage,
        GetCandidatesProtocolObject.GetCandidatesResponse> {
    private static final Serializer<GetCandidatesMessage> SERIALIZER =
            new JacksonSerializer<>(GetCandidatesMessage.class);
    private static final Serializer<GetCandidatesResponse> RETURN_SERIALIZER =
            new JacksonSerializer<>(GetCandidatesResponse.class);

    @Override
    public Serializer<GetCandidatesMessage> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public Serializer<GetCandidatesResponse> getReturnSerializer() {
        return RETURN_SERIALIZER;
    }

    public record GetCandidatesMessage() {}

    public record GetCandidatesResponse(
            boolean electionOpen,
            List<CandidateInfo> candidates,
            boolean success,
            @Nullable String error
    ) {}

    public record CandidateInfo(
            String mayorName,
            List<String> activePerks,
            long votes,
            double votePercentage
    ) {}
}
