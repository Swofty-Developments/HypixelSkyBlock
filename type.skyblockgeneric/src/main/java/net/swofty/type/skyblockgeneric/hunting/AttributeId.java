package net.swofty.type.skyblockgeneric.hunting;

import org.jspecify.annotations.NonNull;

import java.util.Locale;

public record AttributeId(AttributeRarity rarity, int number) {
    public static AttributeId parse(String value) {
        if (value == null || value.length() < 2) throw new IllegalArgumentException("Invalid attribute id: " + value);
        AttributeRarity rarity = switch (Character.toUpperCase(value.charAt(0))) {
            case 'C' -> AttributeRarity.COMMON;
            case 'U' -> AttributeRarity.UNCOMMON;
            case 'R' -> AttributeRarity.RARE;
            case 'E' -> AttributeRarity.EPIC;
            case 'L' -> AttributeRarity.LEGENDARY;
            default -> throw new IllegalArgumentException("Invalid attribute id: " + value);
        };
        return new AttributeId(rarity, Integer.parseInt(value.substring(1)));
    }

    @Override
    public @NonNull String toString() {
        return rarity.name().substring(0, 1).toUpperCase(Locale.ROOT) + number;
    }
}
