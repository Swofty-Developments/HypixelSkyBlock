package net.swofty.type.skywarsgame.redis.service;

import net.swofty.commons.service.FromServiceChannels;
import net.swofty.commons.skywars.SkywarsGameType;
import net.swofty.commons.skywars.map.SkywarsMapsConfig;
import net.swofty.proxyapi.redis.ServiceToClient;
import net.swofty.type.skywarsgame.TypeSkywarsGameLoader;
import net.swofty.type.skywarsgame.game.SkywarsGame;
import org.json.JSONObject;

import java.util.List;

public class RedisInstantiateGame implements ServiceToClient {
    @Override
    public FromServiceChannels getChannel() {
        return FromServiceChannels.INSTANTIATE_GAME;
    }

    @Override
    public JSONObject onMessage(JSONObject message) {
        try {
            String gameTypeStr = message.getString("gameType");
            String mapName = message.optString("map", null);

            SkywarsGameType gameType = SkywarsGameType.from(gameTypeStr.toUpperCase());
            if (gameType == null) {
                return new JSONObject()
                        .put("success", false)
                        .put("error", "Invalid game type: " + gameTypeStr);
            }

            SkywarsMapsConfig.MapEntry mapEntry = null;
            if (TypeSkywarsGameLoader.getMapsConfig() != null) {
                List<SkywarsMapsConfig.MapEntry> availableMaps = TypeSkywarsGameLoader.getMapsConfig().getMaps();

                if (mapName != null && !mapName.isEmpty()) {
                    for (SkywarsMapsConfig.MapEntry entry : availableMaps) {
                        if (entry.getId().equals(mapName) || entry.getName().equals(mapName)) {
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
                        mapEntry = compatibleMaps.get(java.util.concurrent.ThreadLocalRandom.current().nextInt(compatibleMaps.size()));
                    }
                }
            }

            if (mapEntry == null) {
                return new JSONObject()
                        .put("success", false)
                        .put("error", mapName != null ? "Map not found: " + mapName : "No compatible maps available for " + gameType);
            }

            SkywarsGame game = TypeSkywarsGameLoader.createGame(mapEntry, gameType);
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
