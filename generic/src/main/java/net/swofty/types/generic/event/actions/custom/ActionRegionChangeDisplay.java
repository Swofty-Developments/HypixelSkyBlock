package net.swofty.types.generic.event.actions.custom;

import net.minestom.server.event.Event;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.StatisticDisplayReplacement;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.custom.PlayerRegionChangeEvent;

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
        }
    }
}
