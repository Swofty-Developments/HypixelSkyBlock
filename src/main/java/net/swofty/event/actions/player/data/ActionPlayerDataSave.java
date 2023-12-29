package net.swofty.event.actions.player.data;

import lombok.SneakyThrows;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.data.DataHandler;
import net.swofty.data.mongodb.ProfilesDatabase;
import net.swofty.data.mongodb.UserDatabase;
import net.swofty.event.EventNodes;
import net.swofty.event.EventParameters;
import net.swofty.event.SkyBlockEvent;
import net.swofty.user.SkyBlockPlayer;

import java.util.Map;
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
        MinecraftServer.getSchedulerManager().scheduleTask(() -> {
            player.getSkyBlockIsland().runVacantCheck();
        }, TaskSchedule.tick(2), TaskSchedule.stop());

        /*
        Save the data into the DB
         */
        UserDatabase userDatabase = new UserDatabase(uuid);
        userDatabase.saveProfiles(player.getProfiles());

        UUID profileId = player.getProfiles().getCurrentlySelected();

        ProfilesDatabase profilesDatabase = new ProfilesDatabase(profileId.toString());
        if (profilesDatabase.exists()) {
            ProfilesDatabase.collection.replaceOne(
                    profilesDatabase.getDocument(), player.getDataHandler().toDocument(profileId)
            );
            DataHandler.userCache.remove(uuid);
        } else {
            ProfilesDatabase.collection.insertOne(player.getDataHandler().toDocument(profileId));
            DataHandler.userCache.remove(uuid);
        }

        Map<String, Object> persistentValues = player.getDataHandler().getPersistentValues();
        player.getProfiles().getProfiles().stream().filter(profile -> profile != profileId).forEach(profile -> {
            ProfilesDatabase otherProfileDatabase = new ProfilesDatabase(profile.toString());

            persistentValues.forEach(otherProfileDatabase::insertOrUpdate);
        });
    }
}
