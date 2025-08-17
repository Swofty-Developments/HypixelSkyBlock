package net.swofty.type.island.events.traditional;

import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.PlayerHand;
import net.minestom.server.event.player.PlayerEntityInteractEvent;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.event.HypixelEventHandler;
import net.swofty.type.island.gui.GUIJerry;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import net.swofty.type.skyblockgeneric.event.custom.JerryClickedEvent;

public class ActionPlayerClickedJerryNPC implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.PLAYER , requireDataLoaded = true)
    public void run(PlayerEntityInteractEvent event) {
        final SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        if (event.getHand() != PlayerHand.MAIN) return;
        if (event.getTarget().getEntityType() != EntityType.VILLAGER) return;

        JerryClickedEvent jerryClickedEvent = new JerryClickedEvent(player);
        HypixelEventHandler.callCustomEvent(jerryClickedEvent);

        if (jerryClickedEvent.isCancelled()) return;

        new GUIJerry().open(player);
    }
}
