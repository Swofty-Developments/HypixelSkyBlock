package net.swofty.types.generic.calendar;

import lombok.Getter;
import lombok.Setter;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.TaskSchedule;

import java.util.Arrays;
import java.util.List;

public final class SkyBlockCalendar {
    private static final String DAY_SYMBOL = "☀";
    private static final String NIGHT_SYMBOL = "☽";

    private static final int YEAR = 8928000;
    private static final int MONTH = 744000;
    private static final int DAY = 24000;
    private static final int HOUR = 1000;

    public static final int INTEREST_INTERVAL = 31;
    public static final double INTEREST_RATE = 0.02;

    private static final List<String> MONTH_NAMES = Arrays.asList("Early Spring", "Spring",
            "Late Spring", "Early Summer", "Summer", "Late Summer", "Early Autumn",
            "Autumn", "Late Autumn", "Early Winter", "Winter", "Late Winter");

    @Getter
    @Setter
    private static long elapsed = 0L;

    public static void tick(Scheduler scheduler) {
        scheduler.submitTask(() -> {
            elapsed += 10L;
            checkForEvents(getElapsed() % YEAR);
            return TaskSchedule.tick(10);
        });
    }

    public static Integer getHoursUntilNextInterest() {
        // Interest happens every 31 hours
        return (int) (INTEREST_INTERVAL - (getElapsed() % DAY) / 1000);
    }

    public static Long getDifferenceInHours(long time) {
        return (getElapsed() - time) / HOUR;
    }

    public static int getYear() {
        return (int) (getElapsed() / YEAR) + 1;
    }

    public static int getMonth() {
        return ((int) (getElapsed() / MONTH) % 12) + 1;
    }

    public static int getMonth(long elapsed) {
        return ((int) (elapsed / MONTH) % 12) + 1;
    }

    public static int getDay() {
        return ((int) (getElapsed() / DAY) % 31) + 1;
    }

    public static int getHour() {
        int currentTime = (int) ((getElapsed() % DAY) - 6000);
        if (currentTime < 0) currentTime += DAY; // Adjust if before 6:00AM
        int hours = 6 + (currentTime / 1000); // 1 hour = 1000 units in elapsed time
        if (hours >= 24) hours -= 24; // Adjust if the time is past midnight
        return hours;
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
        List<CalendarEvent> eventsAtTime = CalendarEvent.getCurrentEvents(time);
        for (CalendarEvent event : eventsAtTime) {
            event.action().accept(time);
        }
    }

    public static String getMonthName(long elapsed) {
        return getMonthName((int) ((elapsed / MONTH) % 12) + 1);
    }

    public static int getDay(long elapsed) {
        return ((int) (elapsed / DAY) % 31) + 1;
    }

    public static String getDisplay(long elapsed) {
        boolean isDaytime = true;
        int currentTime = (int) ((elapsed % DAY) - 6000);
        if (currentTime < 0) currentTime += DAY;
        int hours = 6 + (currentTime / 1000);
        int minutes = (currentTime % 1000) * 60 / 1000; // Corrected minutes calculation
        String formattedMinutes = String.format("%02d", minutes);
        if (hours >= 24) hours -= 24;
        if (hours < 6 || hours >= 20) isDaytime = false;

        String timePeriod = hours >= 12 ? "pm" : "am";
        hours = hours > 12 ? hours - 12 : (hours == 0 ? 12 : hours);

        String symbol = isDaytime ? "§e" + DAY_SYMBOL : "§b" + NIGHT_SYMBOL;
        return String.format("%d:%s%s %s", hours, formattedMinutes, timePeriod, symbol);
    }
}
