package net.swofty.service.orchestrator.endpoints;

import net.swofty.commons.ServerType;
import net.swofty.commons.UnderstandableProxyServer;
import net.swofty.commons.bedwars.BedWarsGameType;
import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.murdermystery.MurderMysteryGameType;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.objects.orchestrator.GetServerForMapProtocolObject;
import net.swofty.commons.protocol.objects.game.InstantiateGamePushProtocol;
import net.swofty.commons.skywars.SkywarsGameType;
import net.swofty.service.generic.redis.ServiceEndpoint;
import net.swofty.service.generic.redis.ServiceToServerManager;
import net.swofty.service.orchestrator.OrchestratorCache;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class GetServerForMapEndpoint implements ServiceEndpoint
    <GetServerForMapProtocolObject.GetServerForMapMessage,
        GetServerForMapProtocolObject.GetServerForMapResponse> {

    @Override
    public ProtocolObject<GetServerForMapProtocolObject.GetServerForMapMessage, GetServerForMapProtocolObject.GetServerForMapResponse> associatedProtocolObject() {
        return new GetServerForMapProtocolObject();
    }

    @Override
    public GetServerForMapProtocolObject.GetServerForMapResponse onMessage(ServiceProxyRequest message,
                                                                           GetServerForMapProtocolObject.GetServerForMapMessage body) {
        return switch (body.type()) {
            case BEDWARS_GAME -> handleBedwars(body);
            case MURDER_MYSTERY_GAME -> handleMurderMystery(body);
            case SKYWARS_GAME -> handleSkywars(body);
            default -> new GetServerForMapProtocolObject.GetServerForMapResponse(null, null, true, null);
        };
    }

    private GetServerForMapProtocolObject.GetServerForMapResponse handleBedwars(
        GetServerForMapProtocolObject.GetServerForMapMessage body) {
        try {
            BedWarsGameType gameType = parseBedwarsGameType(body.mode());
            if (gameType == null) {
                return new GetServerForMapProtocolObject.GetServerForMapResponse(null, null, true, null);
            }

            int neededSlots = body.neededSlots() > 0 ? body.neededSlots() : 1;
            String requestedMap = body.map();

            if (requestedMap != null) {
                OrchestratorCache.GameWithServer existing = OrchestratorCache.findExisting(gameType, requestedMap, neededSlots);
                if (existing != null) {
                    GetServerForMapProtocolObject.GetServerForMapResponse reused = responseFromExisting(existing);
                    if (reused.server() != null) {
                        return reused;
                    }
                }
                return instantiateBedwarsGame(gameType, requestedMap);
            }

            OrchestratorCache.GameWithServer populatedExisting = OrchestratorCache.findMostPopulatedBedwarsGame(gameType, neededSlots);
            if (populatedExisting != null) {
                GetServerForMapProtocolObject.GetServerForMapResponse reused = responseFromExisting(populatedExisting);
                if (reused.server() != null) {
                    return reused;
                }
            }

            String randomlyChosenMap = OrchestratorCache.pickRandomBedwarsMap(gameType);
            if (randomlyChosenMap == null) {
                return new GetServerForMapProtocolObject.GetServerForMapResponse(null, null, true, null);
            }

            OrchestratorCache.GameWithServer existingForRandomMap = OrchestratorCache.findExisting(gameType, randomlyChosenMap, neededSlots);
            if (existingForRandomMap != null) {
                GetServerForMapProtocolObject.GetServerForMapResponse reused = responseFromExisting(existingForRandomMap);
                if (reused.server() != null) {
                    return reused;
                }
            }

            return instantiateBedwarsGame(gameType, randomlyChosenMap);
        } catch (Exception e) {
            return new GetServerForMapProtocolObject.GetServerForMapResponse(null, null, true, null);
        }
    }

    private GetServerForMapProtocolObject.GetServerForMapResponse instantiateBedwarsGame(BedWarsGameType gameType, String map) {
        OrchestratorCache.GameServerState availableServer = OrchestratorCache.instantiateServer(gameType, map);
        if (availableServer == null) {
            return new GetServerForMapProtocolObject.GetServerForMapResponse(null, null, true, null);
        }

        try {
            CompletableFuture<InstantiateGamePushProtocol.Response> responseFuture = ServiceToServerManager.sendToServer(
                availableServer.uuid(),
                new InstantiateGamePushProtocol(),
                new InstantiateGamePushProtocol.Request(gameType.toString(), map)
            );

            InstantiateGamePushProtocol.Response response = responseFuture.get();
            if (response != null && response.success()) {
                String gameId = response.gameId();
                if (gameId != null) {
                    return new GetServerForMapProtocolObject.GetServerForMapResponse(createProxy(availableServer), gameId, true, null);
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to instantiate Bedwars game: " + e.getMessage());
        }

        return new GetServerForMapProtocolObject.GetServerForMapResponse(null, null, true, null);
    }

    private GetServerForMapProtocolObject.GetServerForMapResponse responseFromExisting(OrchestratorCache.GameWithServer gameWithServer) {
        OrchestratorCache.GameServerState hostingServer = OrchestratorCache.getServerByUuid(gameWithServer.serverUuid());
        if (hostingServer == null) {
            return new GetServerForMapProtocolObject.GetServerForMapResponse(null, null, true, null);
        }
        return new GetServerForMapProtocolObject.GetServerForMapResponse(
            createProxy(hostingServer),
            gameWithServer.game().getGameId().toString(), true, null
        );
    }

    private UnderstandableProxyServer createProxy(OrchestratorCache.GameServerState server) {
        return new UnderstandableProxyServer(
            server.shortName(),
            server.uuid(),
            server.type(),
            -1,
            new ArrayList<>(),
            server.maxPlayers(),
            server.shortName()
        );
    }

    private GetServerForMapProtocolObject.GetServerForMapResponse handleMurderMystery(
        GetServerForMapProtocolObject.GetServerForMapMessage body) {
        try {
            MurderMysteryGameType gameType = parseMurderMysteryGameType(body.mode());
            if (gameType == null) {
                return new GetServerForMapProtocolObject.GetServerForMapResponse(null, null, true, null);
            }

            int neededSlots = body.neededSlots() > 0 ? body.neededSlots() : 1;

            OrchestratorCache.GameWithServer existingGameWithServer = OrchestratorCache.findExisting(
                ServerType.MURDER_MYSTERY_GAME, gameType.getMaxPlayers(), body.map(), neededSlots, gameType.name());
            if (existingGameWithServer != null) {
                OrchestratorCache.GameServerState hostingServer = OrchestratorCache.getServerByUuid(existingGameWithServer.serverUuid());
                if (hostingServer != null) {
                    return new GetServerForMapProtocolObject.GetServerForMapResponse(createProxy(hostingServer), existingGameWithServer.game().getGameId().toString(), true, null);
                }
            }

            OrchestratorCache.GameServerState availableServer = OrchestratorCache.instantiateServer(
                ServerType.MURDER_MYSTERY_GAME, gameType.getMaxPlayers());
            if (availableServer != null) {
                try {
                    CompletableFuture<InstantiateGamePushProtocol.Response> responseFuture = ServiceToServerManager.sendToServer(
                        availableServer.uuid(),
                        new InstantiateGamePushProtocol(),
                        new InstantiateGamePushProtocol.Request(gameType.name(), body.map())
                    );

                    InstantiateGamePushProtocol.Response response = responseFuture.get();

                    if (response != null && response.success()) {
                        return new GetServerForMapProtocolObject.GetServerForMapResponse(createProxy(availableServer), response.gameId(), true, null);
                    }
                } catch (Exception e) {
                    System.err.println("Failed to instantiate Murder Mystery game: " + e.getMessage());
                }
            }

            return new GetServerForMapProtocolObject.GetServerForMapResponse(null, null, true, null);
        } catch (Exception e) {
            return new GetServerForMapProtocolObject.GetServerForMapResponse(null, null, true, null);
        }
    }

    private GetServerForMapProtocolObject.GetServerForMapResponse handleSkywars(
        GetServerForMapProtocolObject.GetServerForMapMessage body) {
        try {
            SkywarsGameType gameType = parseSkywarsGameType(body.mode());
            if (gameType == null) {
                return new GetServerForMapProtocolObject.GetServerForMapResponse(null, null, true, null);
            }

            int neededSlots = body.neededSlots() > 0 ? body.neededSlots() : 1;

            OrchestratorCache.GameWithServer existingGameWithServer = OrchestratorCache.findExisting(
                ServerType.SKYWARS_GAME, gameType.getMaxPlayers(), body.map(), neededSlots, gameType.name());
            if (existingGameWithServer != null) {
                OrchestratorCache.GameServerState hostingServer = OrchestratorCache.getServerByUuid(existingGameWithServer.serverUuid());
                if (hostingServer != null) {
                    return new GetServerForMapProtocolObject.GetServerForMapResponse(createProxy(hostingServer), existingGameWithServer.game().getGameId().toString(), true, null);
                }
            }

            OrchestratorCache.GameServerState availableServer = OrchestratorCache.instantiateServer(
                ServerType.SKYWARS_GAME, gameType.getMaxPlayers());
            if (availableServer != null) {
                try {
                    CompletableFuture<InstantiateGamePushProtocol.Response> responseFuture = ServiceToServerManager.sendToServer(
                        availableServer.uuid(),
                        new InstantiateGamePushProtocol(),
                        new InstantiateGamePushProtocol.Request(gameType.name(), body.map())
                    );

                    InstantiateGamePushProtocol.Response response = responseFuture.get();

                    if (response != null && response.success()) {
                        return new GetServerForMapProtocolObject.GetServerForMapResponse(createProxy(availableServer), response.gameId(), true, null);
                    }
                } catch (Exception e) {
                    System.err.println("Failed to instantiate Skywars game: " + e.getMessage());
                }
            }

            return new GetServerForMapProtocolObject.GetServerForMapResponse(null, null, true, null);
        } catch (Exception e) {
            return new GetServerForMapProtocolObject.GetServerForMapResponse(null, null, true, null);
        }
    }

    private BedWarsGameType parseBedwarsGameType(String mode) {
        if (mode == null) return null;

        try {
            return BedWarsGameType.valueOf(mode.toUpperCase());
        } catch (IllegalArgumentException e) {
            switch (mode.toLowerCase()) {
                case "solo", "1v1v1v1v1v1v1v1" -> {
                    return BedWarsGameType.ONE_EIGHT;
                }
                case "doubles", "2v2v2v2" -> {
                    return BedWarsGameType.TWO_EIGHT;
                }
                case "triples", "3v3v3v3" -> {
                    return BedWarsGameType.FOUR_THREE;
                }
                case "quads", "4v4v4v4" -> {
                    return BedWarsGameType.FOUR_FOUR;
                }
                case "4v4" -> {
                    return BedWarsGameType.TWO_FOUR;
                }
                default -> {
                    return null;
                }
            }
        }
    }

    private MurderMysteryGameType parseMurderMysteryGameType(String mode) {
        if (mode == null) return null;

        MurderMysteryGameType type = MurderMysteryGameType.from(mode);
        if (type != null) return type;

        type = MurderMysteryGameType.fromDisplayName(mode);
        if (type != null) return type;

        return switch (mode.toLowerCase()) {
            case "classic" -> MurderMysteryGameType.CLASSIC;
            case "double_up", "doubleup", "double up" -> MurderMysteryGameType.DOUBLE_UP;
            case "assassins" -> MurderMysteryGameType.ASSASSINS;
            default -> null;
        };
    }

    private SkywarsGameType parseSkywarsGameType(String mode) {
        if (mode == null) return null;

        SkywarsGameType type = SkywarsGameType.from(mode);
        if (type != null) return type;

        type = SkywarsGameType.fromDisplayName(mode);
        if (type != null) return type;

        return switch (mode.toLowerCase()) {
            case "solo_normal", "solo normal", "normal" -> SkywarsGameType.SOLO_NORMAL;
            case "solo_insane", "solo insane", "insane" -> SkywarsGameType.SOLO_INSANE;
            case "doubles_normal", "doubles normal", "doubles" -> SkywarsGameType.DOUBLES_NORMAL;
            case "solo_lucky_block", "lucky_block", "lucky block", "luckyblock" -> SkywarsGameType.SOLO_LUCKY_BLOCK;
            default -> null;
        };
    }
}
