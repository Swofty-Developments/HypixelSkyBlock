package net.swofty.type.ravengaardgeneric.event.actions.player.data;

import lombok.SneakyThrows;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.swofty.type.generic.data.mongodb.UserDatabase;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.ravengaardgeneric.data.RavengaardDataHandler;
import net.swofty.type.ravengaardgeneric.user.RavengaardPlayer;
import org.bson.Document;
import org.tinylog.Logger;

import java.util.UUID;

public class ActionPlayerRavengaardDataLoad implements HypixelEventClass {

    @SneakyThrows
    @HypixelEvent(node = EventNodes.PLAYER_DATA, requireDataLoaded = false)
    public void run(AsyncPlayerConfigurationEvent event) {
        RavengaardPlayer player = (RavengaardPlayer) event.getPlayer();
        UUID playerUuid = player.getUuid();

        Logger.info("Loading Ravengaard data for: {}", player.getUsername());

        UserDatabase userDatabase = new UserDatabase(playerUuid);
        Document userDocument = userDatabase.getHypixelData();

        RavengaardDataHandler handler;
        if (RavengaardDataHandler.hasDataInDocument(userDocument)) {
            handler = RavengaardDataHandler.createFromDocument(playerUuid, userDocument);
        } else {
            handler = RavengaardDataHandler.initUserWithDefaultData(playerUuid);
            userDatabase.saveData(handler);
        }

        RavengaardDataHandler.ravengaardCache.put(playerUuid, handler);
        Logger.info("Successfully loaded Ravengaard data for: {}", player.getUsername());
    }
}
