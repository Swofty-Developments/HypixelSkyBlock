package net.swofty.service.election.endpoints;

import com.google.gson.Gson;
import net.swofty.commons.protocol.RedisProtocol;
import net.swofty.commons.protocol.objects.election.StartElectionProtocol;
import net.swofty.service.election.ElectionDatabase;
import net.swofty.commons.redis.RedisMessageHandler;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.swofty.commons.redis.RedisMessageContext;

public class StartElectionEndpoint implements RedisMessageHandler
        <StartElectionProtocol.StartElectionMessage,
                StartElectionProtocol.StartElectionResponse> {

    private static final Gson GSON = new Gson();

    @Override
    public RedisProtocol<StartElectionProtocol.StartElectionMessage,
            StartElectionProtocol.StartElectionResponse> protocol() {
        return new StartElectionProtocol();
    }

    @Override
    @SuppressWarnings("unchecked")
    public StartElectionProtocol.StartElectionResponse handle(StartElectionProtocol.StartElectionMessage messageObject, RedisMessageContext context) {
        try {
            String rawData = ElectionDatabase.loadElectionData();

            if (rawData != null) {
                Map<String, Object> existing = GSON.fromJson(rawData, Map.class);
                Boolean electionOpen = (Boolean) existing.get("electionOpen");
                Number existingYear = (Number) existing.get("electionYear");

                if (electionOpen != null && electionOpen
                        && existingYear != null && existingYear.intValue() == messageObject.year()) {
                    Map<String, Long> tallies = ElectionDatabase.getTallies(messageObject.year());
                    existing.put("voteTallies", tallies);
                    existing.remove("votes");
                    return new StartElectionProtocol.StartElectionResponse(false, GSON.toJson(existing), true, null);
                }
            }

            Map<String, Object> electionData = GSON.fromJson(
                    messageObject.candidatesJson(), Map.class
            );

            ElectionDatabase.saveElectionData(messageObject.candidatesJson());

            List<Map<String, Object>> candidates = (List<Map<String, Object>>) electionData.get("candidates");
            List<String> candidateNames = new ArrayList<>();
            if (candidates != null) {
                for (Map<String, Object> c : candidates) {
                    candidateNames.add((String) c.get("mayorName"));
                }
            }
            ElectionDatabase.initTallies(messageObject.year(), candidateNames);

            electionData.remove("votes");
            return new StartElectionProtocol.StartElectionResponse(true, GSON.toJson(electionData), true, null);
        } catch (Exception e) {
            Logger.error(e, "Failed to start election");
            return new StartElectionProtocol.StartElectionResponse(false, null, true, null);
        }
    }
}
