package net.swofty.type.village.events;

import lombok.SneakyThrows;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.swofty.type.village.TypeVillageLoader;
import net.swofty.types.generic.SkyBlockConst;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointUUID;
import net.swofty.types.generic.data.mongodb.ProfilesDatabase;
import net.swofty.types.generic.data.mongodb.UserDatabase;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.EventParameters;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.user.SkyBlockIsland;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.UserProfiles;

import java.util.UUID;

@EventParameters(description = "Sending a player to the hub",
        node = EventNodes.PLAYER,
        requireDataLoaded = false)
public class ActionPlayerJoin extends SkyBlockEvent {

    @Override
    public Class<? extends Event> getEvent() {
        return PlayerLoginEvent.class;
    }

    @SneakyThrows
    @Override
    public void run(Event event) {
        PlayerLoginEvent playerLoginEvent = (PlayerLoginEvent) event;

        final SkyBlockPlayer player = (SkyBlockPlayer) playerLoginEvent.getPlayer();

        playerLoginEvent.setSpawningInstance(SkyBlockConst.getInstanceContainer());

        player.setRespawnPoint(SkyBlockConst.getTypeLoader().getLoaderValues().spawnPosition());
    }
}
