package net.swofty.service.election.endpoints;

import com.google.gson.Gson;
import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.objects.election.GetPlayerVoteProtocolObject;
import net.swofty.service.election.ElectionDatabase;
import net.swofty.service.generic.redis.ServiceEndpoint;

import java.util.Map;

public class GetPlayerVoteEndpoint implements ServiceEndpoint
        <GetPlayerVoteProtocolObject.GetPlayerVoteMessage,
                GetPlayerVoteProtocolObject.GetPlayerVoteResponse> {

    private static final Gson GSON = new Gson();

    @Override
    public ProtocolObject<GetPlayerVoteProtocolObject.GetPlayerVoteMessage,
            GetPlayerVoteProtocolObject.GetPlayerVoteResponse> associatedProtocolObject() {
        return new GetPlayerVoteProtocolObject();
    }

    @Override
    @SuppressWarnings("unchecked")
    public GetPlayerVoteProtocolObject.GetPlayerVoteResponse onMessage(
            ServiceProxyRequest message,
            GetPlayerVoteProtocolObject.GetPlayerVoteMessage messageObject) {
        try {
            String rawData = ElectionDatabase.loadElectionData();
            if (rawData == null) {
                return new GetPlayerVoteProtocolObject.GetPlayerVoteResponse(null);
            }

            Map<String, Object> data = GSON.fromJson(rawData, Map.class);
            Map<String, String> votes = (Map<String, String>) data.get("votes");
            if (votes == null) {
                return new GetPlayerVoteProtocolObject.GetPlayerVoteResponse(null);
            }

            String vote = votes.get(messageObject.accountId().toString());
            return new GetPlayerVoteProtocolObject.GetPlayerVoteResponse(vote);
        } catch (Exception e) {
            return new GetPlayerVoteProtocolObject.GetPlayerVoteResponse(null);
        }
    }
}
