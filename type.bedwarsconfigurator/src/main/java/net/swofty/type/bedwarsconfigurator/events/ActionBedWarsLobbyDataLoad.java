package net.swofty.type.bedwarsconfigurator.events;

import lombok.SneakyThrows;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.swofty.type.generic.data.handlers.BedWarsDataHandler;
import net.swofty.type.generic.data.mongodb.UserDatabase;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.event.phase.EventPhase;
import net.swofty.type.generic.event.phase.PhasedEvent;
import net.swofty.type.generic.user.HypixelPlayer;
import org.bson.Document;
import org.tinylog.Logger;

import java.util.UUID;

public class ActionBedWarsLobbyDataLoad implements HypixelEventClass {

    @SneakyThrows
    @PhasedEvent(node = EventNodes.PLAYER_DATA, requireDataLoaded = false, phase = EventPhase.LOAD_DATA, order = 10)
    public void run(AsyncPlayerConfigurationEvent event) {
        Logger.info("Loading BedWars data for player: " + event.getPlayer().getUsername() + "...");

        final HypixelPlayer player = (HypixelPlayer) event.getPlayer();
        UUID playerUuid = player.getUuid();

        BedWarsDataHandler handler = BedWarsDataHandler.initUserWithDefaultData(playerUuid);
        handler.loadBackedData();
        BedWarsDataHandler.bedwarsCache.put(playerUuid, handler);

        Logger.info("Successfully loaded BedWars data for player: " + player.getUsername());
    }
}
