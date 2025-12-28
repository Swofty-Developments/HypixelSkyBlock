package net.swofty.service.orchestrator.endpoints;

import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.objects.orchestrator.ChooseGameProtocolObject;
import net.swofty.service.generic.redis.ServiceEndpoint;
import net.swofty.service.generic.redis.ServiceToServerManager;

public class GameChooseEndpoint implements ServiceEndpoint
		<ChooseGameProtocolObject.ChooseGameMessage,
				ChooseGameProtocolObject.ChooseGameResponse> {

	@Override
	public ProtocolObject<ChooseGameProtocolObject.ChooseGameMessage, ChooseGameProtocolObject.ChooseGameResponse> associatedProtocolObject() {
		return new ChooseGameProtocolObject();
	}

	@Override
	public ChooseGameProtocolObject.ChooseGameResponse onMessage(ServiceProxyRequest message,
																		   ChooseGameProtocolObject.ChooseGameMessage body) {
		ServiceToServerManager.gameInformation(body.server().uuid(), body.player(), body.gameId());
		return new ChooseGameProtocolObject.ChooseGameResponse(false);
	}

}
