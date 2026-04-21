package net.swofty.type.generic.collectibles;

import lombok.Getter;

import java.util.Locale;

@Getter
public enum CollectibleCurrency {
    BEDWARS_TOKENS("Tokens", "§2"),
    SKYWARS_COINS("Tokens", "§2"),
    MURDER_MYSTERY_COINS("Tokens", "§2");

    private final String displayName;
    private final String color;

    CollectibleCurrency(String displayName, String color) {
        this.displayName = displayName;
        this.color = color;
    }

    public static CollectibleCurrency fromString(String value, CollectibleCurrency fallback) {
        if (value == null || value.isBlank()) {
            return fallback;
        }
        try {
            return CollectibleCurrency.valueOf(value.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException exception) {
            return fallback;
        }
    }
}
