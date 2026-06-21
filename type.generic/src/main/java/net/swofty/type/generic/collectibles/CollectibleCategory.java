package net.swofty.type.generic.collectibles;

import lombok.Getter;

import java.util.EnumSet;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

@Getter
public enum CollectibleCategory {
    PROJECTILE_TRAILS("Projectile Trails"),
    VICTORY_DANCES("Victory Dances"),
    FINAL_KILL_EFFECTS("Final Kill Effects"),
    SPRAYS("Sprays"),
    ISLAND_TOPPERS("Island Toppers"),
    DEATH_CRIES("Death Cries"),
    SHOPKEEPER_SKINS("Shopkeeper Skins"),
    KILL_MESSAGES("Kill Messages"),
    GLYPHS("Glyphs"),
    BED_DESTROYS("Bed Destroys"),
    WOOD_SKINS("Wood Skins"),
    FIGURINES("Figurines"),
    PRESTIGE_SCHEMES("Prestige Schemes"),
    PRESTIGE_STARS("Prestige Stars"),
    PRESTIGE_BRACKETS("Prestige Brackets");

    private static final EnumSet<CollectibleCategory> BEDWARS_CATEGORIES = EnumSet.of(
        PROJECTILE_TRAILS,
        VICTORY_DANCES,
        FINAL_KILL_EFFECTS,
        SPRAYS,
        ISLAND_TOPPERS,
        DEATH_CRIES,
        SHOPKEEPER_SKINS,
        KILL_MESSAGES,
        GLYPHS,
        BED_DESTROYS,
        WOOD_SKINS,
        FIGURINES,
        PRESTIGE_SCHEMES,
        PRESTIGE_STARS,
        PRESTIGE_BRACKETS
    );

    private final String displayName;

    CollectibleCategory(String displayName) {
        this.displayName = displayName;
    }

    public static Optional<CollectibleCategory> fromKey(String key) {
        if (key == null || key.isBlank()) {
            return Optional.empty();
        }
        try {
            return Optional.of(CollectibleCategory.valueOf(key.trim().toUpperCase(Locale.ROOT)));
        } catch (IllegalArgumentException exception) {
            return Optional.empty();
        }
    }

    public static Set<CollectibleCategory> bedWarsCategories() {
        return EnumSet.copyOf(BEDWARS_CATEGORIES);
    }
}
