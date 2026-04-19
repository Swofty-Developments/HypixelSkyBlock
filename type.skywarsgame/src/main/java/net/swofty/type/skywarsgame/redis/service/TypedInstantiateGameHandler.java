package net.swofty.type.skywarsgame.redis.service;

import net.swofty.commons.protocol.ServicePushProtocol;
import net.swofty.commons.protocol.objects.game.InstantiateGamePushProtocol;
import net.swofty.commons.protocol.objects.game.InstantiateGamePushProtocol.Request;
import net.swofty.commons.protocol.objects.game.InstantiateGamePushProtocol.Response;
import net.swofty.commons.skywars.SkywarsGameType;
import net.swofty.commons.skywars.map.SkywarsMapsConfig;
import net.swofty.proxyapi.redis.TypedServiceHandler;
import net.swofty.type.skywarsgame.TypeSkywarsGameLoader;
import net.swofty.type.skywarsgame.game.SkywarsGame;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class TypedInstantiateGameHandler implements TypedServiceHandler<Request, Response> {

    private static final InstantiateGamePushProtocol PROTOCOL = new InstantiateGamePushProtocol();

    @Override
    public ServicePushProtocol<Request, Response> getProtocol() {
        return PROTOCOL;
    }

    @Override
    public Response onMessage(Request request) {
        try {
            SkywarsGameType gameType = SkywarsGameType.from(request.gameType().toUpperCase());
            if (gameType == null) {
                return Response.failure("Invalid game type: " + request.gameType());
            }

            SkywarsMapsConfig.MapEntry mapEntry = null;
            if (TypeSkywarsGameLoader.getMapsConfig() != null) {
                List<SkywarsMapsConfig.MapEntry> availableMaps = TypeSkywarsGameLoader.getMapsConfig().getMaps();

                if (request.map() != null && !request.map().isEmpty()) {
                    for (SkywarsMapsConfig.MapEntry entry : availableMaps) {
                        if (entry.getId().equals(request.map()) || entry.getName().equals(request.map())) {
                            mapEntry = entry;
                            break;
                        }
                    }
                } else {
                    List<SkywarsMapsConfig.MapEntry> compatibleMaps = availableMaps.stream()
                            .filter(entry -> entry.getConfiguration() != null
                                    && entry.getConfiguration().getTypes() != null
                                    && entry.getConfiguration().getTypes().contains(gameType))
                            .toList();

                    if (!compatibleMaps.isEmpty()) {
                        mapEntry = compatibleMaps.get(ThreadLocalRandom.current().nextInt(compatibleMaps.size()));
                    }
                }
            }

            if (mapEntry == null) {
                return Response.failure(request.map() != null ? "Map not found: " + request.map() : "No compatible maps available for " + gameType);
            }

            SkywarsGame game = TypeSkywarsGameLoader.createGame(mapEntry, gameType);
            if (game == null) {
                return Response.failure("Server at capacity, cannot create new game");
            }

            return Response.success(game.getGameId(), mapEntry.getName(), gameType.toString());
        } catch (Exception e) {
            return Response.failure("Failed to instantiate game: " + e.getMessage());
        }
    }
}
