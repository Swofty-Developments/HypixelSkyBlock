package net.swofty.type.skywarslobby.soulwell;

import net.minestom.server.item.Material;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Represents a Soul Well upgrade definition with its tiers.
 */
public record SoulWellUpgrade(
        String id,
        String name,
        Material material,
        String baseDescription,
        String color,
        List<SoulWellUpgradeTier> tiers
) {
    /**
     * Get the tier for a specific level (1-indexed)
     */
    public @Nullable SoulWellUpgradeTier getTier(int level) {
        for (SoulWellUpgradeTier tier : tiers) {
            if (tier.level() == level) {
                return tier;
            }
        }
        return null;
    }

    /**
     * Get the next tier after the current level
     */
    public @Nullable SoulWellUpgradeTier getNextTier(int currentLevel) {
        return getTier(currentLevel + 1);
    }

    /**
     * Get the maximum level for this upgrade
     */
    public int getMaxLevel() {
        int max = 0;
        for (SoulWellUpgradeTier tier : tiers) {
            if (tier.level() > max) {
                max = tier.level();
            }
        }
        return max;
    }

    /**
     * Check if a level is maxed out
     */
    public boolean isMaxed(int currentLevel) {
        return currentLevel >= getMaxLevel();
    }

    /**
     * Represents a single tier of an upgrade
     */
    public record SoulWellUpgradeTier(
            int level,
            long cost,
            String previousEffect,
            String newEffect,
            String effectDescription,
            int effectValue
    ) {
        /**
         * Format the effect change display line
         */
        public String getEffectChangeLine() {
            return "§7" + previousEffect + "§l\u279C§a" + newEffect + " §7" + effectDescription;
        }

        /**
         * Format the cost display
         */
        public String getCostDisplay() {
            return "§6" + formatNumber(cost);
        }

        private static String formatNumber(long number) {
            if (number >= 1000) {
                return String.format("%,d", number);
            }
            return String.valueOf(number);
        }
    }
}
