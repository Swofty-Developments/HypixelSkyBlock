package net.swofty.type.generic.user.flow;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import net.swofty.commons.data.SwoftyData;
import net.swofty.commons.protocol.objects.proxy.to.FinishedWithPlayerProtocol;
import net.swofty.commons.redis.RedisClient;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.data.DataHandler;
import net.swofty.type.generic.data.Datapoint;
import net.swofty.type.generic.data.GameDataHandler;
import net.swofty.type.generic.data.GameDataHandlerRegistry;
import net.swofty.type.generic.data.HypixelDataHandler;
import net.swofty.type.generic.data.datapoints.DatapointLocale;
import net.swofty.type.generic.data.mongodb.UserDatabase;
import net.swofty.type.generic.entity.hologram.PlayerHolograms;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.leaderboard.LeaderboardService;
import net.swofty.type.generic.leaderboard.LeaderboardTracked;
import net.swofty.type.generic.leaderboard.MapLeaderboardTracked;
import net.swofty.type.generic.resourcepack.ResourcePackManager;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.utility.MathUtility;
import org.bson.Document;
import org.tinylog.Logger;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class GenericPlayerDataFlow {

    public static void load(HypixelPlayer player) {
        UUID playerUuid = player.getUuid();

        HypixelDataHandler handler = HypixelDataHandler.initUserWithDefaultData(playerUuid);
        handler.loadFromApi();
        HypixelDataHandler.userCache.put(playerUuid, handler);
        net.swofty.type.generic.achievement.AchievementStatisticsService.recordPlayer(playerUuid);

        Locale locale = handler.get(HypixelDataHandler.Data.LOCALE, DatapointLocale.class)
                .getValue()
                .getCurrentLocale()
                .getLocale();
        player.setLocale(locale);

        loadAdditionalHandlers(player);
    }

    public static void postSpawn(HypixelPlayer player) {
        UUID uuid = player.getUuid();

        player.getDataHandler().runOnLoad(player);

        for (Class<? extends GameDataHandler> handlerClass : HypixelConst.getTypeLoader().getAdditionalDataHandlers()) {
            GameDataHandler gameHandler = GameDataHandlerRegistry.get(handlerClass);
            if (gameHandler != null) {
                gameHandler.runOnLoad(uuid, player);
            }
        }

        ResourcePackManager packManager = HypixelConst.getResourcePackManager();
        if (packManager != null) {
            packManager.sendPack(player);
            packManager.getActivePack().onPlayerJoin(player);
        }

        HypixelNPC.updateForPlayer(player);
        if (!HypixelConst.isIslandServer()) {
            PlayerHolograms.spawnAll(player);
        }
    }

    public static void save(HypixelPlayer player) {
        UUID uuid = player.getUuid();

        ResourcePackManager packManager = HypixelConst.getResourcePackManager();
        if (packManager != null) {
            packManager.getActivePack().onPlayerQuit(player);
        }

        HypixelDataHandler handler = player.getDataHandler();
        handler.runOnSave(player);
        handler.saveToApi();
        syncLeaderboards(uuid, handler);

        for (Class<? extends GameDataHandler> handlerClass : HypixelConst.getTypeLoader().getAdditionalDataHandlers()) {
            GameDataHandler gameHandler = GameDataHandlerRegistry.get(handlerClass);
            if (gameHandler == null) continue;

            DataHandler gameDataHandler = gameHandler.getHandler(uuid);
            if (gameDataHandler == null) continue;

            gameDataHandler.runOnSave(player);
            gameDataHandler.saveBackedData();
            syncLeaderboards(uuid, gameDataHandler);
            gameHandler.removeFromCache(uuid);
        }

        SwoftyData.account().unload(uuid);
        HypixelDataHandler.userCache.remove(uuid);

        RedisClient.requestProxy(new FinishedWithPlayerProtocol(), new FinishedWithPlayerProtocol.Request(uuid.toString()));

        MathUtility.delay(() -> HypixelConst.getTypeLoader().getTablistManager().deleteTablistEntries(player), 5);
    }

    private static void loadAdditionalHandlers(HypixelPlayer player) {
        UUID playerUuid = player.getUuid();
        List<Class<? extends GameDataHandler>> additionalHandlers = HypixelConst.getTypeLoader().getAdditionalDataHandlers();

        for (Class<? extends GameDataHandler> handlerClass : additionalHandlers) {
            GameDataHandler gameHandler = GameDataHandlerRegistry.get(handlerClass);
            if (gameHandler == null) {
                Logger.warn("GameDataHandler not registered: {}", handlerClass.getSimpleName());
                continue;
            }

            DataHandler gameDataHandler = gameHandler.initWithDefaults(playerUuid);
            gameDataHandler.loadBackedData();
            gameHandler.cacheHandler(playerUuid, gameDataHandler);
        }
    }

    private static void syncLeaderboards(UUID uuid, DataHandler handler) {
        if (!LeaderboardService.isInitialized()) return;

        for (Datapoint<?> datapoint : handler.getDatapoints().values()) {
            if (datapoint instanceof LeaderboardTracked tracked) {
                String key = tracked.getLeaderboardKey();
                if (key != null) {
                    LeaderboardService.updateScore(key, uuid, tracked.getLeaderboardScore());
                }
            }

            if (datapoint instanceof MapLeaderboardTracked mapTracked) {
                Map<String, Double> scores = mapTracked.getAllLeaderboardScores();
                for (Map.Entry<String, Double> entry : scores.entrySet()) {
                    LeaderboardService.updateScore(
                            mapTracked.getLeaderboardKeyFor(entry.getKey()),
                            uuid,
                            entry.getValue()
                    );
                }
            }
        }
    }
}
