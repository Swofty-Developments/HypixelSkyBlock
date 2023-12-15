package net.swofty.utility.calendar;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public final class SkyBlockCalendar {
      public static final List<String> MONTH_NAMES = Arrays.asList("Early Spring", "Spring",
              "Late Spring", "Early Summer", "Summer", "Late Summer", "Early Autumn",
              "Autumn", "Late Autumn", "Early Winter", "Winter", "Late Winter");

      public static HashMap<Long[], Consumer<Long>> calendarEvent = new HashMap<>();

      public static long ELAPSED = 0L;
      public static final int YEAR = 8928000;
      public static final int MONTH = 744000;
      public static final int DAY = 24000;
      //public static final long HOUR = 50000L;

      private SkyBlockCalendar() {
      }

      public static int getYear() {
            return (int) (ELAPSED / YEAR) + 1;
      }

      public static int getMonth() {
            return ((int) (ELAPSED / MONTH) % 12) + 1;
      }

      public static int getDay() {
            return ((int) (ELAPSED / DAY) % 31) + 1;
      }

      public static String getMonthName(int month) {
            if (month < 1 || month > 12)
                  return "Unknown Month";
            return MONTH_NAMES.get(month - 1);
      }

      public static String getMonthName() {
            return getMonthName(getMonth());
      }

      public static void checkForEvents(Long time) {
            for (CalendarEvent event : CalendarEvent.values()) {
                  for (Long timE : event.getTimes()) {
                        if (time.equals(timE)) {
                              event.getAction().accept(timE);
                              return;
                        }
                  }
            }
      }
}
