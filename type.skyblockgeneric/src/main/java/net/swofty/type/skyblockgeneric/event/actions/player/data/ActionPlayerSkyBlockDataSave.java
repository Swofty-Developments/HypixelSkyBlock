package net.swofty.type.skyblockgeneric.event.actions.player.data;

import lombok.SneakyThrows;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.swofty.type.generic.data.DataHandler;
import net.swofty.type.generic.data.mongodb.ProfilesDatabase;
import net.swofty.type.generic.data.mongodb.UserDatabase;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.data.SkyBlockDataHandler;
import net.swofty.type.generic.user.HypixelPlayer;
import org.bson.Document;
import org.tinylog.Logger;

import java.util.UUID;

import static com.mongodb.client.model.Filters.eq;

public class ActionPlayerSkyBlockDataSave implements HypixelEventClass {

    @SneakyThrows
    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = false, isAsync = true)
    public void run(PlayerDisconnectEvent event) {
        if (!(event.getPlayer() instanceof HypixelPlayer player)) return;

        UUID playerUuid = player.getUuid();
        SkyBlockDataHandler handler = (SkyBlockDataHandler) DataHandler.userCache.get(playerUuid);

        if (handler == null) return;

        Logger.info("Saving SkyBlock (and account) data for: " + player.getUsername() + "...");

        // Run onSave for both layers
        handler.runOnSave(player);

        // Save account-wide data to UserDatabase
        UserDatabase userDb = new UserDatabase(playerUuid);
        userDb.saveHypixelData(handler.getAccount());

        // Save profile-scoped data to ProfilesDatabase
        UUID profileId = handler.getCurrentProfileId();
        ProfilesDatabase profileDb = new ProfilesDatabase(profileId.toString());
        Document newDoc = handler.toProfileDocument(profileId);

        if (profileDb.exists()) {
            // FIX: replaceOne by key, not whole doc
            ProfilesDatabase.collection.replaceOne(eq("_id", profileId.toString()), newDoc);
        } else {
            ProfilesDatabase.collection.insertOne(newDoc);
        }

        // Evict cache
        DataHandler.userCache.remove(playerUuid);

        Logger.info("Successfully saved SkyBlock (profile " + profileId + ") and account data for: " + player.getUsername());
    }
}
