package net.swofty.type.generic.collectibles;

import lombok.Getter;

import java.util.Locale;

@Getter
public enum CollectibleRarity {
    COMMON("COMMON", "§a", 1),
    RARE("RARE", "§9", 2),
    EPIC("EPIC", "§5", 3),
    LEGENDARY("LEGENDARY", "§6", 4),
    MYTHIC("MYTHIC", "§d", 5);

    private final String displayName;
    private final String colorCode;
    private final int weight;

    CollectibleRarity(String displayName, String colorCode, int weight) {
        this.displayName = displayName;
        this.colorCode = colorCode;
        this.weight = weight;
    }

    public String formattedName() {
        return colorCode + displayName;
    }

    public static CollectibleRarity fromString(String value, CollectibleRarity fallback) {
        if (value == null || value.isBlank()) {
            return fallback;
        }
        try {
            return CollectibleRarity.valueOf(value.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException exception) {
            return fallback;
        }
    }
}
