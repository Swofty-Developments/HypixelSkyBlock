package net.swofty.types.generic.event.actions.player.offhand;

import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerHandAnimationEvent;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.EventParameters;
import net.swofty.types.generic.event.SkyBlockEvent;

@EventParameters(description = "Handles not being able to use an offhand",
        node = EventNodes.PLAYER,
        requireDataLoaded = false)
public class ActionOffhandHandClick extends SkyBlockEvent {
    @Override
    public Class<? extends Event> getEvent() {
        return PlayerHandAnimationEvent.class;
    }

    @Override
    public void run(Event tempEvent) {
        PlayerHandAnimationEvent event = (PlayerHandAnimationEvent) tempEvent;

        if (event.getHand().equals(Player.Hand.OFF)) {
            event.getPlayer().sendMessage("§cYou cannot use your offhand!");
            event.setCancelled(true);
        }
    }
}
