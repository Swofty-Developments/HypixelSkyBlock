package net.swofty.service.election.endpoints;

import com.google.gson.Gson;
import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.objects.election.GetElectionDataProtocolObject;
import net.swofty.service.election.ElectionDatabase;
import net.swofty.service.generic.redis.ServiceEndpoint;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetElectionDataEndpoint implements ServiceEndpoint
        <GetElectionDataProtocolObject.GetElectionDataMessage,
                GetElectionDataProtocolObject.GetElectionDataResponse> {

    private static final Gson GSON = new Gson();

    @Override
    public ProtocolObject<GetElectionDataProtocolObject.GetElectionDataMessage,
            GetElectionDataProtocolObject.GetElectionDataResponse> associatedProtocolObject() {
        return new GetElectionDataProtocolObject();
    }

    @Override
    @SuppressWarnings("unchecked")
    public GetElectionDataProtocolObject.GetElectionDataResponse onMessage(
            ServiceProxyRequest message,
            GetElectionDataProtocolObject.GetElectionDataMessage messageObject) {
        String data = ElectionDatabase.loadElectionData();
        if (data == null) {
            return new GetElectionDataProtocolObject.GetElectionDataResponse(false, null);
        }

        try {
            Map<String, Object> parsed = GSON.fromJson(data, Map.class);

            Map<String, String> votes = (Map<String, String>) parsed.get("votes");
            List<Map<String, Object>> candidates = (List<Map<String, Object>>) parsed.get("candidates");

            Map<String, Long> tallies = new HashMap<>();
            if (candidates != null) {
                for (Map<String, Object> c : candidates) {
                    tallies.put((String) c.get("mayorName"), 0L);
                }
            }
            if (votes != null) {
                for (String candidateName : votes.values()) {
                    tallies.merge(candidateName, 1L, Long::sum);
                }
            }

            parsed.put("voteTallies", tallies);
            parsed.remove("votes");

            return new GetElectionDataProtocolObject.GetElectionDataResponse(true, GSON.toJson(parsed));
        } catch (Exception e) {
            return new GetElectionDataProtocolObject.GetElectionDataResponse(true, data);
        }
    }
}
