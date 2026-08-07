package net.swofty.service.orchestrator.endpoints;

import net.swofty.commons.protocol.RedisProtocol;
import net.swofty.commons.protocol.objects.orchestrator.ListGamesProtocol;
import net.swofty.commons.redis.RedisMessageContext;
import net.swofty.commons.redis.RedisMessageHandler;
import net.swofty.service.orchestrator.OrchestratorCache;

import java.util.ArrayList;
import java.util.List;

public class ListGamesEndpoint implements RedisMessageHandler
        <ListGamesProtocol.ListGamesMessage, ListGamesProtocol.ListGamesResponse> {

    @Override
    public RedisProtocol<ListGamesProtocol.ListGamesMessage, ListGamesProtocol.ListGamesResponse> protocol() {
        return new ListGamesProtocol();
    }

    @Override
    public ListGamesProtocol.ListGamesResponse handle(ListGamesProtocol.ListGamesMessage body,
                                                      RedisMessageContext context) {
        List<ListGamesProtocol.ServerGames> servers = new ArrayList<>();
        for (OrchestratorCache.GameServerState server : OrchestratorCache.getAllServers()) {
            if (server.type() != body.type()) continue;
            List<ListGamesProtocol.GameSummary> games = new ArrayList<>();
            for (OrchestratorCache.GameWithServer game : OrchestratorCache.getAllActiveGames()) {
                if (!game.serverUuid().equals(server.uuid())) continue;
                games.add(new ListGamesProtocol.GameSummary(
                        game.game().getGameId().toString(),
                        game.game().getGameTypeName(),
                        game.game().getMap(),
                        game.game().getInvolvedPlayers().size(),
                        game.game().isAcceptingJoins()));
            }
            servers.add(new ListGamesProtocol.ServerGames(server.shortName(),
                    server.uuid().toString(), server.onlinePlayers(), server.maxPlayers(),
                    server.remainingGameSlots(), games));
        }
        return new ListGamesProtocol.ListGamesResponse(servers, true, null);
    }
}
