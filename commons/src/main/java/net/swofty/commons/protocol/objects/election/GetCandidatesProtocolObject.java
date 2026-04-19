package net.swofty.commons.protocol.objects.election;

import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;

import java.util.List;

public class GetCandidatesProtocolObject
        extends ProtocolObject<GetCandidatesProtocolObject.GetCandidatesMessage,
        GetCandidatesProtocolObject.GetCandidatesResponse> {

    @Override
    public Serializer<GetCandidatesMessage> getSerializer() {
        return new JacksonSerializer<>(GetCandidatesMessage.class);
    }

    @Override
    public Serializer<GetCandidatesResponse> getReturnSerializer() {
        return new JacksonSerializer<>(GetCandidatesResponse.class);
    }

    public record GetCandidatesMessage() {}

    public record GetCandidatesResponse(
            boolean electionOpen,
            List<CandidateInfo> candidates
    ) {}

    public record CandidateInfo(
            String mayorName,
            List<String> activePerks,
            long votes,
            double votePercentage
    ) {}
}
