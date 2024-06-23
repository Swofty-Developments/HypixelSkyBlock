package net.swofty.types.generic.event.actions.custom;

import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.SkyBlockEventClass;
import net.swofty.types.generic.event.custom.PlayerRegionChangeEvent;
import net.swofty.types.generic.user.SkyBlockActionBar;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class ActionRegionChangeDisplay implements SkyBlockEventClass {

    @SkyBlockEvent(node = EventNodes.CUSTOM , requireDataLoaded = false)
    public void run(PlayerRegionChangeEvent event) {
        SkyBlockPlayer player = event.getPlayer();

        if (event.getTo() != null && event.getFrom() != null
                && !event.getTo().equals(event.getFrom())) {

            SkyBlockActionBar.getFor(player).addReplacement(
                    SkyBlockActionBar.BarSection.DEFENSE,
                    new SkyBlockActionBar.DisplayReplacement(
                            event.getTo().getColor() + " ⏣ " + event.getTo().getName(),
                            20,
                            2
                    )
            );
        }
    }
}
