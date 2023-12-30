package net.swofty.event.actions.player.data;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.swofty.SkyBlock;
import net.swofty.data.mongodb.UserDatabase;
import net.swofty.event.EventNodes;
import net.swofty.event.EventParameters;
import net.swofty.event.SkyBlockEvent;
import net.swofty.user.SkyBlockIsland;
import net.swofty.user.SkyBlockPlayer;
import net.swofty.user.UserProfiles;
import org.tinylog.Logger;

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
        if (new UserDatabase(player.getUuid()).getProfiles().getCurrentlySelected() == null) {
            UserProfiles profiles = new UserDatabase(player.getUuid()).getProfiles();
            UUID profileId = UUID.randomUUID();

            islandUUID = profileId;
            profiles.setCurrentlySelected(profileId);
            profiles.addProfile(profileId);
        } else {
            islandUUID = new UserDatabase(player.getUuid()).getProfiles().getCurrentlySelected();
        }

        player.setSkyBlockIsland(new SkyBlockIsland(player, islandUUID));
        playerLoginEvent.setSpawningInstance(player.getSkyBlockIsland().getSharedInstance().join());

        player.sendMessage("§7Sending to server mini1A...");
        player.sendMessage("§7 ");

        player.setRespawnPoint(new Pos(0, 100, 0));
    }
}
