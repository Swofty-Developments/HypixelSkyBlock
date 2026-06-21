package net.swofty.service.replay.endpoints;

import net.swofty.commons.protocol.RedisProtocol;
import net.swofty.commons.protocol.objects.replay.ChooseReplayProtocolObject;
import net.swofty.commons.redis.RedisMessageHandler;
import net.swofty.commons.redis.RedisMessageContext;
import net.swofty.service.generic.redis.ServiceToServerManager;

public class ReplayChooseEndpoint implements RedisMessageHandler
		<ChooseReplayProtocolObject.ChooseReplayMessage,
			ChooseReplayProtocolObject.ChooseReplayResponse> {

	@Override
	public RedisProtocol<ChooseReplayProtocolObject.ChooseReplayMessage, ChooseReplayProtocolObject.ChooseReplayResponse> protocol() {
		return new ChooseReplayProtocolObject();
	}

	@Override
	public ChooseReplayProtocolObject.ChooseReplayResponse handle(ChooseReplayProtocolObject.ChooseReplayMessage body, RedisMessageContext context) {
		ServiceToServerManager.viewReplay(body.player(), body.replayId(), body.shareCode());
		return new ChooseReplayProtocolObject.ChooseReplayResponse(false);
	}

}
