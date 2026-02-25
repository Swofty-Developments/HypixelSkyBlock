package net.swofty.service.election.endpoints;

import com.google.gson.Gson;
import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.objects.election.CastVoteProtocolObject;
import net.swofty.service.election.ElectionDatabase;
import net.swofty.service.generic.redis.ServiceEndpoint;

import java.util.ArrayList;
import java.util.HashMap;
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

            List<Map<String, Object>> candidates = (List<Map<String, Object>>) data.get("candidates");
            if (candidates == null) candidates = new ArrayList<>();

            boolean validCandidate = candidates.stream()
                    .anyMatch(c -> messageObject.candidateName().equals(c.get("mayorName")));
            if (!validCandidate) {
                return new CastVoteProtocolObject.CastVoteResponse(false, null);
            }

            Map<String, String> votes = (Map<String, String>) data.get("votes");
            if (votes == null) {
                votes = new HashMap<>();
                data.put("votes", votes);
            }
            votes.put(messageObject.accountId().toString(), messageObject.candidateName());

            String updatedData = GSON.toJson(data);
            ElectionDatabase.saveElectionData(updatedData);

            return new CastVoteProtocolObject.CastVoteResponse(true, updatedData);
        } catch (Exception e) {
            return new CastVoteProtocolObject.CastVoteResponse(false, null);
        }
    }
}
