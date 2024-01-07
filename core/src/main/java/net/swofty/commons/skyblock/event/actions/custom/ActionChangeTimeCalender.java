package net.swofty.commons.skyblock.event.actions.custom;

import net.minestom.server.event.Event;
import net.swofty.commons.skyblock.SkyBlock;
import net.swofty.commons.skyblock.event.EventNodes;
import net.swofty.commons.skyblock.event.EventParameters;
import net.swofty.commons.skyblock.event.SkyBlockEvent;
import net.swofty.commons.skyblock.event.custom.CalenderHourlyUpdateEvent;

@EventParameters(description = "Updates hourly from the calender",
        node = EventNodes.CUSTOM,
        validLocations = EventParameters.Location.EITHER,
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

        SkyBlock.getInstanceContainer().setTime(time);
        SkyBlock.getLoadedPlayers().forEach(player -> {
            player.sendPacket(player.getInstance().createTimePacket());
        });
    }
}
