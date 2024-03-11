package net.swofty.types.generic.gems;

import lombok.Getter;
import net.swofty.types.generic.item.Rarity;

public enum GemStats {
    // RUBY
    ROUGH_RUBY(1, 2, 3, 4, 5, 7),
    FLAWED_RUBY(3, 4, 5, 6, 8, 10),
    FINE_RUBY(4, 5, 6, 8, 10, 14),
    FLAWLESS_RUBY(5, 7, 10, 14, 18, 22),
    PERFECT_RUBY(6, 9, 13, 18, 24, 30),

    // AMETHYST
    ROUGH_AMETHYST(1, 2, 3, 4, 5, 7),
    FLAWED_AMETHYST(3, 4, 5, 6, 8, 10),
    FINE_AMETHYST(4, 5, 6, 8, 10, 14),
    FLAWLESS_AMETHYST(5, 7, 10, 14, 18, 22),
    PERFECT_AMETHYST(6, 9, 13, 18, 24, 30),

    // JADE
    ROUGH_JADE(0, 4, 6, 8, 10, 12),
    FLAWED_JADE(0, 5, 7, 10, 14, 18),
    FINE_JADE(0, 7, 10, 15, 20, 25),
    FLAWLESS_JADE(0, 10, 15, 20, 27, 35),
    PERFECT_JADE(0, 14, 20, 30, 40, 50),

    // SAPPHIRE
    ROUGH_SAPPHIRE(2, 2, 3, 4, 5, 6),
    FLAWED_SAPPHIRE(4, 4, 5, 6, 7, 8),
    FINE_SAPPHIRE(6, 6, 7, 8, 9, 10),
    FLAWLESS_SAPPHIRE(8, 9, 10, 12, 14, 16),
    PERFECT_SAPPHIRE(10, 12, 14, 17, 20, 25),

    // AMBER
    ROUGH_AMBER(4, 8, 12, 16, 20, 24),
    FLAWED_AMBER(6, 10, 14, 18, 24, 30),
    FINE_AMBER(10, 14, 20, 28, 36, 45),
    FLAWLESS_AMBER(14, 20, 30, 44, 58, 75),
    PERFECT_AMBER(20, 28, 40, 60, 80, 100),

    // JASPER
    ROUGH_JASPER(0, 0, 1, 1, 2, 3),
    FLAWED_JASPER(0, 0, 2, 3, 4, 0),
    FINE_JASPER(0, 0, 3, 4, 4, 5),
    FLAWLESS_JASPER(0, 0, 5, 6, 7, 8),
    PERFECT_JASPER(0, 0, 7, 9, 10, 12),
    ;

    @Getter
    public final Integer common;
    @Getter
    public final Integer uncommon;
    @Getter
    public final Integer rare;
    @Getter
    public final Integer epic;
    @Getter
    public final Integer legendary;
    @Getter
    public final Integer mythic;

    GemStats(Integer common, Integer uncommon, Integer rare, Integer epic, Integer legendary, Integer mythic) {
        this.common = common;
        this.uncommon = uncommon;
        this.rare = rare;
        this.epic = epic;
        this.legendary = legendary;
        this.mythic = mythic;
    }

    public Integer getFromRarity(GemRarity rarity) {
        return switch (rarity) {
            case ROUGH -> getCommon();
            case FLAWED -> getUncommon();
            case FINE -> getRare();
            case FLAWLESS -> getEpic();
            case PERFECT -> getLegendary();
            default -> 0;
        };
    }

    public static GemStats getFromGemstoneAndRarity(Gemstone gemstone, GemRarity gemRarity) {
        for (GemStats gemStatsInternal : GemStats.values()) {
            if (gemStatsInternal.name().startsWith(gemRarity.name.toUpperCase()) && gemStatsInternal.name().endsWith(gemstone.name().toUpperCase())) {
                return gemStatsInternal;
            }
        }
        return null;
    }
}
