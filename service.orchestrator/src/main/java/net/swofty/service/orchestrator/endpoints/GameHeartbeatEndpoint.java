package net.swofty.service.orchestrator.endpoints;

import net.swofty.commons.protocol.RedisProtocol;
import net.swofty.commons.protocol.objects.orchestrator.GameHeartbeatProtocol;
import net.swofty.commons.redis.RedisMessageHandler;
import net.swofty.service.orchestrator.OrchestratorCache;
import net.swofty.commons.redis.RedisMessageContext;

public class GameHeartbeatEndpoint implements RedisMessageHandler
        <GameHeartbeatProtocol.HeartbeatMessage,
                GameHeartbeatProtocol.HeartbeatResponse> {

    @Override
    public RedisProtocol<GameHeartbeatProtocol.HeartbeatMessage, GameHeartbeatProtocol.HeartbeatResponse> protocol() {
        return new GameHeartbeatProtocol();
    }

    @Override
    public GameHeartbeatProtocol.HeartbeatResponse handle(GameHeartbeatProtocol.HeartbeatMessage body, RedisMessageContext context) {
        OrchestratorCache.handleHeartbeat(
            body.uuid(),
            body.shortName(),
            body.type(),
            body.maxPlayers(),
            body.onlinePlayers(),
            body.games(),
            body.mapAdvertisements(),
            body.remainingGameSlots()
        );
        return new GameHeartbeatProtocol.HeartbeatResponse(true, null);
    }
}