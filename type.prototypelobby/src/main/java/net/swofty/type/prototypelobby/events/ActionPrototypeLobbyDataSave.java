package net.swofty.type.prototypelobby.events;

import lombok.SneakyThrows;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.swofty.type.generic.data.mongodb.UserDatabase;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.prototypelobby.PrototypeLobbyDataHandler;
import org.tinylog.Logger;

import java.util.UUID;

public class ActionPrototypeLobbyDataSave implements HypixelEventClass {
    @SneakyThrows
    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = false, isAsync = true)
    public void run(PlayerDisconnectEvent event) {
        final HypixelPlayer player = (HypixelPlayer) event.getPlayer();
        UUID uuid = player.getUuid();

        Logger.info("Saving PrototypeLobby account data for: " + player.getUsername() + "...");

        PrototypeLobbyDataHandler handler = PrototypeLobbyDataHandler.getUser(player);

        if (handler == null) return;

        // Run onSave callbacks for basic Hypixel functionality
        handler.runOnSave(player);

        UserDatabase userDatabase = new UserDatabase(uuid);
        userDatabase.saveData(handler);

        // Remove from cache
        PrototypeLobbyDataHandler.userCache.remove(uuid);

        Logger.info("Successfully saved PrototypeLobby account data for: " + player.getUsername());
    }
}