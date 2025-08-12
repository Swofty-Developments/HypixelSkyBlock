package net.swofty.type.island.events.traditional;

import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.PlayerHand;
import net.minestom.server.event.player.PlayerEntityInteractEvent;
import net.swofty.type.island.gui.GUIJerry;
import net.swofty.type.skyblockgeneric.event.EventNodes;
import net.swofty.type.skyblockgeneric.event.SkyBlockEventClass;
import net.swofty.type.skyblockgeneric.event.SkyBlockEventHandler;
import SkyBlockPlayer;
import net.swofty.type.skyblockgeneric.event.SkyBlockEvent;
import net.swofty.type.skyblockgeneric.event.custom.JerryClickedEvent;

public class ActionPlayerClickedJerryNPC implements SkyBlockEventClass {

    @SkyBlockEvent(node = EventNodes.PLAYER , requireDataLoaded = true)
    public void run(PlayerEntityInteractEvent event) {
        final SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        if (event.getHand() != PlayerHand.MAIN) return;
        if (event.getTarget().getEntityType() != EntityType.VILLAGER) return;

        JerryClickedEvent jerryClickedEvent = new JerryClickedEvent(player);
        SkyBlockEventHandler.callSkyBlockEvent(jerryClickedEvent);

        if (jerryClickedEvent.isCancelled()) return;

        new GUIJerry().open(player);
    }
}
