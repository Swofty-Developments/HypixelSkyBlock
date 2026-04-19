package net.swofty.service.election.endpoints;

import com.google.gson.Gson;
import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.objects.election.GetElectionDataProtocolObject;
import net.swofty.service.election.ElectionDatabase;
import net.swofty.service.generic.redis.ServiceEndpoint;
import org.tinylog.Logger;

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
            return new GetElectionDataProtocolObject.GetElectionDataResponse(false, null, true, null);
        }

        try {
            Map<String, Object> parsed = GSON.fromJson(data, Map.class);

            parsed.remove("votes");

            Number yearNum = (Number) parsed.get("electionYear");
            if (yearNum != null) {
                int electionYear = yearNum.intValue();
                Map<String, Long> tallies = ElectionDatabase.getTallies(electionYear);
                parsed.put("voteTallies", tallies);
            }

            return new GetElectionDataProtocolObject.GetElectionDataResponse(true, GSON.toJson(parsed), true, null);
        } catch (Exception e) {
            Logger.error(e, "Failed to parse election data");
            return new GetElectionDataProtocolObject.GetElectionDataResponse(false, null, true, null);
        }
    }
}
