package net.swofty.event.actions.custom;

import net.minestom.server.event.Event;
import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;
import net.swofty.event.EventNodes;
import net.swofty.event.EventParameters;
import net.swofty.event.SkyBlockEvent;
import net.swofty.event.custom.PlayerRegionChange;
import net.swofty.region.SkyBlockRegion;
import net.swofty.user.SkyBlockPlayer;
import net.swofty.user.statistics.StatisticDisplayReplacement;
import net.swofty.utility.Groups;

@EventParameters(description = "Handles the display of changing regions",
        node = EventNodes.CUSTOM,
        requireDataLoaded = false)
public class ActionRegionChangeDisplay extends SkyBlockEvent {
    @Override
    public Class<? extends Event> getEvent() {
        return PlayerRegionChange.class;
    }

    @Override
    public void run(Event event) {
        PlayerRegionChange regionChangeEvent = (PlayerRegionChange) event;
        SkyBlockPlayer player = regionChangeEvent.getPlayer();

        if (regionChangeEvent.getTo() != null && regionChangeEvent.getFrom() != null
                && !regionChangeEvent.getTo().getType().equals(regionChangeEvent.getFrom().getType())) {
            player.setDisplayReplacement(StatisticDisplayReplacement
                    .builder()
                    .ticksToLast(20)
                    .display(regionChangeEvent.getTo().getType().getColor() + " ⏣ " + regionChangeEvent.getTo().getType().getName())
                    .build(), StatisticDisplayReplacement.DisplayType.DEFENSE
            );
            return;
        }
    }
}
