package net.swofty.type.ravengardgeneric.event.actions.player.data;

import lombok.SneakyThrows;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.swofty.type.generic.data.mongodb.UserDatabase;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.ravengardgeneric.data.RavengardDataHandler;
import net.swofty.type.ravengardgeneric.user.RavengardPlayer;
import org.bson.Document;
import org.tinylog.Logger;

import java.util.UUID;

public class ActionPlayerRavengardDataLoad implements HypixelEventClass {

    @SneakyThrows
    @HypixelEvent(node = EventNodes.PLAYER_DATA, requireDataLoaded = false)
    public void run(AsyncPlayerConfigurationEvent event) {
        RavengardPlayer player = (RavengardPlayer) event.getPlayer();
        UUID playerUuid = player.getUuid();

        Logger.info("Loading Ravengard data for: {}", player.getUsername());

        UserDatabase userDatabase = new UserDatabase(playerUuid);
        Document userDocument = userDatabase.getHypixelData();

        RavengardDataHandler handler;
        if (RavengardDataHandler.hasDataInDocument(userDocument)) {
            handler = RavengardDataHandler.createFromDocument(playerUuid, userDocument);
        } else {
            handler = RavengardDataHandler.initUserWithDefaultData(playerUuid);
            userDatabase.saveData(handler);
        }

        RavengardDataHandler.ravengardCache.put(playerUuid, handler);
        Logger.info("Successfully loaded Ravengard data for: {}", player.getUsername());
    }
}
