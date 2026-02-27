package net.swofty.service.election.endpoints;

import com.google.gson.Gson;
import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.objects.election.GetCandidatesProtocolObject;
import net.swofty.service.election.ElectionDatabase;
import net.swofty.service.generic.redis.ServiceEndpoint;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GetCandidatesEndpoint implements ServiceEndpoint
        <GetCandidatesProtocolObject.GetCandidatesMessage,
                GetCandidatesProtocolObject.GetCandidatesResponse> {

    private static final Gson GSON = new Gson();

    @Override
    public ProtocolObject<GetCandidatesProtocolObject.GetCandidatesMessage,
            GetCandidatesProtocolObject.GetCandidatesResponse> associatedProtocolObject() {
        return new GetCandidatesProtocolObject();
    }

    @Override
    @SuppressWarnings("unchecked")
    public GetCandidatesProtocolObject.GetCandidatesResponse onMessage(
            ServiceProxyRequest message,
            GetCandidatesProtocolObject.GetCandidatesMessage messageObject) {
        try {
            String rawData = ElectionDatabase.loadElectionData();
            if (rawData == null) {
                return new GetCandidatesProtocolObject.GetCandidatesResponse(false, List.of());
            }

            Map<String, Object> data = GSON.fromJson(rawData, Map.class);
            Boolean electionOpen = (Boolean) data.get("electionOpen");
            if (electionOpen == null || !electionOpen) {
                return new GetCandidatesProtocolObject.GetCandidatesResponse(false, List.of());
            }

            int electionYear = ((Number) data.get("electionYear")).intValue();

            List<Map<String, Object>> candidates = (List<Map<String, Object>>) data.get("candidates");
            if (candidates == null || candidates.isEmpty()) {
                return new GetCandidatesProtocolObject.GetCandidatesResponse(true, List.of());
            }

            Map<String, Long> tallies = ElectionDatabase.getTallies(electionYear);
            long totalVotes = tallies.values().stream().mapToLong(Long::longValue).sum();

            List<GetCandidatesProtocolObject.CandidateInfo> infos = new ArrayList<>();
            for (Map<String, Object> c : candidates) {
                String name = (String) c.get("mayorName");
                List<String> perks = (List<String>) c.get("activePerks");
                if (perks == null) perks = List.of();
                long voteCount = tallies.getOrDefault(name, 0L);
                double pct = totalVotes > 0 ? (voteCount * 100.0) / totalVotes : 0;
                infos.add(new GetCandidatesProtocolObject.CandidateInfo(name, perks, voteCount, pct));
            }

            return new GetCandidatesProtocolObject.GetCandidatesResponse(true, infos);
        } catch (Exception e) {
            Logger.error(e, "Failed to get candidates");
            return new GetCandidatesProtocolObject.GetCandidatesResponse(false, List.of());
        }
    }
}
