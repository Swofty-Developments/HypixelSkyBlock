package net.swofty.types.generic.event.custom;

import lombok.Getter;
import net.minestom.server.event.Event;

@Getter
public class CalenderHourlyUpdateEvent implements Event {

    private int hour = 0;

    public CalenderHourlyUpdateEvent(int hour) {
        this.hour = hour;
    }
}
