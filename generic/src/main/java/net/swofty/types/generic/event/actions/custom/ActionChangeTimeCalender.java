package net.swofty.types.generic.event.actions.custom;

import net.minestom.server.event.Event;
import net.swofty.types.generic.SkyBlockConst;
import net.swofty.types.generic.SkyBlockGenericLoader;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.EventParameters;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.custom.CalenderHourlyUpdateEvent;

@EventParameters(description = "Updates hourly from the calender",
        node = EventNodes.CUSTOM,
        requireDataLoaded = true)
public class ActionChangeTimeCalender extends SkyBlockEvent {

    @Override
    public Class<? extends Event> getEvent() {
        return CalenderHourlyUpdateEvent.class;
    }

    @Override
    public void run(Event event) {
        CalenderHourlyUpdateEvent e = (CalenderHourlyUpdateEvent) event;

        long time = (e.getHour() * 1000L);
        if (time > 18000) { // if it is past midnight in Minecraft time
            time = time - 24000; // subtract a full day to get the time in the new day
        } else if (time < 0) { // if the time is before sunrise
            time = 24000 + time; // add a full day to move into the previous day
        }

        SkyBlockConst.getInstanceContainer().setTime(time);
        SkyBlockGenericLoader.getLoadedPlayers().forEach(player -> {
            player.sendPacket(player.getInstance().createTimePacket());
        });
    }
}
