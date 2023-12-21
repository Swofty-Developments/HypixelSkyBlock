package net.swofty.event.actions.custom;

import net.minestom.server.event.Event;
import net.swofty.event.EventNodes;
import net.swofty.event.EventParameters;
import net.swofty.event.SkyBlockEvent;
import net.swofty.event.custom.PlayerRegionChangeEvent;
import net.swofty.user.SkyBlockPlayer;
import net.swofty.user.statistics.StatisticDisplayReplacement;

@EventParameters(description = "Handles the display of changing regions",
        node = EventNodes.CUSTOM,
        requireDataLoaded = false)
public class ActionRegionChangeDisplay extends SkyBlockEvent {
    @Override
    public Class<? extends Event> getEvent() {
        return PlayerRegionChangeEvent.class;
    }

    @Override
    public void run(Event event) {
        PlayerRegionChangeEvent regionChangeEvent = (PlayerRegionChangeEvent) event;
        SkyBlockPlayer player = regionChangeEvent.getPlayer();

        if (regionChangeEvent.getTo() != null && regionChangeEvent.getFrom() != null
                && !regionChangeEvent.getTo().equals(regionChangeEvent.getFrom())) {
            player.setDisplayReplacement(StatisticDisplayReplacement
                    .builder()
                    .ticksToLast(20)
                    .display(regionChangeEvent.getTo().getColor() + " ⏣ " + regionChangeEvent.getTo().getName())
                    .build(), StatisticDisplayReplacement.DisplayType.DEFENSE
            );
            return;
        }
    }
}
