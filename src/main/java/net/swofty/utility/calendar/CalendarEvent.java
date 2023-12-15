package net.swofty.utility.calendar;

import lombok.Getter;

import java.util.function.Consumer;

@Getter
public enum CalendarEvent {
      NEW_YEAR(new Long[] {10L}, (time) -> {
            // ya
      })
      ;

      private final Long[] times;
      private final Consumer<Long> action;

      CalendarEvent(Long[] times, Consumer<Long> action) {
            this.times = times;
            this.action = action;
      }

}
