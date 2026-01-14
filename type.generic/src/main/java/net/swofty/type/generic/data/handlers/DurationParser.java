package net.swofty.type.generic.data.handlers;

import java.util.Locale;

/**
 * Parses Lynx-like durations: 1d, 3d, 14d, 2h, 30m, 1d12h, 2d6h30m
 * Returns duration in milliseconds.
 */
public final class DurationParser {
    private DurationParser() {}

    public static long parseToMs(String raw) {
        if (raw == null) throw new IllegalArgumentException("Duration is null");
        String s = raw.trim().toLowerCase(Locale.ROOT);
        if (s.isEmpty()) throw new IllegalArgumentException("Duration is empty");

        long totalMs = 0L;
        int i = 0;

        while (i < s.length()) {
            // read number
            int startNum = i;
            while (i < s.length() && Character.isDigit(s.charAt(i))) i++;
            if (startNum == i) throw new IllegalArgumentException("Expected number at: " + s.substring(i));

            long val = Long.parseLong(s.substring(startNum, i));

            if (i >= s.length()) throw new IllegalArgumentException("Missing duration unit in: " + s);

            char unit = s.charAt(i);
            i++;

            switch (unit) {
                case 'd' -> totalMs += val * 24L * 60L * 60L * 1000L;
                case 'h' -> totalMs += val * 60L * 60L * 1000L;
                case 'm' -> totalMs += val * 60L * 1000L;
                case 's' -> totalMs += val * 1000L;
                default -> throw new IllegalArgumentException("Unknown duration unit '" + unit + "' in: " + s);
            }
        }

        if (totalMs <= 0) throw new IllegalArgumentException("Duration must be > 0: " + raw);
        return totalMs;
    }
}
