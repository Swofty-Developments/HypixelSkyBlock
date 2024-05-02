package net.swofty.types.generic.event.actions.custom;

import net.swofty.types.generic.SkyBlockGenericLoader;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.SkyBlockEventClass;
import net.swofty.types.generic.event.custom.CalenderHourlyUpdateEvent;

public class ActionChangeTimeCalender implements SkyBlockEventClass {

    @SkyBlockEvent(node = EventNodes.CUSTOM , requireDataLoaded = true)
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
