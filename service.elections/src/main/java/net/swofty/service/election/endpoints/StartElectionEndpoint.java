package net.swofty.service.election.endpoints;

import com.google.gson.Gson;
import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.objects.election.StartElectionProtocolObject;
import net.swofty.service.election.ElectionDatabase;
import net.swofty.service.generic.redis.ServiceEndpoint;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StartElectionEndpoint implements ServiceEndpoint
        <StartElectionProtocolObject.StartElectionMessage,
                StartElectionProtocolObject.StartElectionResponse> {

    private static final Gson GSON = new Gson();

    @Override
    public ProtocolObject<StartElectionProtocolObject.StartElectionMessage,
            StartElectionProtocolObject.StartElectionResponse> associatedProtocolObject() {
        return new StartElectionProtocolObject();
    }

    @Override
    @SuppressWarnings("unchecked")
    public StartElectionProtocolObject.StartElectionResponse onMessage(
            ServiceProxyRequest message,
            StartElectionProtocolObject.StartElectionMessage messageObject) {
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
                    return new StartElectionProtocolObject.StartElectionResponse(false, GSON.toJson(existing), true, null);
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
            return new StartElectionProtocolObject.StartElectionResponse(true, GSON.toJson(electionData), true, null);
        } catch (Exception e) {
            Logger.error(e, "Failed to start election");
            return new StartElectionProtocolObject.StartElectionResponse(false, null, true, null);
        }
    }
}
