package net.swofty.type.skyblockgeneric.event.actions.custom;

import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.event.custom.PlayerRegionChangeEvent;
import net.swofty.type.generic.user.SkyBlockActionBar;
import net.swofty.type.generic.user.HypixelPlayer;

public class ActionRegionChangeDisplay implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.CUSTOM , requireDataLoaded = false)
    public void run(PlayerRegionChangeEvent event) {
        HypixelPlayer player = event.getPlayer();

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
