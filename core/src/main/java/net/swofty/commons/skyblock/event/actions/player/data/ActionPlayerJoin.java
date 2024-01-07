package net.swofty.commons.skyblock.event.actions.player.data;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.swofty.commons.skyblock.user.UserProfiles;
import net.swofty.commons.skyblock.data.DataHandler;
import net.swofty.commons.skyblock.data.datapoints.DatapointUUID;
import net.swofty.commons.skyblock.data.mongodb.ProfilesDatabase;
import net.swofty.commons.skyblock.data.mongodb.UserDatabase;
import net.swofty.commons.skyblock.event.EventNodes;
import net.swofty.commons.skyblock.event.EventParameters;
import net.swofty.commons.skyblock.event.SkyBlockEvent;
import net.swofty.commons.skyblock.user.SkyBlockIsland;
import net.swofty.commons.skyblock.user.SkyBlockPlayer;

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
