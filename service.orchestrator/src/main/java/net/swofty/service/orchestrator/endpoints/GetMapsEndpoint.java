package net.swofty.service.orchestrator.endpoints;

import net.swofty.commons.protocol.RedisProtocol;
import net.swofty.commons.protocol.objects.orchestrator.GetMapsProtocol;
import net.swofty.commons.redis.RedisMessageHandler;
import net.swofty.service.orchestrator.OrchestratorCache;
import net.swofty.commons.redis.RedisMessageContext;

public class GetMapsEndpoint implements RedisMessageHandler
        <GetMapsProtocol.GetMapsMessage,
                GetMapsProtocol.GetMapsResponse> {

    @Override
    public RedisProtocol<GetMapsProtocol.GetMapsMessage, GetMapsProtocol.GetMapsResponse> protocol() {
        return new GetMapsProtocol();
    }

    @Override
    public GetMapsProtocol.GetMapsResponse handle(GetMapsProtocol.GetMapsMessage body, RedisMessageContext context) {
        var maps = OrchestratorCache.getMaps(body.type(), body.mode());
        return new GetMapsProtocol.GetMapsResponse(maps.stream().sorted().toList(), true, null);
    }
}