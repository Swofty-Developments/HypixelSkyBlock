package net.swofty.type.skyblockgeneric.event.actions.player.data;

import lombok.SneakyThrows;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.swofty.commons.skyblock.SkyBlockPlayerProfiles;
import net.swofty.type.generic.data.datapoints.DatapointString;
import net.swofty.type.generic.data.mongodb.ProfilesDatabase;
import net.swofty.type.generic.data.mongodb.UserDatabase;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointUUID;
import net.swofty.type.skyblockgeneric.user.SkyBlockIsland;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import org.bson.Document;
import org.tinylog.Logger;

import java.util.Objects;
import java.util.UUID;

public class ActionPlayerSkyBlockDataLoad implements HypixelEventClass {

    @SneakyThrows
    @HypixelEvent(node = EventNodes.PLAYER_DATA, requireDataLoaded = false)
    public void run(AsyncPlayerConfigurationEvent event) {
        Logger.info("Loading SkyBlock data for: " + event.getPlayer().getUsername() + "...");

        final SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();
        UUID playerUuid = player.getUuid();

        // Check if player has ever joined SkyBlock before
        SkyBlockPlayerProfiles profiles = new UserDatabase(playerUuid).getProfiles();
        UUID profileId;

        if (profiles == null) {
            // Brand new SkyBlock player - initialize everything
            Logger.info("New SkyBlock player detected: " + player.getUsername() + " - initializing...");

            profileId = UUID.randomUUID();
            // Create new profiles object
            profiles = new SkyBlockPlayerProfiles(playerUuid);
            profiles.setCurrentlySelected(profileId);
            profiles.addProfile(profileId);

            // Save the new profiles to database
            UserDatabase userDb = new UserDatabase(playerUuid);
            userDb.saveProfiles(profiles);
        } else {
            // Existing player
            profileId = profiles.getCurrentlySelected();
            if (profileId == null) {
                // Player has profiles but no selected profile - create new one
                profileId = UUID.randomUUID();
                profiles.setCurrentlySelected(profileId);
                profiles.addProfile(profileId);

                // Save updated profiles
                UserDatabase userDb = new UserDatabase(playerUuid);
                userDb.saveProfiles(profiles);
            }
        }

        // Load SkyBlock profile data
        ProfilesDatabase profileDb = new ProfilesDatabase(profileId.toString());
        SkyBlockDataHandler handler;
        boolean shouldPersistProfile = false;

        if (profileDb.exists()) {
            Document profileDocument = profileDb.getDocument();
            handler = SkyBlockDataHandler.createFromProfile(playerUuid, profileId, profileDocument);
        } else {
            handler = SkyBlockDataHandler.initUserWithDefaultData(playerUuid, profileId);
            shouldPersistProfile = true;
        }

        DatapointUUID islandDatapoint = handler.get(SkyBlockDataHandler.Data.ISLAND_UUID, DatapointUUID.class);
        UUID islandUUID = islandDatapoint.getValue();
        if (islandUUID == null) {
            islandUUID = profileId;
            islandDatapoint.setValue(islandUUID);
            shouldPersistProfile = true;
        }

        DatapointString profileNameDatapoint = handler.get(SkyBlockDataHandler.Data.PROFILE_NAME, DatapointString.class);
        if (Objects.equals(profileNameDatapoint.getValue(), "null")) {
            profileNameDatapoint.setValue(SkyBlockPlayerProfiles.getRandomName());
            shouldPersistProfile = true;
        }

        // Put in SkyBlock cache
        SkyBlockDataHandler.skyBlockCache.put(playerUuid, handler);

        if (shouldPersistProfile) {
            profileDb.saveDocument(handler.toProfileDocument());
        }

        // Set up the island after the cache is populated so island init can see the player as loaded
        SkyBlockIsland island = (SkyBlockIsland.getIsland(islandUUID) == null)
            ? new SkyBlockIsland(islandUUID, profileId)
            : SkyBlockIsland.getIsland(islandUUID);
        player.setSkyBlockIsland(island);

        Logger.info("Successfully loaded SkyBlock (profile " + profileId + ") for: " + player.getUsername());
    }
}
