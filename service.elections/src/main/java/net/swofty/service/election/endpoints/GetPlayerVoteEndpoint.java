package net.swofty.service.election.endpoints;

import com.google.gson.Gson;
import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.objects.election.GetPlayerVoteProtocolObject;
import net.swofty.service.election.ElectionDatabase;
import net.swofty.service.generic.redis.ServiceEndpoint;
import org.tinylog.Logger;

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
                return new GetPlayerVoteProtocolObject.GetPlayerVoteResponse(null, true, null);
            }

            Map<String, Object> data = GSON.fromJson(rawData, Map.class);
            Number yearNum = (Number) data.get("electionYear");
            if (yearNum == null) {
                return new GetPlayerVoteProtocolObject.GetPlayerVoteResponse(null, true, null);
            }

            String vote = ElectionDatabase.getPlayerVote(
                    messageObject.accountId().toString(),
                    yearNum.intValue()
            );
            return new GetPlayerVoteProtocolObject.GetPlayerVoteResponse(vote, true, null);
        } catch (Exception e) {
            Logger.error(e, "Failed to get player vote");
            return new GetPlayerVoteProtocolObject.GetPlayerVoteResponse(null, true, null);
        }
    }
}
