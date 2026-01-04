package net.swofty.type.skywarslobby.level;

import net.minestom.server.item.Material;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Represents the SkyWars level progression system.
 * Contains data structures for levels, rewards, and progression tracking.
 */
public interface SkywarsLevelCategory {

    /**
     * Get all level data
     */
    SkywarsLevel[] getLevels();

    /**
     * Calculate the player's current level based on their XP
     */
    default int calculateLevel(long xp) {
        SkywarsLevel[] levels = getLevels();
        int currentLevel = 1;
        long cumulativeXP = 0;

        for (SkywarsLevel level : levels) {
            cumulativeXP += level.requirement();
            if (xp >= cumulativeXP) {
                currentLevel = level.level();
            } else {
                break;
            }
        }
        return currentLevel;
    }

    /**
     * Get the cumulative XP required for a specific level
     */
    default long getCumulativeXPForLevel(int targetLevel) {
        SkywarsLevel[] levels = getLevels();
        long cumulativeXP = 0;

        for (SkywarsLevel level : levels) {
            if (level.level() <= targetLevel) {
                cumulativeXP += level.requirement();
            } else {
                break;
            }
        }
        return cumulativeXP;
    }

    /**
     * Get progress towards the next level (0.0 to 1.0)
     */
    default double getProgressToNextLevel(long xp) {
        int currentLevel = calculateLevel(xp);
        SkywarsLevel[] levels = getLevels();

        if (currentLevel >= levels.length) {
            return 1.0; // Max level
        }

        long xpForCurrentLevel = getCumulativeXPForLevel(currentLevel);
        long xpForNextLevel = getCumulativeXPForLevel(currentLevel + 1);
        long xpIntoCurrentLevel = xp - xpForCurrentLevel;
        long xpNeededForNextLevel = xpForNextLevel - xpForCurrentLevel;

        if (xpNeededForNextLevel <= 0) return 1.0;

        return Math.min(1.0, Math.max(0.0, (double) xpIntoCurrentLevel / xpNeededForNextLevel));
    }

    /**
     * Get XP progress into the current level
     */
    default long getXPIntoCurrentLevel(long xp) {
        int currentLevel = calculateLevel(xp);
        long xpForCurrentLevel = getCumulativeXPForLevel(currentLevel);
        return xp - xpForCurrentLevel;
    }

    /**
     * Get XP needed for the next level
     */
    default long getXPForNextLevel(long xp) {
        int currentLevel = calculateLevel(xp);
        SkywarsLevel[] levels = getLevels();

        if (currentLevel >= levels.length) {
            return 0; // Max level
        }

        for (SkywarsLevel level : levels) {
            if (level.level() == currentLevel + 1) {
                return level.requirement();
            }
        }
        return 0;
    }

    /**
     * Represents a single SkyWars level with its requirements and rewards
     */
    record SkywarsLevel(
            int level,
            long requirement,
            boolean isPrestige,
            @Nullable String prestigeName,
            @Nullable String prestigeColor,
            Material material,
            @Nullable String headTexture,
            List<Reward> rewards
    ) {
        /**
         * Get the display name color code for this level
         */
        public String getDisplayColor() {
            if (isPrestige && prestigeColor != null) {
                return "§" + prestigeColor;
            }
            return "§a"; // Default green for normal unlocked levels
        }

        /**
         * Get the level type description
         */
        public String getLevelType() {
            return isPrestige ? "Prestige Level" : "Normal Level";
        }

        /**
         * Format the level emblem for display
         */
        public String getEmblem() {
            if (isPrestige && prestigeColor != null) {
                return "§" + prestigeColor + "[" + level + "\u272F]";
            }
            return "§7[" + level + "\u272F]";
        }
    }

    /**
     * Base interface for all reward types
     */
    sealed interface Reward permits CoinReward, HypixelXPReward, TokenReward, OpalReward, PrestigeSchemeReward, FeatureUnlockReward {
        String getDisplayLine();
    }

    /**
     * Coin reward
     */
    record CoinReward(int amount) implements Reward {
        @Override
        public String getDisplayLine() {
            return " §8+§6" + formatNumber(amount) + " §7SkyWars Coins";
        }
    }

    /**
     * Hypixel network XP reward
     */
    record HypixelXPReward(int amount) implements Reward {
        @Override
        public String getDisplayLine() {
            return " §8+§3" + formatNumber(amount) + "§7 Hypixel Experience";
        }
    }

    /**
     * Token reward
     */
    record TokenReward(int amount) implements Reward {
        @Override
        public String getDisplayLine() {
            return " §8+§2" + formatNumber(amount) + " §7SkyWars Tokens";
        }
    }

    /**
     * Opal reward
     */
    record OpalReward(int amount) implements Reward {
        @Override
        public String getDisplayLine() {
            return " §8+§9" + amount + " §7Opal";
        }
    }

    /**
     * Prestige scheme unlock reward
     */
    record PrestigeSchemeReward(String name, String colorCode, int level) implements Reward {
        @Override
        public String getDisplayLine() {
            return " §8+§" + colorCode + "[" + level + "\u272F] §a" + name + " §7Prestige Scheme";
        }
    }

    /**
     * Feature unlock reward (e.g., Angel's Descent, Angel's Brewery)
     */
    record FeatureUnlockReward(String feature) implements Reward {
        @Override
        public String getDisplayLine() {
            return switch (feature) {
                case "ANGELS_DESCENT" -> " §8+§bAccess to the Angel's Descent";
                case "ANGELS_BREWERY" -> " §8+§cAccess to Angel's Brewery";
                default -> " §8+§7" + feature;
            };
        }
    }

    /**
     * Format a number with commas for display
     */
    static String formatNumber(int number) {
        if (number >= 1000) {
            return String.format("%,d", number);
        }
        return String.valueOf(number);
    }

    /**
     * Format XP requirement for display (e.g., 1000 -> "1k", 2500 -> "2.5k")
     */
    static String formatXPRequirement(long xp) {
        if (xp >= 1000) {
            double kValue = xp / 1000.0;
            if (kValue == (int) kValue) {
                return (int) kValue + "k";
            }
            return String.format("%.1fk", kValue).replace(".0k", "k");
        }
        return String.valueOf(xp);
    }
}
