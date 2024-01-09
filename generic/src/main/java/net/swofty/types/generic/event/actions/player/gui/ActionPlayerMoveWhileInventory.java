package net.swofty.types.generic.event.actions.player.gui;

import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.EventParameters;
import net.swofty.types.generic.event.SkyBlockEvent;

@EventParameters(description = "Triggers when the player moves with an inventory open",
        node = EventNodes.PLAYER,
        requireDataLoaded = true)
public class ActionPlayerMoveWhileInventory extends SkyBlockEvent {

    @Override
    public Class<? extends Event> getEvent() {
        return PlayerMoveEvent.class;
    }

    @Override
    public void run(Event event) {
        PlayerMoveEvent playerMoveEvent = (PlayerMoveEvent) event;
        final SkyBlockPlayer player = (SkyBlockPlayer) playerMoveEvent.getPlayer();

        if (player.getOpenInventory() != null) {
            // player.closeInventory();
            // player.sendMessage("§cYou cannot open an inventory while moving!");
        }
    }
}