package net.swofty.type.bedwarsgame.redis.service;

import net.swofty.commons.bedwars.BedwarsGameType;
import net.swofty.commons.service.FromServiceChannels;
import net.swofty.proxyapi.redis.ServiceToClient;
import net.swofty.type.bedwarsgame.TypeBedWarsGameLoader;
import net.swofty.type.bedwarsgame.game.Game;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig;
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

            BedwarsGameType gameType = BedwarsGameType.valueOf(gameTypeStr.toUpperCase());

            // Find the map entry
            BedWarsMapsConfig.MapEntry mapEntry = null;
            if (TypeBedWarsGameLoader.getMapsConfig() != null) {
                for (BedWarsMapsConfig.MapEntry entry : TypeBedWarsGameLoader.getMapsConfig().getMaps()) {
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
            Game game = TypeBedWarsGameLoader.createGame(mapEntry);
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