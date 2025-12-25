package net.swofty.type.generic.event.actions.data;

import lombok.SneakyThrows;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.data.DataHandler;
import net.swofty.type.generic.data.GameDataHandler;
import net.swofty.type.generic.data.GameDataHandlerRegistry;
import net.swofty.type.generic.data.HypixelDataHandler;
import net.swofty.type.generic.data.mongodb.UserDatabase;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.user.HypixelPlayer;
import org.bson.Document;
import org.tinylog.Logger;

import java.util.List;
import java.util.UUID;

public class ActionPlayerDataLoad implements HypixelEventClass {

    @SneakyThrows
    @HypixelEvent(node = EventNodes.PLAYER_DATA, requireDataLoaded = false)
    public void run(AsyncPlayerConfigurationEvent event) {
        Logger.info("Loading Hypixel account data for player: " + event.getPlayer().getUsername() + "...");

        final HypixelPlayer player = (HypixelPlayer) event.getPlayer();
        UUID playerUuid = player.getUuid();

        UserDatabase userDatabase = new UserDatabase(playerUuid);
        HypixelDataHandler handler;
        Document userDocument = null;

        if (userDatabase.exists()) {
            userDocument = userDatabase.getHypixelData();
            handler = HypixelDataHandler.createFromDocument(userDocument);
            HypixelDataHandler.userCache.put(playerUuid, handler);
        } else {
            handler = HypixelDataHandler.initUserWithDefaultData(playerUuid);
            HypixelDataHandler.userCache.put(playerUuid, handler);
            userDatabase.saveData(handler);
        }

        // Load additional game handlers based on TypeLoader configuration
        List<Class<? extends GameDataHandler>> additionalHandlers =
                HypixelConst.getTypeLoader().getAdditionalDataHandlers();

        for (Class<? extends GameDataHandler> handlerClass : additionalHandlers) {
            GameDataHandler gameHandler = GameDataHandlerRegistry.get(handlerClass);
            if (gameHandler == null) {
                Logger.warn("GameDataHandler not registered: " + handlerClass.getSimpleName());
                continue;
            }

            Logger.info("Loading " + gameHandler.getHandlerId() + " data for: " + player.getUsername());

            DataHandler gameDataHandler;
            if (userDocument != null && gameHandler.hasDataInDocument(userDocument)) {
                gameDataHandler = gameHandler.createFromDocument(playerUuid, userDocument);
            } else {
                gameDataHandler = gameHandler.initWithDefaults(playerUuid);
            }

            gameHandler.cacheHandler(playerUuid, gameDataHandler);

            // Save if this is new data
            if (userDocument == null || !gameHandler.hasDataInDocument(userDocument)) {
                userDatabase.saveData(gameDataHandler);
            }
        }

        Logger.info("Successfully loaded all data for player: " + player.getUsername());
    }
}