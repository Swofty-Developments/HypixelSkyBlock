package net.swofty.type.skyblockgeneric.event.actions.custom;

import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.skyblockgeneric.event.custom.PlayerRegionChangeEvent;
import net.swofty.type.skyblockgeneric.user.SkyBlockActionBar;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public class ActionRegionChangeDisplay implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.CUSTOM , requireDataLoaded = false)
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
