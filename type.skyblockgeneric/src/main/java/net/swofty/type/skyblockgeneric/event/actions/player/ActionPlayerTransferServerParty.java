package net.swofty.type.skyblockgeneric.event.actions.player;

import lombok.SneakyThrows;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.swofty.commons.ServiceType;
import net.swofty.proxyapi.ProxyPlayer;
import net.swofty.proxyapi.ProxyService;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.party.PartyManager;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import net.swofty.type.generic.utility.MathUtility;

public class ActionPlayerTransferServerParty implements HypixelEventClass {

    @SneakyThrows
    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = false, isAsync = true)
    public void run(PlayerDisconnectEvent event) {
        final SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        // Wait 60 ticks (3 seconds) to give the player time to connect to a new server
        MathUtility.delay(() -> {
            ProxyService partyService = new ProxyService(ServiceType.PARTY);
            if (!partyService.isOnline().join()) {
                return;
            }

            // Check if the player is still online on the network
            ProxyPlayer proxyPlayer = new ProxyPlayer(player.getUuid());
            boolean isOnline = proxyPlayer.isOnline().join();

            if (isOnline) {
                // Player is on another server - just a server transfer
                PartyManager.switchPartyServer(player);
            } else {
                // Player is truly offline - check if they're in a party and notify
                if (PartyManager.isInParty(player)) {
                    PartyManager.notifyPlayerDisconnect(player.getUuid());
                }
            }
        }, 60);
    }
}
