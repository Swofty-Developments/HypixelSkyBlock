package net.swofty.commons.skyblock.event.actions.player.npc;

import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerEntityInteractEvent;
import net.swofty.commons.skyblock.event.SkyBlockEvent;
import net.swofty.commons.skyblock.user.SkyBlockPlayer;
import net.swofty.commons.skyblock.event.EventNodes;
import net.swofty.commons.skyblock.event.EventParameters;
import net.swofty.commons.skyblock.event.custom.JerryClickedEvent;

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
