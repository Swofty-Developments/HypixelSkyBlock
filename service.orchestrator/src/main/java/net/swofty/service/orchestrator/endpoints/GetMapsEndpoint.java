package net.swofty.service.orchestrator.endpoints;

import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.objects.orchestrator.GetMapsProtocolObject;
import net.swofty.service.generic.redis.ServiceEndpoint;
import net.swofty.service.orchestrator.OrchestratorCache;

public class GetMapsEndpoint implements ServiceEndpoint
        <GetMapsProtocolObject.GetMapsMessage,
                GetMapsProtocolObject.GetMapsResponse> {

    @Override
    public ProtocolObject<GetMapsProtocolObject.GetMapsMessage, GetMapsProtocolObject.GetMapsResponse> associatedProtocolObject() {
        return new GetMapsProtocolObject();
    }

    @Override
    public GetMapsProtocolObject.GetMapsResponse onMessage(ServiceProxyRequest message, GetMapsProtocolObject.GetMapsMessage body) {
        var maps = OrchestratorCache.getMaps(body.type(), body.mode());
        return new GetMapsProtocolObject.GetMapsResponse(maps.stream().sorted().toList());
    }
}