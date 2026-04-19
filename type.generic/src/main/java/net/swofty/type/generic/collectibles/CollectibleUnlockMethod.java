package net.swofty.type.generic.collectibles;

import java.util.Locale;

public enum CollectibleUnlockMethod {
    FREE,
    RANK,
    MANUAL,
    CURRENCY,
    CUSTOM;

    public static CollectibleUnlockMethod fromString(String value, CollectibleUnlockMethod fallback) {
        if (value == null || value.isBlank()) {
            return fallback;
        }
        try {
            return CollectibleUnlockMethod.valueOf(value.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException exception) {
            return fallback;
        }
    }
}
