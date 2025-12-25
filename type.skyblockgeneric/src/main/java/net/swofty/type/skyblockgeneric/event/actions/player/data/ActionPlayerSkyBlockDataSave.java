package net.swofty.type.skyblockgeneric.event.actions.player.data;

import lombok.SneakyThrows;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.swofty.type.generic.data.DataHandler;
import net.swofty.type.generic.data.mongodb.ProfilesDatabase;
import net.swofty.type.generic.data.mongodb.UserDatabase;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import org.bson.Document;
import org.tinylog.Logger;

import java.util.UUID;

import static com.mongodb.client.model.Filters.eq;

public class ActionPlayerSkyBlockDataSave implements HypixelEventClass {

    @SneakyThrows
    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = false, isAsync = true)
    public void run(PlayerDisconnectEvent event) {
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();
        UUID playerUuid = player.getUuid();
        SkyBlockDataHandler handler = SkyBlockDataHandler.skyBlockCache.get(playerUuid);

        if (handler == null) return;

        Logger.info("Saving SkyBlock data for: " + player.getUsername() + "...");

        // Run onSave for SkyBlock data
        handler.runOnSave(player);

        // Save profile-scoped data to ProfilesDatabase
        UUID profileId = handler.getCurrentProfileId();
        ProfilesDatabase profileDb = new ProfilesDatabase(profileId.toString());
        Document newDoc = handler.toProfileDocument();

        if (profileDb.exists()) {
            ProfilesDatabase.collection.replaceOne(eq("_id", profileId.toString()), newDoc);
        } else {
            ProfilesDatabase.collection.insertOne(newDoc);
        }

        if(player.getSkyBlockIsland() != null && HypixelConst.getTypeLoader().getType() == ServerType.SKYBLOCK_ISLAND)
            player.getSkyBlockIsland().runVacantCheck();

        // Evict from SkyBlock cache
        SkyBlockDataHandler.skyBlockCache.remove(playerUuid);

        Logger.info("Successfully saved SkyBlock (profile " + profileId + ") for: " + player.getUsername());
    }
}
