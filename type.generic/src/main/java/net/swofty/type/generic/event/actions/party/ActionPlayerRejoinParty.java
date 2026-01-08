package net.swofty.type.generic.event.actions.party;

import lombok.SneakyThrows;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.swofty.commons.ServiceType;
import net.swofty.proxyapi.ProxyService;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.party.PartyManager;
import net.swofty.type.generic.user.HypixelPlayer;

public class ActionPlayerRejoinParty implements HypixelEventClass {

    @SneakyThrows
    @HypixelEvent(node = EventNodes.PLAYER_DATA, requireDataLoaded = false, isAsync = true)
    public void run(AsyncPlayerConfigurationEvent event) {
        final HypixelPlayer player = (HypixelPlayer) event.getPlayer();

        ProxyService partyService = new ProxyService(ServiceType.PARTY);
        if (!partyService.isOnline().join()) {
            return;
        }

        // Check if player is in a party (they might have a pending disconnect timer)
        if (PartyManager.isInParty(player)) {
            // Notify the service that this player has rejoined
            // This will cancel any pending disconnect timer
            PartyManager.notifyPlayerRejoin(player.getUuid());
        }
    }
}
