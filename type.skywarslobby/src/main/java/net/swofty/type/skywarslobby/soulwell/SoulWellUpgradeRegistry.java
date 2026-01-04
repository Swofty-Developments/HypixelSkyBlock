package net.swofty.type.skywarslobby.soulwell;

import org.jetbrains.annotations.Nullable;
import org.tinylog.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Static registry for Soul Well upgrade definitions.
 * Should be initialized during server startup.
 */
public class SoulWellUpgradeRegistry {
    private static final Map<String, SoulWellUpgrade> upgrades = new HashMap<>();
    private static boolean initialized = false;

    /**
     * Initialize the upgrade registry by loading from YAML
     */
    public static void initialize() {
        if (initialized) {
            Logger.warn("SoulWellUpgradeRegistry already initialized, skipping...");
            return;
        }

        List<SoulWellUpgrade> loadedUpgrades = SoulWellUpgradeLoader.loadFromFile();
        for (SoulWellUpgrade upgrade : loadedUpgrades) {
            upgrades.put(upgrade.id(), upgrade);
        }

        initialized = true;
        Logger.info("Initialized Soul Well Upgrade Registry with " + upgrades.size() + " upgrades");
    }

    /**
     * Check if the registry has been initialized
     */
    public static boolean isInitialized() {
        return initialized;
    }

    /**
     * Get an upgrade by its ID
     */
    public static @Nullable SoulWellUpgrade getUpgrade(String id) {
        ensureInitialized();
        return upgrades.get(id);
    }

    /**
     * Get all upgrades
     */
    public static List<SoulWellUpgrade> getAllUpgrades() {
        ensureInitialized();
        return List.copyOf(upgrades.values());
    }

    /**
     * Get the next tier for an upgrade at the current level
     */
    public static @Nullable SoulWellUpgrade.SoulWellUpgradeTier getNextTier(String upgradeId, int currentLevel) {
        SoulWellUpgrade upgrade = getUpgrade(upgradeId);
        if (upgrade == null) return null;
        return upgrade.getNextTier(currentLevel);
    }

    /**
     * Get the cost for a specific upgrade tier
     */
    public static long getCost(String upgradeId, int level) {
        SoulWellUpgrade upgrade = getUpgrade(upgradeId);
        if (upgrade == null) return 0;

        SoulWellUpgrade.SoulWellUpgradeTier tier = upgrade.getTier(level);
        return tier != null ? tier.cost() : 0;
    }

    /**
     * Check if an upgrade is at max level
     */
    public static boolean isMaxed(String upgradeId, int currentLevel) {
        SoulWellUpgrade upgrade = getUpgrade(upgradeId);
        if (upgrade == null) return true;
        return upgrade.isMaxed(currentLevel);
    }

    /**
     * Get the effect value for a specific upgrade level
     */
    public static int getEffectValue(String upgradeId, int level) {
        SoulWellUpgrade upgrade = getUpgrade(upgradeId);
        if (upgrade == null) return 0;

        SoulWellUpgrade.SoulWellUpgradeTier tier = upgrade.getTier(level);
        return tier != null ? tier.effectValue() : 0;
    }

    /**
     * Ensure the registry is initialized before use
     */
    private static void ensureInitialized() {
        if (!initialized) {
            Logger.warn("SoulWellUpgradeRegistry not initialized, initializing now...");
            initialize();
        }
    }
}
