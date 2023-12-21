package net.swofty.event.actions.player.gui;

import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.swofty.event.EventNodes;
import net.swofty.event.EventParameters;
import net.swofty.event.SkyBlockEvent;
import net.swofty.user.SkyBlockPlayer;

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