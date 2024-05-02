package net.swofty.types.generic.event.actions.player.data;

import com.mongodb.client.model.Filters;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.swofty.types.generic.SkyBlockGenericLoader;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointBoolean;
import net.swofty.types.generic.data.mongodb.CoopDatabase;
import net.swofty.types.generic.data.mongodb.ProfilesDatabase;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.SkyBlockEventClass;
import net.swofty.types.generic.event.SkyBlockEventHandler;
import net.swofty.types.generic.event.custom.PlayerRegionChangeEvent;
import net.swofty.types.generic.redis.RedisAuthenticate;
import net.swofty.types.generic.region.SkyBlockRegion;
import net.swofty.types.generic.user.PlayerProfiles;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.MathUtility;
import org.bson.Document;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.UUID;

public class ActionPlayerDataSpawn implements SkyBlockEventClass {

    @SkyBlockEvent(node = EventNodes.PLAYER_DATA , requireDataLoaded = false , isAsync = true)
    public void run(PlayerSpawnEvent event) {
        if (!event.isFirstSpawn()) return;
        final SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();
        if (!player.hasAuthenticated) {
            RedisAuthenticate.promptAuthentication(player);
            return;
        }

        Logger.info("Loading player data for " + event.getPlayer().getUsername() + "...");

        UUID playerUuid = player.getUuid();
        PlayerProfiles profiles = player.getProfiles();

        DataHandler handler = player.getDataHandler();

        if (profiles.getProfiles().size() >= 2) {
            try {
                UUID finalProfileId1 = profiles.getCurrentlySelected();
                Document previousProfile = ProfilesDatabase.collection
                        .find(Filters.eq("_owner", playerUuid.toString())).into(new ArrayList<>())
                        .stream().filter(document -> !document.get("_id").equals(finalProfileId1.toString()))
                        .findFirst().get();
                DataHandler previousHandler = DataHandler.fromDocument(previousProfile);
                previousHandler.getPersistentValues().forEach((key, value) -> {
                    handler.getDatapoint(key).setValue(value);
                });
            } catch (Exception e) {
                Logger.info("Failed to load previous profile data for " + player.getUsername());
                Logger.info("Probably needs to be updated to the latest version");
                e.printStackTrace();
            }
        }

        if (handler.get(DataHandler.Data.IS_COOP, DatapointBoolean.class).getValue()) {
            CoopDatabase.Coop coop = CoopDatabase.getFromMember(playerUuid);
            if (coop.members().size() != 1) {
                DataHandler data;

                if (SkyBlockGenericLoader.getLoadedPlayers().stream().anyMatch(player1 -> coop.members().contains(player1.getUuid()))) {
                    // A coop member is online, use their data
                    SkyBlockPlayer otherCoopMember = SkyBlockGenericLoader.getLoadedPlayers().stream().filter(player1 -> coop.members().contains(player1.getUuid())).findFirst().get();
                    data = otherCoopMember.getDataHandler();
                } else {
                    // No coop members are online, use the first member's data
                    UUID finalProfileId = profiles.getCurrentlySelected();
                    data = DataHandler.fromDocument(new ProfilesDatabase(coop.memberProfiles().stream().filter(
                            uuid -> !uuid.equals(finalProfileId)).findFirst().get().toString()).getDocument());
                }

                data.getCoopValues().forEach((key, value) -> {
                    handler.getDatapoint(key).setValueBypassCoop(value);
                });
            }
        }

        player.sendMessage("");

        handler.runOnLoad(player);

        // Manually call region event with a delay
        MathUtility.delay(() -> {
            SkyBlockRegion playerRegion = player.getRegion();
            if (playerRegion != null)
                SkyBlockEventHandler.callSkyBlockEvent(new PlayerRegionChangeEvent(
                        player,
                        null,
                        playerRegion.getType()
                ));
        }, 50);
    }
}
