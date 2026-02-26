package net.swofty.type.skyblockgeneric.event.actions.custom;

import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.skyblockgeneric.SkyBlockGenericLoader;
import net.swofty.type.skyblockgeneric.event.custom.CalenderHourlyUpdateEvent;

public class ActionChangeTimeCalender implements HypixelEventClass {
    @HypixelEvent(node = EventNodes.CUSTOM, requireDataLoaded = true)
    public void run(CalenderHourlyUpdateEvent event) {
        int hour = event.hour();
        long minecraftTime = (hour * 1000L + 6000) % 24000;

        SkyBlockGenericLoader.getLoadedPlayers().forEach(player -> {
            player.getInstance().setTime(minecraftTime);
        });
    }
}
