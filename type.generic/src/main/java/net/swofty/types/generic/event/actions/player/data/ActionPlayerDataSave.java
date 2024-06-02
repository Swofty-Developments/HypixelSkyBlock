package net.swofty.types.generic.event.actions.player.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.SneakyThrows;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.types.generic.SkyBlockConst;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.mongodb.ProfilesDatabase;
import net.swofty.types.generic.data.mongodb.UserDatabase;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.SkyBlockEventClass;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.MathUtility;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

public class ActionPlayerDataSave implements SkyBlockEventClass {


    @SneakyThrows
    @SkyBlockEvent(node = EventNodes.PLAYER , requireDataLoaded = false , isAsync = true)
    public void run(PlayerDisconnectEvent event) {
        final SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();
        UUID uuid = player.getUuid();

        if (!player.hasAuthenticated) return;

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
        } else {
            ProfilesDatabase.collection.insertOne(player.getDataHandler().toDocument(profileId));
        }

        Map<String, Object> persistentValues = player.getDataHandler().getPersistentValues();
        player.getProfiles().getProfiles().stream().filter(profile -> profile != profileId).forEach(profile -> {
            ProfilesDatabase otherProfileDatabase = new ProfilesDatabase(profile.toString());

            Arrays.stream(DataHandler.Data.values()).forEach(data -> {
                if (persistentValues.containsKey(data.getKey())) {
                    try {
                        otherProfileDatabase.insertOrUpdate(data.getKey(), player.getDataHandler().getDatapoint(data.getKey()).getSerializedValue());
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        });

        DataHandler.userCache.remove(uuid);
        MathUtility.delay(() -> {
            SkyBlockConst.getTypeLoader().getTablistManager().deleteTablistEntries(player);
        }, 10);
    }
}
