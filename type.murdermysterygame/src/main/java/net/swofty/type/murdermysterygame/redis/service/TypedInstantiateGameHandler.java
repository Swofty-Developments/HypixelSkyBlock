package net.swofty.type.murdermysterygame.redis.service;

import net.swofty.commons.murdermystery.MurderMysteryGameType;
import net.swofty.commons.murdermystery.map.MurderMysteryMapsConfig;
import net.swofty.commons.protocol.ServicePushProtocol;
import net.swofty.commons.protocol.objects.game.InstantiateGamePushProtocol;
import net.swofty.commons.protocol.objects.game.InstantiateGamePushProtocol.Request;
import net.swofty.commons.protocol.objects.game.InstantiateGamePushProtocol.Response;
import net.swofty.proxyapi.redis.TypedServiceHandler;
import net.swofty.type.murdermysterygame.TypeMurderMysteryGameLoader;
import net.swofty.type.murdermysterygame.game.Game;

public class TypedInstantiateGameHandler implements TypedServiceHandler<Request, Response> {

    private static final InstantiateGamePushProtocol PROTOCOL = new InstantiateGamePushProtocol();

    @Override
    public ServicePushProtocol<Request, Response> getProtocol() {
        return PROTOCOL;
    }

    @Override
    public Response onMessage(Request request) {
        try {
            MurderMysteryGameType gameType = MurderMysteryGameType.valueOf(request.gameType().toUpperCase());

            MurderMysteryMapsConfig.MapEntry mapEntry = null;
            if (TypeMurderMysteryGameLoader.getMapsConfig() != null) {
                for (MurderMysteryMapsConfig.MapEntry entry : TypeMurderMysteryGameLoader.getMapsConfig().getMaps()) {
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
            }

            if (mapEntry == null) {
                return Response.failure("Map not found: " + request.map());
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
