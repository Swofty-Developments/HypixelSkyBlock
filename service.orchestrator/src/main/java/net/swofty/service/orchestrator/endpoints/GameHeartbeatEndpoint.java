package net.swofty.service.orchestrator.endpoints;

import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.objects.orchestrator.GameHeartbeatProtocolObject;
import net.swofty.service.generic.redis.ServiceEndpoint;
import net.swofty.service.orchestrator.OrchestratorCache;

public class GameHeartbeatEndpoint implements ServiceEndpoint
        <GameHeartbeatProtocolObject.HeartbeatMessage,
                GameHeartbeatProtocolObject.HeartbeatResponse> {

    @Override
    public ProtocolObject<GameHeartbeatProtocolObject.HeartbeatMessage, GameHeartbeatProtocolObject.HeartbeatResponse> associatedProtocolObject() {
        return new GameHeartbeatProtocolObject();
    }

    @Override
    public GameHeartbeatProtocolObject.HeartbeatResponse onMessage(ServiceProxyRequest message,
                                                                   GameHeartbeatProtocolObject.HeartbeatMessage body) {
        OrchestratorCache.handleHeartbeat(
                body.uuid(),
                body.shortName(),
                body.type(),
                body.maxPlayers(),
                body.onlinePlayers(),
                body.games()
        );
        return new GameHeartbeatProtocolObject.HeartbeatResponse(true);
    }
}