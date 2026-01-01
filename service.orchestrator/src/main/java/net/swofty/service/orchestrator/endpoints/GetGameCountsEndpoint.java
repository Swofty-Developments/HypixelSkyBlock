package net.swofty.service.orchestrator.endpoints;

import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.objects.orchestrator.GetGameCountsProtocolObject;
import net.swofty.service.generic.redis.ServiceEndpoint;
import net.swofty.service.orchestrator.OrchestratorCache;

public class GetGameCountsEndpoint implements ServiceEndpoint
        <GetGameCountsProtocolObject.GetGameCountsMessage,
                GetGameCountsProtocolObject.GetGameCountsResponse> {

    @Override
    public ProtocolObject<GetGameCountsProtocolObject.GetGameCountsMessage, GetGameCountsProtocolObject.GetGameCountsResponse> associatedProtocolObject() {
        return new GetGameCountsProtocolObject();
    }

    @Override
    public GetGameCountsProtocolObject.GetGameCountsResponse onMessage(ServiceProxyRequest message, GetGameCountsProtocolObject.GetGameCountsMessage body) {
        var stats = OrchestratorCache.getGameCounts(body.type(), body.gameTypeName(), body.mapName());
        return new GetGameCountsProtocolObject.GetGameCountsResponse(stats.playerCount(), stats.gameCount());
    }
}
