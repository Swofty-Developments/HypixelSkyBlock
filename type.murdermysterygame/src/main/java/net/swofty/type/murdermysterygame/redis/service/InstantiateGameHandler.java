package net.swofty.type.murdermysterygame.redis.service;

import net.swofty.commons.murdermystery.MurderMysteryGameType;
import net.swofty.commons.murdermystery.map.MurderMysteryMapsConfig;
import net.swofty.commons.protocol.RedisProtocol;
import net.swofty.commons.protocol.objects.game.InstantiateGamePushProtocol;
import net.swofty.commons.protocol.objects.game.InstantiateGamePushProtocol.Request;
import net.swofty.commons.protocol.objects.game.InstantiateGamePushProtocol.Response;
import net.swofty.commons.redis.RedisMessageHandler;
import net.swofty.type.murdermysterygame.TypeMurderMysteryGameLoader;
import net.swofty.type.murdermysterygame.game.Game;
import net.swofty.commons.redis.RedisMessageContext;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class InstantiateGameHandler implements RedisMessageHandler<Request, Response> {

    private static final InstantiateGamePushProtocol PROTOCOL = new InstantiateGamePushProtocol();

    @Override
    public RedisProtocol<Request, Response> protocol() {
        return PROTOCOL;
    }

    @Override
    public Response handle(Request request, RedisMessageContext context) {
        try {
            MurderMysteryGameType gameType = MurderMysteryGameType.valueOf(request.gameType().toUpperCase());

            boolean hasRequestedMap = request.map() != null && !request.map().isEmpty();

            MurderMysteryMapsConfig.MapEntry mapEntry = null;
            if (TypeMurderMysteryGameLoader.getMapsConfig() != null) {
                List<MurderMysteryMapsConfig.MapEntry> maps = TypeMurderMysteryGameLoader.getMapsConfig().getMaps();
                if (hasRequestedMap) {
                    for (MurderMysteryMapsConfig.MapEntry entry : maps) {
                        if (entry.getId().equals(request.map()) || entry.getName().equals(request.map())) {
                            if (entry.getConfiguration() != null &&
                                    entry.getConfiguration().getTypes() != null &&
                                    !entry.getConfiguration().getTypes().contains(gameType)) {
                                return Response.failure("Map does not support game type: " + gameType);
                            }
                            mapEntry = entry;
                            break;
                        }
                    }
                } else {
                    // No specific map requested (queueing a mode): pick a random compatible map.
                    List<MurderMysteryMapsConfig.MapEntry> compatible = maps.stream()
                            .filter(e -> e.getConfiguration() != null
                                    && e.getConfiguration().getTypes() != null
                                    && e.getConfiguration().getTypes().contains(gameType))
                            .toList();
                    if (!compatible.isEmpty()) {
                        mapEntry = compatible.get(ThreadLocalRandom.current().nextInt(compatible.size()));
                    }
                }
            }

            if (mapEntry == null) {
                return Response.failure(hasRequestedMap
                        ? "Map not found: " + request.map()
                        : "No compatible maps available for " + gameType);
            }

            Game game = TypeMurderMysteryGameLoader.createGame(mapEntry, gameType);
            if (game == null) {
                return Response.failure("Server at capacity, cannot create new game");
            }

            return Response.success(game.getGameId(), mapEntry.getName(), gameType.toString());
        } catch (Exception e) {
            return Response.failure("Failed to instantiate game: " + e.getMessage());
        }
    }
}
