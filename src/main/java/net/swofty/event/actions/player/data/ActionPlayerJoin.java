package net.swofty.event.actions.player.data;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.swofty.data.DataHandler;
import net.swofty.data.datapoints.DatapointUUID;
import net.swofty.data.mongodb.ProfilesDatabase;
import net.swofty.data.mongodb.UserDatabase;
import net.swofty.event.EventNodes;
import net.swofty.event.EventParameters;
import net.swofty.event.SkyBlockEvent;
import net.swofty.user.SkyBlockIsland;
import net.swofty.user.SkyBlockPlayer;
import net.swofty.user.UserProfiles;

import java.util.UUID;


@EventParameters(description = "Miscellaneous join stuff",
        node = EventNodes.PLAYER,
        validLocations = EventParameters.Location.EITHER,
        requireDataLoaded = false)
public class ActionPlayerJoin extends SkyBlockEvent {

    @Override
    public Class<? extends Event> getEvent() {
        return PlayerLoginEvent.class;
    }

    @Override
    public void run(Event event) {
        PlayerLoginEvent playerLoginEvent = (PlayerLoginEvent) event;

        final SkyBlockPlayer player = (SkyBlockPlayer) playerLoginEvent.getPlayer();

        UUID islandUUID;
        UUID currentlySelectedUUID = new UserDatabase(player.getUuid()).getProfiles().getCurrentlySelected();
        if (currentlySelectedUUID == null
                || !new ProfilesDatabase(currentlySelectedUUID.toString()).exists()) {
            UserProfiles profiles = new UserDatabase(player.getUuid()).getProfiles();
            UUID profileId = UUID.randomUUID();

            islandUUID = profileId;
            profiles.setCurrentlySelected(profileId);
            profiles.addProfile(profileId);
        } else {
            islandUUID = DataHandler.fromDocument(new ProfilesDatabase(currentlySelectedUUID.toString()).getDocument())
                    .get(DataHandler.Data.ISLAND_UUID, DatapointUUID.class).getValue();
        }

        if (islandUUID == null) {
            islandUUID = currentlySelectedUUID;
        }

        SkyBlockIsland island;
        if (SkyBlockIsland.getIsland(islandUUID) == null) {
            island = new SkyBlockIsland(islandUUID, currentlySelectedUUID);
        } else {
            // Island is already loaded, presumably from a coop
            island = SkyBlockIsland.getIsland(islandUUID);
        }
        player.setSkyBlockIsland(island);
        playerLoginEvent.setSpawningInstance(player.getSkyBlockIsland().getSharedInstance().join());

        player.sendMessage("§7Sending to server mini1A...");
        player.sendMessage("§7 ");

        player.setRespawnPoint(new Pos(0, 100, 0));
    }
}
