package net.swofty.type.generic.event.actions.data;

import lombok.SneakyThrows;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.swofty.commons.proxy.ToProxyChannels;
import net.swofty.proxyapi.redis.ServerOutboundMessage;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.data.DataHandler;
import net.swofty.type.generic.data.Datapoint;
import net.swofty.type.generic.data.GameDataHandler;
import net.swofty.type.generic.data.GameDataHandlerRegistry;
import net.swofty.type.generic.data.HypixelDataHandler;
import net.swofty.type.generic.data.mongodb.UserDatabase;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.leaderboard.LeaderboardService;
import net.swofty.type.generic.leaderboard.LeaderboardTracked;
import net.swofty.type.generic.leaderboard.MapLeaderboardTracked;
import net.swofty.type.generic.resourcepack.ResourcePackManager;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.utility.MathUtility;
import org.json.JSONObject;
import org.tinylog.Logger;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class ActionPlayerDataSave implements HypixelEventClass {
    private static final String DATA_SAVE_FAILURE_MESSAGE =
        "We couldn't safely save your player data during transfer. Please reconnect.";

    @SneakyThrows
    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = false, isAsync = true)
    public void run(PlayerDisconnectEvent event) {
        final HypixelPlayer player = (HypixelPlayer) event.getPlayer();
        UUID uuid = player.getUuid();
        boolean saveSucceeded = false;

        ResourcePackManager packManager = HypixelConst.getResourcePackManager();
        if (packManager != null) {
            packManager.getActivePack().onPlayerQuit(player);
        }

        try {
            Logger.info("Saving Hypixel account data for: " + player.getUsername() + "...");

            Optional<HypixelDataHandler> handlerOptional = player.getOptionalDataHandler();
            if (handlerOptional.isEmpty()) {
                throw new IllegalStateException("User " + uuid + " does not exist in HypixelDataHandler cache during save");
            }

            HypixelDataHandler handler = handlerOptional.get();

            // Run onSave callbacks for basic Hypixel functionality
            handler.runOnSave(player);

            // Save Hypixel data to UserDatabase (account-wide data)
            UserDatabase userDatabase = new UserDatabase(uuid);
            userDatabase.saveData(handler);

            // Sync all leaderboard-tracked datapoints for Hypixel data
            syncLeaderboards(uuid, handler);

            // Save additional game handlers
            List<Class<? extends GameDataHandler>> additionalHandlers =
                HypixelConst.getTypeLoader().getAdditionalDataHandlers();

            for (Class<? extends GameDataHandler> handlerClass : additionalHandlers) {
                GameDataHandler gameHandler = GameDataHandlerRegistry.get(handlerClass);
                if (gameHandler == null) continue;

                DataHandler gameDataHandler = gameHandler.getHandler(uuid);
                if (gameDataHandler == null) continue;

                Logger.info("Saving " + gameHandler.getHandlerId() + " data for: " + player.getUsername());

                gameDataHandler.runOnSave(player);
                userDatabase.saveData(gameDataHandler);

                // Sync leaderboards for this game handler
                syncLeaderboards(uuid, gameDataHandler);
            }

            saveSucceeded = true;
            Logger.info("Successfully saved all data for: " + player.getUsername());
        } catch (Exception e) {
            Logger.error(e, "Failed to save all data for: {}", player.getUsername());
        } finally {
            HypixelDataHandler.userCache.remove(uuid);

            List<Class<? extends GameDataHandler>> additionalHandlers =
                HypixelConst.getTypeLoader().getAdditionalDataHandlers();
            for (Class<? extends GameDataHandler> handlerClass : additionalHandlers) {
                GameDataHandler gameHandler = GameDataHandlerRegistry.get(handlerClass);
                if (gameHandler != null) {
                    gameHandler.removeFromCache(uuid);
                }
            }

            notifyProxyOfSaveCompletion(uuid, saveSucceeded);

            // Clean up tablist entries
            MathUtility.delay(() -> {
                HypixelConst.getTypeLoader().getTablistManager().deleteTablistEntries(player);
            }, 5);
        }
    }

    /**
     * Syncs all leaderboard-tracked datapoints in a handler to Redis.
     * This ensures the final values are persisted to leaderboards on disconnect.
     */
    private void syncLeaderboards(UUID uuid, DataHandler handler) {
        if (!LeaderboardService.isInitialized()) return;

        for (Datapoint<?> dp : handler.getDatapoints().values()) {
            // Simple leaderboard (single value like XP, coins)
            if (dp instanceof LeaderboardTracked tracked) {
                String key = tracked.getLeaderboardKey();
                if (key != null) {
                    LeaderboardService.updateScore(key, uuid, tracked.getLeaderboardScore());
                }
            }

            // Map-based leaderboard (collections, skills)
            if (dp instanceof MapLeaderboardTracked mapTracked) {
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

    private void notifyProxyOfSaveCompletion(UUID uuid, boolean saveSucceeded) {
        JSONObject message = new JSONObject()
            .put("uuid", uuid.toString())
            .put("success", saveSucceeded);
        if (!saveSucceeded) {
            message.put("reason", DATA_SAVE_FAILURE_MESSAGE);
        }

        ServerOutboundMessage.sendMessageToProxy(
            ToProxyChannels.FINISHED_WITH_PLAYER,
            message,
            (response) -> {
            }
        );
    }
}
