package net.swofty.type.lobby;

import net.swofty.commons.ServerType;
import net.swofty.commons.ServiceType;
import net.swofty.commons.UnderstandableProxyServer;
import net.swofty.commons.protocol.objects.orchestrator.ChooseGameProtocolObject;
import net.swofty.commons.protocol.objects.orchestrator.GetServerForMapProtocolObject;
import net.swofty.proxyapi.ProxyService;
import net.swofty.type.generic.user.HypixelPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public record LobbyOrchestratorConnector(HypixelPlayer player) {
    private static final ProxyService PROXY_SERVICE = new ProxyService(ServiceType.ORCHESTRATOR);
    private static final List<UUID> PLAYERS_SEARCHING = new ArrayList<>();

    public CompletableFuture<Pair<UnderstandableProxyServer, String>> findGameServer(
            ServerType targetServerType,
            String gameType,
            @Nullable String map
    ) {
        if (PLAYERS_SEARCHING.contains(player.getUuid())) {
            return CompletableFuture.completedFuture(null);
        }

        PLAYERS_SEARCHING.add(player.getUuid());

        GetServerForMapProtocolObject.GetServerForMapMessage message =
                new GetServerForMapProtocolObject.GetServerForMapMessage(
                        targetServerType,
                        map,
                        gameType,
                        1
                );

        return PROXY_SERVICE.handleRequest(message)
                .thenApply(response -> {
                    PLAYERS_SEARCHING.remove(player.getUuid());

                    if (response instanceof GetServerForMapProtocolObject.GetServerForMapResponse(
                            UnderstandableProxyServer server, String gameId
                    )) {
                        return new Pair<>(server, gameId);
                    }
                    return null;
                })
                .exceptionally(throwable -> {
                    PLAYERS_SEARCHING.remove(player.getUuid());
                    player.sendMessage("§cFailed to find server: " + throwable.getMessage());
                    return null;
                });
    }

    public void sendToGame(ServerType targetServerType, String gameType, @Nullable String map) {
        if (PLAYERS_SEARCHING.contains(player.getUuid())) {
            player.sendMessage("§cAlready searching for a game!");
            return;
        }

        findGameServer(targetServerType, gameType, map).thenAccept(pair -> {
            if (pair != null && pair.first() != null) {
                ChooseGameProtocolObject.ChooseGameMessage message =
                        new ChooseGameProtocolObject.ChooseGameMessage(player.getUuid(), pair.first(), pair.second());

                PROXY_SERVICE.handleRequest(message)
                        .exceptionally(throwable -> {
                            player.sendMessage("§cFailed to choose game: " + throwable.getMessage());
                            return null;
                        });

                player.sendMessage("§aSending you to " + pair.first().shortName() + "!");
                player.asProxyPlayer().transferToWithIndication(pair.first().uuid());
            } else {
                player.sendMessage("§cNo available servers found! Please try again later.");
            }
        });
    }

    public void sendToGame(ServerType targetServerType, String gameType) {
        sendToGame(targetServerType, gameType, null);
    }

    public static boolean isSearching(UUID playerUuid) {
        return PLAYERS_SEARCHING.contains(playerUuid);
    }

    public static void removeFromSearching(UUID playerUuid) {
        PLAYERS_SEARCHING.remove(playerUuid);
    }

    public record Pair<A, B>(A first, B second) {}
}
