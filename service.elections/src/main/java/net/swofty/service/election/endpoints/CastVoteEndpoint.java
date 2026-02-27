package net.swofty.service.election.endpoints;

import com.google.gson.Gson;
import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.objects.election.CastVoteProtocolObject;
import net.swofty.service.election.ElectionDatabase;
import net.swofty.service.generic.redis.ServiceEndpoint;
import org.tinylog.Logger;

import java.util.List;
import java.util.Map;

public class CastVoteEndpoint implements ServiceEndpoint
        <CastVoteProtocolObject.CastVoteMessage,
                CastVoteProtocolObject.CastVoteResponse> {

    private static final Gson GSON = new Gson();

    @Override
    public ProtocolObject<CastVoteProtocolObject.CastVoteMessage,
            CastVoteProtocolObject.CastVoteResponse> associatedProtocolObject() {
        return new CastVoteProtocolObject();
    }

    @Override
    @SuppressWarnings("unchecked")
    public CastVoteProtocolObject.CastVoteResponse onMessage(
            ServiceProxyRequest message,
            CastVoteProtocolObject.CastVoteMessage messageObject) {
        try {
            String rawData = ElectionDatabase.loadElectionData();
            if (rawData == null) {
                return new CastVoteProtocolObject.CastVoteResponse(false, null);
            }

            Map<String, Object> data = GSON.fromJson(rawData, Map.class);
            Boolean electionOpen = (Boolean) data.get("electionOpen");
            if (electionOpen == null || !electionOpen) {
                return new CastVoteProtocolObject.CastVoteResponse(false, null);
            }

            int electionYear = ((Number) data.get("electionYear")).intValue();

            List<Map<String, Object>> candidates = (List<Map<String, Object>>) data.get("candidates");
            if (candidates == null) {
                return new CastVoteProtocolObject.CastVoteResponse(false, null);
            }

            boolean validCandidate = candidates.stream()
                    .anyMatch(c -> messageObject.candidateName().equals(c.get("mayorName")));
            if (!validCandidate) {
                return new CastVoteProtocolObject.CastVoteResponse(false, null);
            }

            ElectionDatabase.castVote(
                    messageObject.accountId().toString(),
                    messageObject.candidateName(),
                    electionYear
            );

            Map<String, Long> tallies = ElectionDatabase.getTallies(electionYear);
            return new CastVoteProtocolObject.CastVoteResponse(true, GSON.toJson(tallies));
        } catch (Exception e) {
            Logger.error(e, "Failed to cast vote");
            return new CastVoteProtocolObject.CastVoteResponse(false, null);
        }
    }
}
