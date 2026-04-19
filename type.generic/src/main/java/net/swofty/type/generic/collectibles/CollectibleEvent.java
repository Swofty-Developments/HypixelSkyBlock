package net.swofty.type.generic.collectibles;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Optional;

public enum CollectibleEvent {
    EASTER("Easter",
        LocalDate.of(2026, 1, 1),
        LocalDate.of(2026, 4, 12)
    ),
    HOLIDAY("Holiday",
        LocalDate.of(2025, 12, 31),
        LocalDate.of(2026, 2, 1)
    ),
    HALLOWEEN("Halloween",
        LocalDate.of(2026, 10, 31),
        LocalDate.of(2026, 11, 10)
    );

    private final String displayName;
    private final LocalDate start;
    private final LocalDate end;

    CollectibleEvent(String displayName, LocalDate startDate, LocalDate endDate) {
        this.displayName = displayName;
        this.start = startDate;
        this.end = endDate;
    }

    public String displayName() {
        return displayName;
    }

    public boolean isAvailableNow() {
        return isAvailableAt(LocalDate.now());
    }

    public boolean isAvailableAt(LocalDate date) {
        if (date == null || start == null || end == null) {
            return false;
        }

        // inclusive range: start <= date <= end
        return (!date.isBefore(start)) && (!date.isAfter(end));
    }

    public static Optional<CollectibleEvent> fromString(String value) {
        if (value == null || value.isBlank()) {
            return Optional.empty();
        }

        try {
            return Optional.of(CollectibleEvent.valueOf(value.trim().toUpperCase(Locale.ROOT)));
        } catch (IllegalArgumentException exception) {
            return Optional.empty();
        }
    }
}