package net.swofty.event.actions.player.data;

import lombok.SneakyThrows;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.swofty.data.DataHandler;
import net.swofty.data.mongodb.UserDatabase;
import net.swofty.data.mongodb.ProfilesDatabase;
import net.swofty.event.EventNodes;
import net.swofty.event.EventParameters;
import net.swofty.event.SkyBlockEvent;
import net.swofty.user.UserProfiles;
import org.bson.Document;

import java.util.UUID;

@EventParameters(description = "Load player data on join",
        node = EventNodes.PLAYER,
        validLocations = EventParameters.Location.EITHER,
        requireDataLoaded = false)
public class ActionPlayerDataLoad extends SkyBlockEvent {

    @Override
    public Class<? extends Event> getEvent() {
        return PlayerLoginEvent.class;
    }

    @SneakyThrows
    @Override
    public void run(Event event) {
        PlayerLoginEvent playerLoginEvent = (PlayerLoginEvent) event;

        // Ensure we use player here
        final Player player = playerLoginEvent.getPlayer();
        UUID uuid = player.getUuid();

        UserProfiles profiles = new UserDatabase(uuid).getProfiles();
        UUID profileId = profiles.getCurrentlySelected();
        if (profiles.getCurrentlySelected() == null) {
            // Player has never played before
            profileId = UUID.randomUUID();

            profiles.setCurrentlySelected(profileId);
            profiles.addProfile(profileId);
        }

        ProfilesDatabase profilesDatabase = new ProfilesDatabase(profileId.toString());
        DataHandler handler;

        if (profilesDatabase.exists()) {
            Document document = profilesDatabase.getDocument();
            handler = DataHandler.fromDocument(document);
            DataHandler.userCache.put(uuid, handler);
        } else {
            handler = DataHandler.initUserWithDefaultData(uuid);
            DataHandler.userCache.put(uuid, handler);
        }

        handler.runOnLoad();
    }
}
