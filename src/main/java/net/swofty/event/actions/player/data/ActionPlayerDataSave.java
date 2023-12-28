package net.swofty.event.actions.player.data;

import lombok.SneakyThrows;
import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.swofty.data.DataHandler;
import net.swofty.data.mongodb.UserDatabase;
import net.swofty.event.EventNodes;
import net.swofty.event.EventParameters;
import net.swofty.event.SkyBlockEvent;
import net.swofty.user.SkyBlockPlayer;

import java.util.UUID;

@EventParameters(description = "Saves player data on quit",
        node = EventNodes.PLAYER,
        validLocations = EventParameters.Location.EITHER,
        requireDataLoaded = false)
public class ActionPlayerDataSave extends SkyBlockEvent {

    @Override
    public Class<? extends Event> getEvent() {
        return PlayerDisconnectEvent.class;
    }

    @SneakyThrows
    @Override
    public void run(Event event) {
        PlayerDisconnectEvent playerDisconnectEvent = (PlayerDisconnectEvent) event;

        final SkyBlockPlayer player = (SkyBlockPlayer) playerDisconnectEvent.getPlayer();
        UUID uuid = player.getUuid();

        player.getDataHandler().runOnSave(player);

        /*
         * Save the data into the DB
         */
        UserDatabase userDatabase = new UserDatabase(uuid.toString());
        if (userDatabase.exists()) {
            UserDatabase.collection.replaceOne(userDatabase.getDocument(), player.getDataHandler().toDocument());
            DataHandler.userCache.remove(uuid);
        } else {
            UserDatabase.collection.insertOne(player.getDataHandler().toDocument());
            DataHandler.userCache.remove(uuid);
        }
    }
}
