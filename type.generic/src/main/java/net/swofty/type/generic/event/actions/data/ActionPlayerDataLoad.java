package net.swofty.type.generic.event.actions.data;

import lombok.SneakyThrows;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.data.HypixelDataHandler;
import net.swofty.type.generic.data.mongodb.UserDatabase;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.user.HypixelPlayer;
import org.bson.Document;
import org.tinylog.Logger;

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

        if (userDatabase.exists()) {
            Document userDocument = userDatabase.getHypixelData();
            handler = HypixelDataHandler.createFromDocument(userDocument);
            HypixelDataHandler.userCache.put(playerUuid, handler);
        } else {
            handler = HypixelDataHandler.initUserWithDefaultData(playerUuid);
            HypixelDataHandler.userCache.put(playerUuid, handler);
            userDatabase.saveData(handler);
        }

        Logger.info("Successfully loaded Hypixel account data for player: " + player.getUsername());
    }
}