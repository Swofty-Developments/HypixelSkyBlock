package net.swofty.type.skyblockgeneric.gems;

import lombok.Getter;
import net.swofty.commons.skyblock.item.Rarity;

public enum GemStats {
    // RUBY - Health
    ROUGH_RUBY(1, 2, 3, 4, 5, 7, 0),
    FLAWED_RUBY(3, 4, 5, 6, 8, 10, 0),
    FINE_RUBY(4, 5, 6, 8, 10, 14, 0),
    FLAWLESS_RUBY(5, 7, 10, 14, 18, 22, 0),
    PERFECT_RUBY(6, 9, 13, 18, 24, 30, 0),

    // AMBER - Mining Speed
    ROUGH_AMBER(4, 8, 12, 16, 20, 24, 28),
    FLAWED_AMBER(6, 10, 14, 18, 24, 30, 36),
    FINE_AMBER(10, 14, 20, 28, 36, 45, 54),
    FLAWLESS_AMBER(14, 20, 30, 44, 58, 75, 92),
    PERFECT_AMBER(20, 28, 40, 60, 80, 100, 120),

    // TOPAZ - Pristine
    ROUGH_TOPAZ(0.4, 0.4, 0.4, 0.4, 0.4, 0.4, 0.5),
    FLAWED_TOPAZ(0.8, 0.8, 0.8, 0.8, 0.8, 0.8, 0.9),
    FINE_TOPAZ(1.2, 1.2, 1.2, 1.2, 1.2, 1.2, 1.3),
    FLAWLESS_TOPAZ(1.6, 1.6, 1.6, 1.6, 1.6, 1.6, 1.8),
    PERFECT_TOPAZ(2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.2),

    // JADE - Mining Fortune
    ROUGH_JADE(2, 4, 6, 8, 10, 12, 14),
    FLAWED_JADE(3, 5, 7, 10, 14, 18, 22),
    FINE_JADE(5, 7, 10, 15, 20, 25, 30),
    FLAWLESS_JADE(7, 10, 15, 20, 27, 35, 44),
    PERFECT_JADE(10, 14, 20, 30, 40, 50, 60),

    // SAPPHIRE - Intelligence
    ROUGH_SAPPHIRE(2, 3, 4, 5, 6, 7, 0),
    FLAWED_SAPPHIRE(5, 5, 6, 7, 8, 10, 0),
    FINE_SAPPHIRE(7, 8, 9, 10, 11, 12, 0),
    FLAWLESS_SAPPHIRE(10, 11, 12, 14, 17, 20, 0),
    PERFECT_SAPPHIRE(12, 14, 17, 20, 24, 30, 0),

    // AMETHYST - Defense
    ROUGH_AMETHYST(1, 2, 3, 4, 5, 7, 0),
    FLAWED_AMETHYST(3, 4, 5, 6, 8, 10, 0),
    FINE_AMETHYST(4, 5, 6, 8, 10, 14, 0),
    FLAWLESS_AMETHYST(5, 7, 10, 14, 18, 22, 0),
    PERFECT_AMETHYST(6, 9, 13, 18, 24, 30, 0),

    // JASPER - Strength
    ROUGH_JASPER(1, 1, 1, 2, 3, 4, 0),
    FLAWED_JASPER(2, 2, 3, 4, 4, 5, 0),
    FINE_JASPER(3, 3, 4, 5, 6, 7, 0),
    FLAWLESS_JASPER(5, 6, 7, 8, 10, 12, 0),
    PERFECT_JASPER(6, 7, 9, 11, 13, 16, 0),

    // OPAL - True Defense
    ROUGH_OPAL(1, 1, 1, 2, 2, 3, 0),
    FLAWED_OPAL(2, 2, 2, 3, 3, 4, 0),
    FINE_OPAL(3, 3, 3, 4, 4, 5, 0),
    FLAWLESS_OPAL(4, 4, 5, 6, 8, 9, 0),
    PERFECT_OPAL(5, 6, 7, 9, 11, 13, 0),

