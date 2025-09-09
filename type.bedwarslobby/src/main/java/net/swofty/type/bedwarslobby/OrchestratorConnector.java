package net.swofty.type.bedwarslobby;

import net.swofty.commons.BedwarsGameType;
import net.swofty.commons.ServerType;
import net.swofty.commons.ServiceType;
import net.swofty.commons.UnderstandableProxyServer;
import net.swofty.commons.protocol.objects.orchestrator.GetServerForMapProtocolObject;
import net.swofty.proxyapi.ProxyService;
import net.swofty.type.generic.user.HypixelPlayer;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public record OrchestratorConnector(HypixelPlayer player) {
    private static final ProxyService PROXY_SERVICE = new ProxyService(ServiceType.ORCHESTRATOR);
    private static final List<UUID> PLAYERS_SEARCHING = new ArrayList<>();

    public CompletableFuture<UnderstandableProxyServer> findGameServer(BedwarsGameType gameType, String map) {
        if (PLAYERS_SEARCHING.contains(player.getUuid())) {
            return CompletableFuture.completedFuture(null);
        }

        PLAYERS_SEARCHING.add(player.getUuid());

        GetServerForMapProtocolObject.GetServerForMapMessage message =
                new GetServerForMapProtocolObject.GetServerForMapMessage(
                        ServerType.BEDWARS_GAME,
                        map,
                        gameType.toString(),
                        1
                );

        return PROXY_SERVICE.handleRequest(message)
                .thenApply(response -> {
                    PLAYERS_SEARCHING.remove(player.getUuid());

                    if (response instanceof GetServerForMapProtocolObject.GetServerForMapResponse serverResponse) {
                        return serverResponse.server();
                    }
                    return null;
                })
                .exceptionally(throwable -> {
                    PLAYERS_SEARCHING.remove(player.getUuid());
                    player.sendMessage("§cFailed to find server: " + throwable.getMessage());
                    return null;
                });
    }

    public CompletableFuture<UnderstandableProxyServer> findGameServer(BedwarsGameType gameType) {
        return findGameServer(gameType, null);
    }

    public void sendToGame(BedwarsGameType gameType, String map) {
        if (PLAYERS_SEARCHING.contains(player.getUuid())) {
            player.sendMessage("§cAlready searching for a game!");
            return;
        }

        player.sendMessage("§7Searching for " + gameType.getDisplayName() + " game" + (map != null ? " on " + map : "") + "...");

        findGameServer(gameType, map).thenAccept(server -> {
            if (server != null) {
                player.sendMessage("§7Found game! Sending you to " + server.shortName() + "...");
                player.asProxyPlayer().transferToWithIndication(server.uuid());
            } else {
                player.sendMessage("§cNo available servers found! Please try again later.");
            }
        });
    }

    public void sendToGame(BedwarsGameType gameType) {
        sendToGame(gameType, null);
    }

    public static boolean isSearching(UUID playerUuid) {
        return PLAYERS_SEARCHING.contains(playerUuid);
    }

    public static void removeFromSearching(UUID playerUuid) {
        PLAYERS_SEARCHING.remove(playerUuid);
    }
}