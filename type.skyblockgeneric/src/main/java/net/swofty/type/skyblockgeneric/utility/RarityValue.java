package net.swofty.type.skyblockgeneric.utility;

import net.swofty.commons.skyblock.item.Rarity;

public record RarityValue<T>(T common, T uncommon, T rare, T epic, T legendary, T rest) {

    public T getForRarity(Rarity rarity) {
        return switch (rarity) {
            case COMMON -> common;
            case UNCOMMON -> uncommon;
            case RARE -> rare;
            case EPIC -> epic;
            case LEGENDARY -> legendary;
            default -> rest;
        };
    }

    public static RarityValue<Integer> zeroInteger() {
        return new RarityValue<>(0, 0, 0, 0, 0, 0);
    }

    public static RarityValue<Double> zeroDouble() {
        return new RarityValue<>(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
    }

    public static RarityValue<Integer> singleInteger(Integer i) {
        return new RarityValue<>(i, i, i, i, i, i);
    }

    public static RarityValue<Double> singleDouble(Double d) {
        return new RarityValue<>(d, d, d, d, d, d);
    }
}