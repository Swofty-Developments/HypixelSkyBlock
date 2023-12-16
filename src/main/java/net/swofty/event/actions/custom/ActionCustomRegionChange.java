package net.swofty.event.actions.custom;

import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerHandAnimationEvent;
import net.swofty.event.EventNodes;
import net.swofty.event.EventParameters;
import net.swofty.event.SkyBlockEvent;
import net.swofty.event.custom.PlayerRegionChange;
import org.tinylog.Logger;

@EventParameters(description = "Handles item ability use for left clicks",
        node = EventNodes.CUSTOM,
        requireDataLoaded = false)
public class ActionCustomRegionChange extends SkyBlockEvent {
    @Override
    public Class<? extends Event> getEvent() {
        return PlayerRegionChange.class;
    }

    @Override
    public void run(Event event) {

    }
}
