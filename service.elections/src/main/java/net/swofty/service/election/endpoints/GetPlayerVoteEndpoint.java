package net.swofty.service.election.endpoints;

import com.google.gson.Gson;
import net.swofty.commons.protocol.RedisProtocol;
import net.swofty.commons.protocol.objects.election.GetPlayerVoteProtocol;
import net.swofty.service.election.ElectionDatabase;
import net.swofty.commons.redis.RedisMessageHandler;
import org.tinylog.Logger;

import java.util.Map;
import net.swofty.commons.redis.RedisMessageContext;

public class GetPlayerVoteEndpoint implements RedisMessageHandler
        <GetPlayerVoteProtocol.GetPlayerVoteMessage,
                GetPlayerVoteProtocol.GetPlayerVoteResponse> {

    private static final Gson GSON = new Gson();

    @Override
    public RedisProtocol<GetPlayerVoteProtocol.GetPlayerVoteMessage,
            GetPlayerVoteProtocol.GetPlayerVoteResponse> protocol() {
        return new GetPlayerVoteProtocol();
    }

    @Override
    @SuppressWarnings("unchecked")
    public GetPlayerVoteProtocol.GetPlayerVoteResponse handle(GetPlayerVoteProtocol.GetPlayerVoteMessage messageObject, RedisMessageContext context) {
        try {
            String rawData = ElectionDatabase.loadElectionData();
            if (rawData == null) {
                return new GetPlayerVoteProtocol.GetPlayerVoteResponse(null, true, null);
            }

            Map<String, Object> data = GSON.fromJson(rawData, Map.class);
            Number yearNum = (Number) data.get("electionYear");
            if (yearNum == null) {
                return new GetPlayerVoteProtocol.GetPlayerVoteResponse(null, true, null);
            }

            String vote = ElectionDatabase.getPlayerVote(
                    messageObject.accountId().toString(),
                    yearNum.intValue()
            );
            return new GetPlayerVoteProtocol.GetPlayerVoteResponse(vote, true, null);
        } catch (Exception e) {
            Logger.error(e, "Failed to get player vote");
            return new GetPlayerVoteProtocol.GetPlayerVoteResponse(null, true, null);
        }
    }
}
