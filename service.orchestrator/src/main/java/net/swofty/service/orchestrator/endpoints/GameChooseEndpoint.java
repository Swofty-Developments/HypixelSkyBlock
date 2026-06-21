package net.swofty.service.orchestrator.endpoints;

import net.swofty.commons.protocol.RedisProtocol;
import net.swofty.commons.protocol.objects.game.GameInformationPushProtocol;
import net.swofty.commons.protocol.objects.orchestrator.ChooseGameProtocol;
import net.swofty.commons.redis.RedisMessageHandler;
import net.swofty.commons.redis.RedisClient;
import net.swofty.commons.redis.RedisMessageContext;

public class GameChooseEndpoint implements RedisMessageHandler
		<ChooseGameProtocol.ChooseGameMessage,
				ChooseGameProtocol.ChooseGameResponse> {

	@Override
	public RedisProtocol<ChooseGameProtocol.ChooseGameMessage, ChooseGameProtocol.ChooseGameResponse> protocol() {
		return new ChooseGameProtocol();
	}

	@Override
	public ChooseGameProtocol.ChooseGameResponse handle(ChooseGameProtocol.ChooseGameMessage body, RedisMessageContext context) {
		RedisClient.requestServerFromService(body.server().uuid(), new GameInformationPushProtocol(),
				new GameInformationPushProtocol.Request(body.player(), body.gameId()));
		return new ChooseGameProtocol.ChooseGameResponse(true, null);
	}

}
