package net.swofty.type.skyblockgeneric.event.actions.player;

import net.minestom.server.event.player.PlayerSpawnEvent;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.user.HypixelPlayer;

public class ActionClearPendingBazaar implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.PLAYER , requireDataLoaded = true)
    public void run(PlayerSpawnEvent event) {
        HypixelPlayer player = (HypixelPlayer) event.getPlayer();
        player.getBazaarConnector().processAllPendingTransactions();
    }
}
