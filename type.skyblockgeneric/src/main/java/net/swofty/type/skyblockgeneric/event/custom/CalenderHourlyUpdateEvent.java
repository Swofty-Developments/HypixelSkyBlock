package net.swofty.type.skyblockgeneric.event.custom;

import lombok.Getter;
import net.minestom.server.event.Event;

@Getter
public class CalenderHourlyUpdateEvent implements Event {

    private final int hour;

    public CalenderHourlyUpdateEvent(int hour) {
        this.hour = hour;
    }
}
