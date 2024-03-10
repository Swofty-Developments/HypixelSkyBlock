package net.swofty.types.generic.event.actions.player.data;

import com.mongodb.client.model.Filters;
import lombok.SneakyThrows;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointUUID;
import net.swofty.types.generic.data.mongodb.ProfilesDatabase;
import net.swofty.types.generic.data.mongodb.UserDatabase;
import net.swofty.types.generic.user.SkyBlockIsland;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.event.EventException;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.EventParameters;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.user.PlayerProfiles;
import org.bson.Document;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.UUID;

@EventParameters(description = "Load player data on join",
        node = EventNodes.PLAYER_DATA,
        requireDataLoaded = false)
public class ActionPlayerDataLoad extends SkyBlockEvent implements EventException {

    @Override
    public Class<? extends Event> getEvent() {
        return AsyncPlayerConfigurationEvent.class;
    }

    @SneakyThrows
    @Override
    public void run(Event event) {
        AsyncPlayerConfigurationEvent playerLoginEvent = (AsyncPlayerConfigurationEvent) event;

        Logger.info("New player joined: " + playerLoginEvent.getPlayer().getUsername() + "...");

        // Ensure we use player here
        final Player player = playerLoginEvent.getPlayer();
        UUID playerUuid = player.getUuid();
        UUID islandUUID;

        // Registers the users profiles within the profiles cache
        new UserDatabase(player.getUuid()).getProfiles();

        PlayerProfiles profiles = ((SkyBlockPlayer) playerLoginEvent.getPlayer()).getProfiles();
        UUID profileId = profiles.getCurrentlySelected();
        if (profileId == null) {
            profileId = UUID.randomUUID();

            islandUUID = profileId;
            profiles.setCurrentlySelected(profileId);
            profiles.addProfile(profileId);
        } else {
            profileId = profiles.getCurrentlySelected();
            islandUUID = DataHandler.fromDocument(new ProfilesDatabase(profileId.toString()).getDocument())
                    .get(DataHandler.Data.ISLAND_UUID, DatapointUUID.class).getValue();
        }

        SkyBlockIsland island;
        if (SkyBlockIsland.getIsland(islandUUID) == null) {
            island = new SkyBlockIsland(islandUUID, profileId);
        } else {
            // Island is already loaded, presumably from a coop
            island = SkyBlockIsland.getIsland(islandUUID);
        }
        ((SkyBlockPlayer) player).setSkyBlockIsland(island);

        ProfilesDatabase profilesDatabase = new ProfilesDatabase(profileId.toString());
        DataHandler handler;

        if (profilesDatabase.exists()) {
            Document document = profilesDatabase.getDocument();
            handler = DataHandler.fromDocument(document);
            DataHandler.userCache.put(playerUuid, handler);
        } else {
            handler = DataHandler.initUserWithDefaultData(playerUuid);
            DataHandler.userCache.put(playerUuid, handler);
        }
    }

    @Override
    public void onException(Exception e, Event tempEvent) {
        AsyncPlayerConfigurationEvent event = (AsyncPlayerConfigurationEvent) tempEvent;
        e.printStackTrace();

        if (e instanceof NullPointerException) {
            event.getPlayer().sendMessage("§cAn error occurred while loading your data.");

            UUID playerUuid = event.getPlayer().getUuid();

            DataHandler handler = DataHandler.initUserWithDefaultData(playerUuid);
            DataHandler.userCache.put(playerUuid, handler);

            handler.runOnLoad((SkyBlockPlayer) event.getPlayer());

            PlayerProfiles profiles = ((SkyBlockPlayer) event.getPlayer()).getProfiles();

            if (profiles.getProfiles().size() >= 2) {
                Document previousProfile = ProfilesDatabase.collection
                        .find(Filters.eq("_owner", playerUuid.toString())).into(new ArrayList<>())
                        .stream().filter(document -> !document.get("_id").equals(profiles.getCurrentlySelected().toString()))
                        .findFirst().get();

                DataHandler previousHandler = DataHandler.fromDocument(previousProfile);
                previousHandler.getPersistentValues().forEach((key, value) -> {
                    handler.getDatapoint(key).setValue(value);
                });
            }
        }
    }
}
