package net.swofty.service.election.endpoints;

import com.google.gson.Gson;
import net.swofty.commons.protocol.RedisProtocol;
import net.swofty.commons.protocol.objects.election.GetCandidatesProtocol;
import net.swofty.service.election.ElectionDatabase;
import net.swofty.commons.redis.RedisMessageHandler;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.swofty.commons.redis.RedisMessageContext;

public class GetCandidatesEndpoint implements RedisMessageHandler
        <GetCandidatesProtocol.GetCandidatesMessage,
                GetCandidatesProtocol.GetCandidatesResponse> {

    private static final Gson GSON = new Gson();

    @Override
    public RedisProtocol<GetCandidatesProtocol.GetCandidatesMessage,
            GetCandidatesProtocol.GetCandidatesResponse> protocol() {
        return new GetCandidatesProtocol();
    }

    @Override
    @SuppressWarnings("unchecked")
    public GetCandidatesProtocol.GetCandidatesResponse handle(GetCandidatesProtocol.GetCandidatesMessage messageObject, RedisMessageContext context) {
        try {
            String rawData = ElectionDatabase.loadElectionData();
            if (rawData == null) {
                return new GetCandidatesProtocol.GetCandidatesResponse(false, List.of(), true, null);
            }

            Map<String, Object> data = GSON.fromJson(rawData, Map.class);
            Boolean electionOpen = (Boolean) data.get("electionOpen");
            if (electionOpen == null || !electionOpen) {
                return new GetCandidatesProtocol.GetCandidatesResponse(false, List.of(), true, null);
            }

            int electionYear = ((Number) data.get("electionYear")).intValue();

            List<Map<String, Object>> candidates = (List<Map<String, Object>>) data.get("candidates");
            if (candidates == null || candidates.isEmpty()) {
                return new GetCandidatesProtocol.GetCandidatesResponse(true, List.of(), true, null);
            }

            Map<String, Long> tallies = ElectionDatabase.getTallies(electionYear);
            long totalVotes = tallies.values().stream().mapToLong(Long::longValue).sum();

            List<GetCandidatesProtocol.CandidateInfo> infos = new ArrayList<>();
            for (Map<String, Object> c : candidates) {
                String name = (String) c.get("mayorName");
                List<String> perks = (List<String>) c.get("activePerks");
                if (perks == null) perks = List.of();
                long voteCount = tallies.getOrDefault(name, 0L);
                double pct = totalVotes > 0 ? (voteCount * 100.0) / totalVotes : 0;
                infos.add(new GetCandidatesProtocol.CandidateInfo(name, perks, voteCount, pct));
            }

            return new GetCandidatesProtocol.GetCandidatesResponse(true, infos, true, null);
        } catch (Exception e) {
            Logger.error(e, "Failed to get candidates");
            return new GetCandidatesProtocol.GetCandidatesResponse(false, List.of(), true, null);
        }
    }
}
