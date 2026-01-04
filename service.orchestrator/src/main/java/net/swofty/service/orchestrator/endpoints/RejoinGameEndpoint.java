package net.swofty.service.orchestrator.endpoints;

import net.swofty.commons.ServerType;
import net.swofty.commons.UnderstandableProxyServer;
import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.objects.orchestrator.RejoinGameProtocolObject;
import net.swofty.service.generic.redis.ServiceEndpoint;
import net.swofty.service.orchestrator.OrchestratorCache;

import java.util.ArrayList;

public class RejoinGameEndpoint implements ServiceEndpoint<
        RejoinGameProtocolObject.RejoinGameRequest,
        RejoinGameProtocolObject.RejoinGameResponse> {

    @Override
    public ProtocolObject<RejoinGameProtocolObject.RejoinGameRequest, RejoinGameProtocolObject.RejoinGameResponse> associatedProtocolObject() {
        return new RejoinGameProtocolObject();
    }

    @Override
    public RejoinGameProtocolObject.RejoinGameResponse onMessage(ServiceProxyRequest message,
                                                                  RejoinGameProtocolObject.RejoinGameRequest body) {
        try {
            // Find the game this player is part of (active or disconnected)
            OrchestratorCache.GameWithServer gameWithServer = OrchestratorCache.findPlayerGame(body.playerUuid());

            if (gameWithServer == null) {
                return new RejoinGameProtocolObject.RejoinGameResponse(false, null, null, null, null, false);
            }

            OrchestratorCache.GameServerState hostingServer = OrchestratorCache.getServerByUuid(gameWithServer.serverUuid());
            if (hostingServer == null) {
                return new RejoinGameProtocolObject.RejoinGameResponse(false, null, null, null, null, false);
            }

            // Skywars does not support rejoining
            if (hostingServer.type() == ServerType.SKYWARS_GAME) {
                return new RejoinGameProtocolObject.RejoinGameResponse(false, null, null, null, null, false);
            }

            // Check if this player is in the disconnected list (meaning they should rejoin)
            // If they're in involvedPlayers, they're already in the game
            boolean isDisconnected = gameWithServer.game().getDisconnectedPlayers() != null
                    && gameWithServer.game().getDisconnectedPlayers().contains(body.playerUuid());

            if (!isDisconnected) {
                // Player is already in an active game, not a rejoin scenario
                return new RejoinGameProtocolObject.RejoinGameResponse(false, null, null, null, null, false);
            }

            UnderstandableProxyServer proxy = new UnderstandableProxyServer(
                    hostingServer.shortName(),
                    hostingServer.uuid(),
                    hostingServer.type(),
                    -1,
                    new ArrayList<>(),
                    hostingServer.maxPlayers(),
                    hostingServer.shortName()
            );

            return new RejoinGameProtocolObject.RejoinGameResponse(
                    true,
                    proxy,
                    gameWithServer.game().getGameId().toString(),
                    gameWithServer.game().getMap(),
                    null, // Team name not available from commons Game, will be determined on game server
                    false // Whether spectator determined on game server based on current bed status
            );
        } catch (Exception e) {
            System.err.println("Failed to check rejoin: " + e.getMessage());
            return new RejoinGameProtocolObject.RejoinGameResponse(false, null, null, null, null, false);
        }
    }
}
