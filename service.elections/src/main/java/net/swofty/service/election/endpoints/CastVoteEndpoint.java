package net.swofty.service.election.endpoints;

import com.google.gson.Gson;
import net.swofty.commons.protocol.RedisProtocol;
import net.swofty.commons.protocol.objects.election.CastVoteProtocol;
import net.swofty.service.election.ElectionDatabase;
import net.swofty.commons.redis.RedisMessageHandler;
import org.tinylog.Logger;

import java.util.List;
import java.util.Map;
import net.swofty.commons.redis.RedisMessageContext;

public class CastVoteEndpoint implements RedisMessageHandler
        <CastVoteProtocol.CastVoteMessage,
                CastVoteProtocol.CastVoteResponse> {

    @Override
    public RedisProtocol<CastVoteProtocol.CastVoteMessage,
            CastVoteProtocol.CastVoteResponse> protocol() {
        return new CastVoteProtocol();
    }

    @Override
    @SuppressWarnings("unchecked")
    public CastVoteProtocol.CastVoteResponse handle(CastVoteProtocol.CastVoteMessage messageObject, RedisMessageContext context) {
        try {
            String rawData = ElectionDatabase.loadElectionData();
            if (rawData == null) {
                return new CastVoteProtocol.CastVoteResponse(false, null, "Vote failed");
            }

            Map<String, Object> data = new Gson().fromJson(rawData, Map.class);
            Boolean electionOpen = (Boolean) data.get("electionOpen");
            if (electionOpen == null || !electionOpen) {
                return new CastVoteProtocol.CastVoteResponse(false, null, "Vote failed");
            }

            int electionYear = ((Number) data.get("electionYear")).intValue();

            List<Map<String, Object>> candidates = (List<Map<String, Object>>) data.get("candidates");
            if (candidates == null) {
                return new CastVoteProtocol.CastVoteResponse(false, null, "Vote failed");
            }

            boolean validCandidate = candidates.stream()
                    .anyMatch(c -> messageObject.candidateName().equals(c.get("mayorName")));
            if (!validCandidate) {
                return new CastVoteProtocol.CastVoteResponse(false, null, "Vote failed");
            }

            ElectionDatabase.castVote(
                    messageObject.accountId().toString(),
                    messageObject.candidateName(),
                    electionYear
            );

            Map<String, Long> tallies = ElectionDatabase.getTallies(electionYear);
            return new CastVoteProtocol.CastVoteResponse(true, tallies, null);
        } catch (Exception e) {
            Logger.error(e, "Failed to cast vote");
            return new CastVoteProtocol.CastVoteResponse(false, null, "Vote failed");
        }
    }
}
