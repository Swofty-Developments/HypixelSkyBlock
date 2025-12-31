package net.swofty.type.murdermysterygame.redis.service;

import net.swofty.commons.murdermystery.MurderMysteryGameType;
import net.swofty.commons.service.FromServiceChannels;
import net.swofty.proxyapi.redis.ServiceToClient;
import net.swofty.type.murdermysterygame.TypeMurderMysteryGameLoader;
import net.swofty.type.murdermysterygame.game.Game;
import net.swofty.commons.murdermystery.map.MurderMysteryMapsConfig;
import org.json.JSONObject;

public class RedisInstantiateGame implements ServiceToClient {
    @Override
    public FromServiceChannels getChannel() {
        return FromServiceChannels.INSTANTIATE_GAME;
    }

    @Override
    public JSONObject onMessage(JSONObject message) {
        try {
            String gameTypeStr = message.getString("gameType");
            String mapName = message.getString("map");

            MurderMysteryGameType gameType = MurderMysteryGameType.valueOf(gameTypeStr.toUpperCase());

            // Find the map entry
            MurderMysteryMapsConfig.MapEntry mapEntry = null;
            if (TypeMurderMysteryGameLoader.getMapsConfig() != null) {
                for (MurderMysteryMapsConfig.MapEntry entry : TypeMurderMysteryGameLoader.getMapsConfig().getMaps()) {
                    if (entry.getId().equals(mapName) || entry.getName().equals(mapName)) {
                        // Check if this map supports the requested game type
                        if (entry.getConfiguration() != null &&
                                entry.getConfiguration().getTypes() != null &&
                                !entry.getConfiguration().getTypes().contains(gameType)) {
                            return new JSONObject()
                                    .put("success", false)
                                    .put("error", "Map does not support game type: " + gameType);
                        }
                        mapEntry = entry;
                        break;
                    }
                }
            }

            if (mapEntry == null) {
                return new JSONObject()
                        .put("success", false)
                        .put("error", "Map not found: " + mapName);
            }

            // Create the game
            Game game = TypeMurderMysteryGameLoader.createGame(mapEntry, gameType);
            if (game == null) {
                return new JSONObject()
                        .put("success", false)
                        .put("error", "Server at capacity, cannot create new game");
            }

            return new JSONObject()
                    .put("success", true)
                    .put("gameId", game.getGameId())
                    .put("map", mapEntry.getName())
                    .put("gameType", gameType.toString());

        } catch (Exception e) {
            return new JSONObject()
                    .put("success", false)
                    .put("error", "Failed to instantiate game: " + e.getMessage());
        }
    }
}
