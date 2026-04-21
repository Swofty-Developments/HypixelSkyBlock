package net.swofty.type.bedwarsgame.redis.service;

import net.swofty.commons.bedwars.BedWarsGameType;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig;
import net.swofty.commons.service.FromServiceChannels;
import net.swofty.proxyapi.redis.ServiceToClient;
import net.swofty.type.bedwarsgame.TypeBedWarsGameLoader;
import net.swofty.type.bedwarsgame.game.v2.BedWarsGame;
import org.json.JSONObject;

public class RedisInstantiateGame implements ServiceToClient {
    @Override
    public FromServiceChannels getChannel() {
        return FromServiceChannels.INSTANTIATE_GAME;
    }

    @Override
    public JSONObject onMessage(JSONObject message) {
        try {
            if (!message.has("gameType") || !message.has("map")) {
                return new JSONObject()
                    .put("success", false)
                    .put("error", "Missing required fields: gameType and map");
            }

            String gameTypeStr = message.optString("gameType", "").trim();
            String mapName = message.optString("map", "").trim();
            if (gameTypeStr.isEmpty() || mapName.isEmpty()) {
                return new JSONObject()
                    .put("success", false)
                    .put("error", "gameType and map must be non-empty");
            }

            BedWarsGameType gameType = BedWarsGameType.from(gameTypeStr);
            if (gameType == null) {
                return new JSONObject()
                    .put("success", false)
                    .put("error", "Unknown BedWars game type: " + gameTypeStr);
            }

            // Find the map entry
            BedWarsMapsConfig.MapEntry mapEntry = null;
            if (TypeBedWarsGameLoader.getMapsConfig() != null) {
                for (BedWarsMapsConfig.MapEntry entry : TypeBedWarsGameLoader.getMapsConfig().getMaps()) {
                    boolean sameMapId = entry.getId() != null && entry.getId().equalsIgnoreCase(mapName);
                    boolean sameMapName = entry.getName() != null && entry.getName().equalsIgnoreCase(mapName);
                    if (sameMapId || sameMapName) {
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
            BedWarsGame game = TypeBedWarsGameLoader.createGame(mapEntry, gameType);
            if (game == null) {
                return new JSONObject()
                    .put("success", false)
                    .put("error", "Server at capacity, cannot create new game");
            }

            return new JSONObject()
                .put("success", true)
                .put("gameId", game.getGameId())
                .put("map", mapEntry.getName() != null ? mapEntry.getName() : mapEntry.getId())
                .put("gameType", gameType.toString());

        } catch (Exception e) {
            return new JSONObject()
                .put("success", false)
                .put("error", "Failed to instantiate game: " + e.getMessage());
        }
    }
}