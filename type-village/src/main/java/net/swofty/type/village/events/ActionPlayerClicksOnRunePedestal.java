package net.swofty.type.village.events;

import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerEntityInteractEvent;
import net.swofty.type.village.gui.GUIRunicPedestal;
import net.swofty.type.village.runes.RuneEntityImpl;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class ActionPlayerClicksOnRunePedestal extends SkyBlockEvent {
    @Override
    public Class<? extends Event> getEvent() {
        return PlayerEntityInteractEvent.class;
    }

    @Override
    public void run(Event tempEvent) {
        PlayerEntityInteractEvent event = (PlayerEntityInteractEvent) tempEvent;
        if (!(event.getTarget() instanceof RuneEntityImpl)) return;

        new GUIRunicPedestal().open((SkyBlockPlayer) event.getPlayer());
    }
}
