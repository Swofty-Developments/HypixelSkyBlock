package net.swofty.type.generic.event.actions.data;

import lombok.SneakyThrows;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.swofty.commons.proxy.ToProxyChannels;
import net.swofty.proxyapi.redis.ServerOutboundMessage;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.data.HypixelDataHandler;
import net.swofty.type.generic.data.mongodb.UserDatabase;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.utility.MathUtility;
import org.json.JSONObject;
import org.tinylog.Logger;

import java.util.UUID;

public class ActionPlayerDataSave implements HypixelEventClass {
    @SneakyThrows
    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = false, isAsync = true)
    public void run(PlayerDisconnectEvent event) {
        final HypixelPlayer player = (HypixelPlayer) event.getPlayer();
        UUID uuid = player.getUuid();

        Logger.info("Saving Hypixel account data for: " + player.getUsername() + "...");

        HypixelDataHandler handler = player.getDataHandler();

        // Run onSave callbacks for basic Hypixel functionality
        handler.runOnSave(player);

        // Save Hypixel data to UserDatabase (account-wide data)
        UserDatabase userDatabase = new UserDatabase(uuid);
        userDatabase.saveData(handler);

        // Remove from cache
        HypixelDataHandler.userCache.remove(uuid);

        // Notify proxy that we're done with this player
        ServerOutboundMessage.sendMessageToProxy(
                ToProxyChannels.FINISHED_WITH_PLAYER,
                new JSONObject().put("uuid", uuid.toString()),
                (response) -> {}
        );

        // Clean up tablist entries
        MathUtility.delay(() -> {
            HypixelConst.getTypeLoader().getTablistManager().deleteTablistEntries(player);
        }, 5);

        Logger.info("Successfully saved Hypixel account data for: " + player.getUsername());
    }
}