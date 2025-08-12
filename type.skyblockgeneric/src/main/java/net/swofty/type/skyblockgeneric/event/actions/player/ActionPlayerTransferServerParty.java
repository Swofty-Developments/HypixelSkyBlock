package net.swofty.type.skyblockgeneric.event.actions.player;

import lombok.SneakyThrows;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.swofty.commons.ServiceType;
import net.swofty.proxyapi.ProxyService;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.skyblockgeneric.party.PartyManager;
import SkyBlockPlayer;
import net.swofty.type.generic.utility.MathUtility;

public class ActionPlayerTransferServerParty implements HypixelEventClass {

    @SneakyThrows
    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = false, isAsync = true)
    public void run(PlayerDisconnectEvent event) {
        final SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();
        MathUtility.delay(() -> {
            ProxyService partyService = new ProxyService(ServiceType.PARTY);
            if (!partyService.isOnline().join()) {
                return;
            }
            PartyManager.switchPartyServer(player);
        }, 40);
    }
}
