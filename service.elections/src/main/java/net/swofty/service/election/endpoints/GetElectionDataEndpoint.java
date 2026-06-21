package net.swofty.service.election.endpoints;

import com.google.gson.Gson;
import net.swofty.commons.protocol.RedisProtocol;
import net.swofty.commons.protocol.objects.election.GetElectionDataProtocol;
import net.swofty.service.election.ElectionDatabase;
import net.swofty.commons.redis.RedisMessageHandler;
import org.tinylog.Logger;

import java.util.List;
import java.util.Map;
import net.swofty.commons.redis.RedisMessageContext;

public class GetElectionDataEndpoint implements RedisMessageHandler
        <GetElectionDataProtocol.GetElectionDataMessage,
                GetElectionDataProtocol.GetElectionDataResponse> {

    private static final Gson GSON = new Gson();

    @Override
    public RedisProtocol<GetElectionDataProtocol.GetElectionDataMessage,
            GetElectionDataProtocol.GetElectionDataResponse> protocol() {
        return new GetElectionDataProtocol();
    }

    @Override
    @SuppressWarnings("unchecked")
    public GetElectionDataProtocol.GetElectionDataResponse handle(GetElectionDataProtocol.GetElectionDataMessage messageObject, RedisMessageContext context) {
        String data = ElectionDatabase.loadElectionData();
        if (data == null) {
            return new GetElectionDataProtocol.GetElectionDataResponse(false, null, true, null);
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

            return new GetElectionDataProtocol.GetElectionDataResponse(true, GSON.toJson(parsed), true, null);
        } catch (Exception e) {
            Logger.error(e, "Failed to parse election data");
            return new GetElectionDataProtocol.GetElectionDataResponse(false, null, true, null);
        }
    }
}
