package net.swofty.service.orchestrator.endpoints;

import net.swofty.commons.UnderstandableProxyServer;
import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.objects.orchestrator.GetServerForMapProtocolObject;
import net.swofty.service.generic.redis.ServiceEndpoint;
import net.swofty.service.orchestrator.OrchestratorCache;

import java.util.ArrayList;

public class GetServerForMapEndpoint implements ServiceEndpoint
		<GetServerForMapProtocolObject.GetServerForMapMessage,
				GetServerForMapProtocolObject.GetServerForMapResponse> {

	@Override
	public ProtocolObject<GetServerForMapProtocolObject.GetServerForMapMessage, GetServerForMapProtocolObject.GetServerForMapResponse> associatedProtocolObject() {
		return new GetServerForMapProtocolObject();
	}

	@Override
	public GetServerForMapProtocolObject.GetServerForMapResponse onMessage(ServiceProxyRequest message, GetServerForMapProtocolObject.GetServerForMapMessage body) {
		OrchestratorCache.GameServerState chosen = OrchestratorCache.pickServerForMap(body.type(), body.map(), body.mode(), body.neededSlots());
		if (chosen == null) return new GetServerForMapProtocolObject.GetServerForMapResponse(null);
		UnderstandableProxyServer proxy = new UnderstandableProxyServer(
				chosen.shortName(),
				chosen.uuid(),
				chosen.type(),
				-1,
				new ArrayList<>(),
				chosen.maxPlayers(),
				chosen.shortName()
		);
		return new GetServerForMapProtocolObject.GetServerForMapResponse(proxy);
	}
}
