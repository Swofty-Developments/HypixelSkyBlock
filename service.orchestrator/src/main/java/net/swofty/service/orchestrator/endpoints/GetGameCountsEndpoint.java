package net.swofty.service.orchestrator.endpoints;

import net.swofty.commons.protocol.RedisProtocol;
import net.swofty.commons.protocol.objects.orchestrator.GetGameCountsProtocol;
import net.swofty.commons.redis.RedisMessageHandler;
import net.swofty.service.orchestrator.OrchestratorCache;
import net.swofty.commons.redis.RedisMessageContext;

public class GetGameCountsEndpoint implements RedisMessageHandler
        <GetGameCountsProtocol.GetGameCountsMessage,
                GetGameCountsProtocol.GetGameCountsResponse> {

    @Override
    public RedisProtocol<GetGameCountsProtocol.GetGameCountsMessage, GetGameCountsProtocol.GetGameCountsResponse> protocol() {
        return new GetGameCountsProtocol();
    }

    @Override
    public GetGameCountsProtocol.GetGameCountsResponse handle(GetGameCountsProtocol.GetGameCountsMessage body, RedisMessageContext context) {
        var stats = OrchestratorCache.getGameCounts(body.type(), body.gameTypeName(), body.mapName());
        return new GetGameCountsProtocol.GetGameCountsResponse(stats.playerCount(), stats.gameCount(), true, null);
    }
}
