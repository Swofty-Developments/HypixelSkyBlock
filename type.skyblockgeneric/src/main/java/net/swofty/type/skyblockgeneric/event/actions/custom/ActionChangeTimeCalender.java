package net.swofty.type.skyblockgeneric.event.actions.custom;

import net.swofty.type.skyblockgeneric.SkyBlockGenericLoader;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.skyblockgeneric.event.custom.CalenderHourlyUpdateEvent;

public class ActionChangeTimeCalender implements HypixelEventClass {
    @HypixelEvent(node = EventNodes.CUSTOM , requireDataLoaded = true)
    public void run(CalenderHourlyUpdateEvent event) {

        long time = (event.getHour() * 1000L);
        if (time > 18000) { // if it is past midnight in Minecraft time
            time = time - 24000; // subtract a full day to get the time in the new day
        } else if (time < 0) { // if the time is before sunrise
            time = 24000 + time; // add a full day to move into the previous day
        }

        SkyBlockGenericLoader.getLoadedPlayers().forEach(player -> {
            player.sendPacket(player.getInstance().createTimePacket());
        });
    }
}
