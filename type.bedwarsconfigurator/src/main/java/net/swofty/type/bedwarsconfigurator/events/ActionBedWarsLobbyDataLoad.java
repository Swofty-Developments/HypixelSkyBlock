package net.swofty.type.bedwarsconfigurator.events;

import lombok.SneakyThrows;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.swofty.type.generic.data.handlers.BedWarsDataHandler;
import net.swofty.type.generic.data.mongodb.UserDatabase;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.user.HypixelPlayer;
import org.bson.Document;
import org.tinylog.Logger;

import java.util.UUID;

public class ActionBedWarsLobbyDataLoad implements HypixelEventClass {

    @SneakyThrows
    @HypixelEvent(node = EventNodes.PLAYER_DATA, requireDataLoaded = false)
    public void run(AsyncPlayerConfigurationEvent event) {
        Logger.info("Loading BedWars data for player: " + event.getPlayer().getUsername() + "...");

        final HypixelPlayer player = (HypixelPlayer) event.getPlayer();
        UUID playerUuid = player.getUuid();

        UserDatabase userDatabase = new UserDatabase(playerUuid);
        BedWarsDataHandler handler;

        if (userDatabase.exists()) {
            Document userDocument = userDatabase.getHypixelData();
            handler = BedWarsDataHandler.createFromDocument(userDocument);
            BedWarsDataHandler.bedwarsCache.put(playerUuid, handler);
        } else {
            handler = BedWarsDataHandler.initUserWithDefaultData(playerUuid);
            BedWarsDataHandler.bedwarsCache.put(playerUuid, handler);
            userDatabase.saveData(handler);
        }

        Logger.info("Successfully loaded BedWars data for player: " + player.getUsername());
    }
}