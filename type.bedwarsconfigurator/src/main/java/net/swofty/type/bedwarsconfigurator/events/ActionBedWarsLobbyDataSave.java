package net.swofty.type.bedwarsconfigurator.events;

import lombok.SneakyThrows;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.swofty.type.generic.data.handlers.BedWarsDataHandler;
import net.swofty.type.generic.data.mongodb.UserDatabase;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.user.HypixelPlayer;
import org.tinylog.Logger;

import java.util.UUID;

public class ActionBedWarsLobbyDataSave implements HypixelEventClass {
    @SneakyThrows
    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = false, isAsync = true)
    public void run(PlayerDisconnectEvent event) {
        final HypixelPlayer player = (HypixelPlayer) event.getPlayer();
        UUID uuid = player.getUuid();

        Logger.info("Saving BedWars account data for: " + player.getUsername() + "...");

        BedWarsDataHandler handler = BedWarsDataHandler.getUser(player);

        if (handler == null) return;

        // Run onSave callbacks for basic Hypixel functionality
        handler.runOnSave(player);

        UserDatabase userDatabase = new UserDatabase(uuid);
        userDatabase.saveData(handler);

        // Remove from cache
        BedWarsDataHandler.userCache.remove(uuid);

        Logger.info("Successfully saved BedWars account data for: " + player.getUsername());
    }
}