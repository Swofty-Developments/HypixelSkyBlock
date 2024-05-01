package net.swofty.types.generic.event.actions.player.gui;

import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.event.SkyBlockEvent;

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