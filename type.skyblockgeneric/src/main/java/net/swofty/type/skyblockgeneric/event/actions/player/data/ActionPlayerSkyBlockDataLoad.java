package net.swofty.type.skyblockgeneric.event.actions.player.data;

import lombok.SneakyThrows;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.swofty.commons.HypixelPlayerProfiles;
import net.swofty.type.generic.data.DataHandler;
import net.swofty.type.generic.data.mongodb.ProfilesDatabase;
import net.swofty.type.generic.data.mongodb.UserDatabase;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.data.SkyBlockDataHandler;
import net.swofty.type.generic.data.datapoints.DatapointUUID;
import net.swofty.type.generic.user.SkyBlockIsland;
import net.swofty.type.generic.user.HypixelPlayer;
import org.bson.Document;
import org.tinylog.Logger;

import java.util.UUID;

public class ActionPlayerSkyBlockDataLoad implements HypixelEventClass {

    @SneakyThrows
    @HypixelEvent(node = EventNodes.PLAYER_DATA , requireDataLoaded = false)
    public void run(AsyncPlayerConfigurationEvent event) {
        Logger.info("Loading SkyBlock data for: " + event.getPlayer().getUsername() + "...");

        final HypixelPlayer player = (HypixelPlayer) event.getPlayer();
        UUID playerUuid = player.getUuid();
        UUID islandUUID;

        // Ensure profiles cache is registered
        HypixelPlayerProfiles profiles = player.getProfiles();
        UUID profileId = profiles.getCurrentlySelected();
        if (profileId == null) {
            profileId = UUID.randomUUID();
            islandUUID = profileId;
            profiles.setCurrentlySelected(profileId);
            profiles.addProfile(profileId);
        } else {
            ProfilesDatabase temp = new ProfilesDatabase(profileId.toString());
            Document doc = temp.getDocument();
            islandUUID = SkyBlockDataHandler.createFromProfileOnly(doc)
                    .get(SkyBlockDataHandler.Data.ISLAND_UUID, DatapointUUID.class).getValue();
        }

        SkyBlockIsland island = (SkyBlockIsland.getIsland(islandUUID) == null)
                ? new SkyBlockIsland(islandUUID, profileId)
                : SkyBlockIsland.getIsland(islandUUID);
        player.setSkyBlockIsland(island);

        // Load account (UserDatabase) + profile (ProfilesDatabase)
        UserDatabase userDb = new UserDatabase(playerUuid);
        ProfilesDatabase profileDb = new ProfilesDatabase(profileId.toString());

        SkyBlockDataHandler handler = SkyBlockDataHandler.createFrom(userDb, profileDb, playerUuid, profileId);
        DataHandler.userCache.put(playerUuid, handler);

        // Run both layers' onLoad via handler
        handler.runOnLoad(player);

        Logger.info("Successfully loaded SkyBlock (profile " + profileId + ") for: " + player.getUsername());
    }
}