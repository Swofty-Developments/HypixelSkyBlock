package net.swofty.type.skyblockgeneric.potion;

import lombok.Getter;
import net.swofty.commons.skyblock.item.ItemType;
import org.jetbrains.annotations.Nullable;

/**
 * Potion modifiers that can be applied during brewing to enhance potions.
 * These include level modifiers (Glowstone), duration modifiers (Redstone),
 * and type modifiers (Gunpowder for splash).
 */
@Getter
public enum PotionModifier {
    // Level modifiers - increase potion level
    GLOWSTONE_DUST(ModifierType.LEVEL, 1, 0, ItemType.GLOWSTONE_DUST),
    ENCHANTED_GLOWSTONE_DUST(ModifierType.LEVEL, 2, 0, ItemType.ENCHANTED_GLOWSTONE_DUST),
    ENCHANTED_GLOWSTONE(ModifierType.LEVEL, 3, 0, ItemType.ENCHANTED_GLOWSTONE),

    // Enchanted Redstone Lamp is special - gives both duration AND level
    ENCHANTED_REDSTONE_LAMP(ModifierType.LEVEL_AND_DURATION, 3, 16 * 60, ItemType.ENCHANTED_REDSTONE_LAMP),

    // Duration modifiers - set potion duration
    REDSTONE(ModifierType.DURATION, 0, 8 * 60, ItemType.REDSTONE), // 8 minutes
    ENCHANTED_REDSTONE(ModifierType.DURATION, 0, 16 * 60, ItemType.ENCHANTED_REDSTONE), // 16 minutes
    ENCHANTED_REDSTONE_BLOCK(ModifierType.DURATION, 0, 40 * 60, ItemType.ENCHANTED_REDSTONE_BLOCK), // 40 minutes

    // Type modifiers - change potion type
    GUNPOWDER(ModifierType.SPLASH, 0, 0, ItemType.GUNPOWDER), // Makes splash (halves duration)
    ENCHANTED_GUNPOWDER(ModifierType.SPLASH, 0, 0, ItemType.ENCHANTED_GUNPOWDER), // Makes splash (doesn't halve duration)
    ;

    private final ModifierType type;
    private final int levelIncrease;
    private final int durationSeconds; // Duration in seconds (for DURATION type modifiers)
    private final ItemType ingredientType;

    PotionModifier(ModifierType type, int levelIncrease, int durationSeconds, ItemType ingredientType) {
        this.type = type;
        this.levelIncrease = levelIncrease;
        this.durationSeconds = durationSeconds;
        this.ingredientType = ingredientType;
    }

    /**
     * Get a modifier from an ingredient item type
     * @param itemType The item type to look up
     * @return The modifier, or null if not a modifier ingredient
     */
    public static @Nullable PotionModifier fromIngredient(ItemType itemType) {
        if (itemType == null) return null;
        for (PotionModifier modifier : values()) {
            if (modifier.ingredientType == itemType) {
                return modifier;
            }
        }
        return null;
    }

    /**
     * Check if this modifier halves the potion duration (regular Gunpowder)
     */
    public boolean halvesDuration() {
        return this == GUNPOWDER;
    }

    /**
     * Check if this modifier is a level modifier
     */
    public boolean isLevelModifier() {
        return type == ModifierType.LEVEL || type == ModifierType.LEVEL_AND_DURATION;
    }

    /**
     * Check if this modifier is a duration modifier
     */
    public boolean isDurationModifier() {
        return type == ModifierType.DURATION || type == ModifierType.LEVEL_AND_DURATION;
    }

    /**
     * Check if this modifier makes the potion a splash potion
     */
    public boolean makesSplash() {
        return type == ModifierType.SPLASH;
    }

    public enum ModifierType {
        LEVEL,              // Increases potion level
        DURATION,           // Sets duration
        LEVEL_AND_DURATION, // Both (Enchanted Redstone Lamp)
        SPLASH              // Makes splash potion
    }
}
