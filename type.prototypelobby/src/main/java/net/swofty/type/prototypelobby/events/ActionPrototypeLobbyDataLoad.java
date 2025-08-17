package net.swofty.type.prototypelobby.events;

import lombok.SneakyThrows;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.swofty.type.generic.data.mongodb.UserDatabase;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.prototypelobby.PrototypeLobbyDataHandler;
import org.bson.Document;
import org.tinylog.Logger;

import java.util.UUID;

public class ActionPrototypeLobbyDataLoad implements HypixelEventClass {

    @SneakyThrows
    @HypixelEvent(node = EventNodes.PLAYER_DATA, requireDataLoaded = false)
    public void run(AsyncPlayerConfigurationEvent event) {
        Logger.info("Loading Prototype Lobby data for player: " + event.getPlayer().getUsername() + "...");

        final HypixelPlayer player = (HypixelPlayer) event.getPlayer();
        UUID playerUuid = player.getUuid();

        UserDatabase userDatabase = new UserDatabase(playerUuid);
        PrototypeLobbyDataHandler handler;

        if (userDatabase.exists()) {
            Document userDocument = userDatabase.getHypixelData();
            handler = PrototypeLobbyDataHandler.createFromDocument(userDocument);
            PrototypeLobbyDataHandler.prototypeLobbyCache.put(playerUuid, handler);
        } else {
            handler = PrototypeLobbyDataHandler.initUserWithDefaultData(playerUuid);
            PrototypeLobbyDataHandler.prototypeLobbyCache.put(playerUuid, handler);
            userDatabase.saveData(handler);
        }

        Logger.info("Successfully loaded Prototype Lobby data for player: " + player.getUsername());
    }
}