    // AQUAMARINE - Fishing Speed
    ROUGH_AQUAMARINE(0.5, 0.5, 1.0, 1.0, 1.5, 2.0, 0.0),
    FLAWED_AQUAMARINE(1.0, 1.0, 1.5, 1.5, 2.0, 2.5, 0.0),
    FINE_AQUAMARINE(1.5, 1.5, 2.0, 2.0, 2.5, 3.0, 0.0),
    FLAWLESS_AQUAMARINE(2.0, 2.0, 2.5, 3.0, 3.5, 4.0, 0.0),
    PERFECT_AQUAMARINE(2.5, 2.5, 3.5, 4.0, 4.5, 5.0, 0.0),

    // CITRINE - Foraging Fortune
    ROUGH_CITRINE(1, 2, 3, 4, 5, 6, 0),
    FLAWED_CITRINE(2, 3, 4, 5, 6, 8, 0),
    FINE_CITRINE(3, 4, 6, 8, 10, 12, 0),
    FLAWLESS_CITRINE(4, 6, 8, 10, 12, 16, 0),
    PERFECT_CITRINE(6, 8, 10, 12, 16, 20, 0),

    // ONYX - Crit Damage
    ROUGH_ONYX(1, 1, 2, 2, 3, 4, 0),
    FLAWED_ONYX(2, 2, 3, 3, 4, 6, 0),
    FINE_ONYX(3, 3, 4, 5, 6, 8, 0),
    FLAWLESS_ONYX(4, 5, 6, 7, 8, 10, 0),
    PERFECT_ONYX(5, 6, 7, 8, 10, 12, 0),

    // PERIDOT - Farming Fortune
    ROUGH_PERIDOT(0.5, 1.0, 1.5, 2.0, 2.5, 3.0, 0.0),
    FLAWED_PERIDOT(1.0, 1.5, 2.0, 2.5, 3.0, 4.0, 0.0),
    FINE_PERIDOT(1.5, 2.0, 3.0, 4.0, 5.0, 6.0, 0.0),
    FLAWLESS_PERIDOT(2, 3, 4, 5, 6, 8, 0),
    PERFECT_PERIDOT(3, 4, 5, 6, 8, 10, 0);

    @Getter
    public final Double common;
    @Getter
    public final Double uncommon;
    @Getter
    public final Double rare;
    @Getter
    public final Double epic;
    @Getter
    public final Double legendary;
    @Getter
    public final Double mythic;
    @Getter
    public final Double divine;

    GemStats(Double common, Double uncommon, Double rare, Double epic, Double legendary, Double mythic, Double divine) {
        this.common = common;
        this.uncommon = uncommon;
        this.rare = rare;
        this.epic = epic;
        this.legendary = legendary;
        this.mythic = mythic;
        this.divine = divine;
    }

    GemStats(Integer common, Integer uncommon, Integer rare, Integer epic, Integer legendary, Integer mythic, Integer divine) {
        this.common = Double.valueOf(common);
        this.uncommon = Double.valueOf(uncommon);
        this.rare = Double.valueOf(rare);
        this.epic = Double.valueOf(epic);
        this.legendary = Double.valueOf(legendary);
        this.mythic = Double.valueOf(mythic);
        this.divine = Double.valueOf(divine);
    }

    public Double getFromRarity(Rarity rarity) {
        return switch (rarity) {
            case COMMON -> getCommon();
            case UNCOMMON -> getUncommon();
            case RARE -> getRare();
            case EPIC -> getEpic();
            case LEGENDARY -> getLegendary();
            case MYTHIC -> getMythic();
            case DIVINE -> getDivine();
            default -> 0.0;
        };
    }

    public static GemStats getFromGemstoneAndRarity(Gemstone gemstone, GemRarity gemRarity) {
        for (GemStats gemStatsInternal : GemStats.values()) {
            String rarity = gemRarity.name().toUpperCase();
            String type = gemstone.name().toUpperCase();

            if (gemStatsInternal.name().startsWith(rarity) && gemStatsInternal.name().endsWith(type)) {
                return gemStatsInternal;
            }
        }
        return null;
    }
}