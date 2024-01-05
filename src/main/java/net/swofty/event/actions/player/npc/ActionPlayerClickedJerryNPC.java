package net.swofty.event.actions.player.npc;

import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerEntityInteractEvent;
import net.swofty.event.EventNodes;
import net.swofty.event.EventParameters;
import net.swofty.event.SkyBlockEvent;
import net.swofty.event.custom.JerryClickedEvent;
import net.swofty.user.SkyBlockPlayer;

@EventParameters(description = "Checks to see if a player clicks on Jerry",
        node = EventNodes.PLAYER,
        validLocations = EventParameters.Location.ISLAND,
        requireDataLoaded = true)
public class ActionPlayerClickedJerryNPC extends SkyBlockEvent {
    @Override
    public Class<? extends Event> getEvent() {
        return PlayerEntityInteractEvent.class;
    }

    @Override
    public void run(Event tempEvent) {
        PlayerEntityInteractEvent event = (PlayerEntityInteractEvent) tempEvent;
        final SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        if (event.getHand() != Player.Hand.MAIN) return;

        if (event.getTarget().getEntityType() != EntityType.VILLAGER) return;

        JerryClickedEvent jerryClickedEvent = new JerryClickedEvent(player);
        SkyBlockEvent.callSkyBlockEvent(jerryClickedEvent);

        if (jerryClickedEvent.isCancelled()) return;

        player.sendMessage("§eTesting");
    }
}
