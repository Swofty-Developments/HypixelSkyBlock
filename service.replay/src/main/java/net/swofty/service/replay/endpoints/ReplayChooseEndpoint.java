package net.swofty.service.replay.endpoints;

import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.objects.replay.ChooseReplayProtocolObject;
import net.swofty.service.generic.redis.ServiceEndpoint;
import net.swofty.service.generic.redis.ServiceToServerManager;

public class ReplayChooseEndpoint implements ServiceEndpoint
		<ChooseReplayProtocolObject.ChooseReplayMessage,
			ChooseReplayProtocolObject.ChooseReplayResponse> {

	@Override
	public ProtocolObject<ChooseReplayProtocolObject.ChooseReplayMessage, ChooseReplayProtocolObject.ChooseReplayResponse> associatedProtocolObject() {
		return new ChooseReplayProtocolObject();
	}

	@Override
	public ChooseReplayProtocolObject.ChooseReplayResponse onMessage(ServiceProxyRequest message,
																	 ChooseReplayProtocolObject.ChooseReplayMessage body) {
		ServiceToServerManager.viewReplay(body.player(), body.replayId());
		return new ChooseReplayProtocolObject.ChooseReplayResponse(false);
	}

}
