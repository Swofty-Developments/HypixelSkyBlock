package net.swofty.commons.skyblock.item.attribute.attributes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.swofty.commons.skyblock.item.attribute.ItemAttribute;
import net.swofty.commons.skyblock.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

/**
 * Item attribute that stores potion effect data on potion items.
 * This allows potions to remember their effect type, level, duration, and modifiers.
 */
public class ItemAttributePotionData extends ItemAttribute<ItemAttributePotionData.PotionData> {

    @Override
    public String getKey() {
        return "potion_data";
    }

    @Override
    public PotionData getDefaultValue(@Nullable ItemStatistics defaultStatistics) {
        return null;
    }

    @Override
    public PotionData loadFromString(String string) {
        if (string == null || string.isEmpty() || string.equals("null")) {
            return null;
        }

        String[] parts = string.split(":");
        if (parts.length < 3) return null;

        try {
            String effectType = parts[0];
            int level = Integer.parseInt(parts[1]);
            int baseDurationSeconds = Integer.parseInt(parts[2]);
            boolean isSplash = parts.length > 3 && Boolean.parseBoolean(parts[3]);
            boolean isExtended = parts.length > 4 && Boolean.parseBoolean(parts[4]);

            return new PotionData(effectType, level, baseDurationSeconds, isSplash, isExtended);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public String saveIntoString() {
        if (getValue() == null) return "null";
        PotionData data = getValue();
        return data.effectType + ":" +
               data.level + ":" +
               data.baseDurationSeconds + ":" +
               data.isSplash + ":" +
               data.isExtended;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PotionData {
        private String effectType;
        private int level;
        private int baseDurationSeconds; // Base duration before Alchemy/splash modifiers
        private boolean isSplash;
        private boolean isExtended;

        /**
         * Create a simple potion data with default flags
         */
        public PotionData(String effectType, int level, int baseDurationSeconds) {
            this(effectType, level, baseDurationSeconds, false, false);
        }

        /**
         * Calculate final duration in milliseconds with Alchemy skill bonus
         * @param alchemyLevel Player's Alchemy skill level (0-50)
         * @return Duration in milliseconds
         */
        public long getFinalDurationMs(int alchemyLevel) {
            if (baseDurationSeconds <= 0) return 0; // Instant potions

            // Alchemy gives +1% duration per level (max +50%)
            double alchemyBonus = 1.0 + (Math.min(alchemyLevel, 50) * 0.01);

            // Splash potions have half duration (unless extended modifier was used)
            double splashMultiplier = (isSplash && !isExtended) ? 0.5 : 1.0;

            return (long) (baseDurationSeconds * 1000 * alchemyBonus * splashMultiplier);
        }

        /**
         * Get the final duration in seconds with Alchemy bonus
         */
        public int getFinalDurationSeconds(int alchemyLevel) {
            return (int) (getFinalDurationMs(alchemyLevel) / 1000);
        }

        /**
         * Get formatted duration string (e.g., "3:00" or "45s")
         */
        public String getFormattedDuration(int alchemyLevel) {
            int totalSeconds = getFinalDurationSeconds(alchemyLevel);
            if (totalSeconds <= 0) return "Instant";

            int minutes = totalSeconds / 60;
            int seconds = totalSeconds % 60;

            if (minutes > 0) {
                return minutes + ":" + String.format("%02d", seconds);
            } else {
                return seconds + "s";
            }
        }

        /**
         * Get the display name for this potion
         */
        public String getDisplayName() {
            String prefix = isSplash ? "Splash " : "";
            String levelRoman = toRoman(level);
            return prefix + "Potion of " + formatEffectName(effectType) + " " + levelRoman;
        }

        /**
         * Get just the effect display (e.g., "Speed V")
         */
        public String getEffectDisplay() {
            return formatEffectName(effectType) + " " + toRoman(level);
        }

        /**
         * Format effect type name for display (FIRE_RESISTANCE -> Fire Resistance)
         */
        private static String formatEffectName(String effectType) {
            if (effectType == null) return "Unknown";
            String[] words = effectType.toLowerCase().split("_");
            StringBuilder result = new StringBuilder();
            for (String word : words) {
                if (!word.isEmpty()) {
                    if (result.length() > 0) result.append(" ");
                    result.append(Character.toUpperCase(word.charAt(0)));
                    if (word.length() > 1) {
                        result.append(word.substring(1));
                    }
                }
            }
            return result.toString();
        }

        /**
         * Convert integer to Roman numeral
         */
        private static String toRoman(int level) {
            String[] romans = {"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"};
            if (level >= 1 && level <= 10) {
                return romans[level];
            }
            return String.valueOf(level);
        }

        /**
         * Create a copy with updated level
         */
        public PotionData withLevel(int newLevel) {
            return new PotionData(effectType, newLevel, baseDurationSeconds, isSplash, isExtended);
        }

        /**
         * Create a copy with updated duration
         */
        public PotionData withDuration(int newDurationSeconds) {
            return new PotionData(effectType, level, newDurationSeconds, isSplash, isExtended);
        }

        /**
         * Create a copy as splash potion
         */
        public PotionData asSplash(boolean preserveDuration) {
            return new PotionData(effectType, level, baseDurationSeconds, true, preserveDuration);
        }

        /**
         * Check if this is an instant effect (healing, damage, stamina)
         */
        public boolean isInstant() {
            return baseDurationSeconds <= 0;
        }
    }
}
