package net.swofty.type.skywarslobby.soulwell;

import lombok.Data;
import net.minestom.server.item.Material;
import net.swofty.commons.YamlFileUtils;
import org.tinylog.Logger;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Loader for Soul Well upgrade configuration from YAML files.
 */
@Data
public class SoulWellUpgradeLoader {
    private static final File UPGRADES_DIR = new File("./configuration/skywars");

    @Data
    public static class UpgradesConfig {
        public Map<String, UpgradeEntry> upgrades;
    }

    @Data
    public static class UpgradeEntry {
        public String name;
        public String material;
        public String baseDescription;
        public String color;
        public List<TierEntry> tiers;
    }

    @Data
    public static class TierEntry {
        public int level;
        public long cost;
        public String previousEffect;
        public String newEffect;
        public String effectDescription;
        public int effectValue;
    }

    /**
     * Load Soul Well upgrades from the YAML file
     */
    public static List<SoulWellUpgrade> loadFromFile() {
        List<SoulWellUpgrade> upgrades = new ArrayList<>();

        try {
            if (!YamlFileUtils.ensureDirectoryExists(UPGRADES_DIR)) {
                throw new IOException("Failed to create upgrades directory");
            }

            File upgradesFile = new File(UPGRADES_DIR, "soul_well_upgrades.yml");

            if (!upgradesFile.exists()) {
                Logger.warn("Soul Well upgrades file not found at: " + upgradesFile.getAbsolutePath());
                return getDefaultUpgrades();
            }

            Yaml yaml = new Yaml();
            UpgradesConfig config = yaml.loadAs(new FileReader(upgradesFile), UpgradesConfig.class);

            if (config == null || config.upgrades == null) {
                Logger.warn("Invalid soul_well_upgrades.yml structure");
                return getDefaultUpgrades();
            }

            for (Map.Entry<String, UpgradeEntry> entry : config.upgrades.entrySet()) {
                String id = entry.getKey();
                UpgradeEntry upgradeEntry = entry.getValue();

                SoulWellUpgrade upgrade = parseUpgrade(id, upgradeEntry);
                if (upgrade != null) {
                    upgrades.add(upgrade);
                }
            }

            Logger.info("Loaded " + upgrades.size() + " Soul Well upgrades from configuration");

        } catch (Exception e) {
            Logger.error(e, "Failed to load Soul Well upgrades from file");
            return getDefaultUpgrades();
        }

        return upgrades;
    }

    /**
     * Parse a single upgrade entry
     */
    private static SoulWellUpgrade parseUpgrade(String id, UpgradeEntry entry) {
        if (entry == null || entry.name == null) {
            return null;
        }

        Material material = parseMaterial(entry.material);
        List<SoulWellUpgrade.SoulWellUpgradeTier> tiers = parseTiers(entry.tiers);

        return new SoulWellUpgrade(
                id,
                entry.name,
                material,
                entry.baseDescription != null ? entry.baseDescription : "",
                entry.color != null ? entry.color : "a",
                tiers
        );
    }

    /**
     * Parse tiers from YAML config
     */
    private static List<SoulWellUpgrade.SoulWellUpgradeTier> parseTiers(List<TierEntry> tierEntries) {
        List<SoulWellUpgrade.SoulWellUpgradeTier> tiers = new ArrayList<>();

        if (tierEntries == null) {
            return tiers;
        }

        for (TierEntry entry : tierEntries) {
            tiers.add(new SoulWellUpgrade.SoulWellUpgradeTier(
                    entry.level,
                    entry.cost,
                    entry.previousEffect != null ? entry.previousEffect : "0",
                    entry.newEffect != null ? entry.newEffect : "+1",
                    entry.effectDescription != null ? entry.effectDescription : "",
                    entry.effectValue
            ));
        }

        return tiers;
    }

    /**
     * Parse material string to Material enum
     */
    private static Material parseMaterial(String materialStr) {
        if (materialStr == null) {
            return Material.GOLD_INGOT;
        }

        return Material.values().stream()
                .filter(m -> m.key().value().equalsIgnoreCase(materialStr.toLowerCase()))
                .findFirst()
                .orElse(Material.GOLD_INGOT);
    }

    /**
     * Get default upgrades if file loading fails
     */
    private static List<SoulWellUpgrade> getDefaultUpgrades() {
        List<SoulWellUpgrade> defaults = new ArrayList<>();

        // Xezbeth Luck default
        defaults.add(new SoulWellUpgrade(
                "xezbeth_luck",
                "Xezbeth Luck",
                Material.GOLD_INGOT,
                "Wins grant additional Souls",
                "a",
                List.of(
                        new SoulWellUpgrade.SoulWellUpgradeTier(1, 2500, "0", "+1", "Souls per win", 1),
                        new SoulWellUpgrade.SoulWellUpgradeTier(2, 5000, "1", "+2", "Souls per win", 2),
                        new SoulWellUpgrade.SoulWellUpgradeTier(3, 7500, "2", "+3", "Souls per win", 3)
                )
        ));

        // Harvesting Season default
        defaults.add(new SoulWellUpgrade(
                "harvesting_season",
                "Harvesting Season",
                Material.GHAST_TEAR,
                "Chance to earn x2 Souls from a kill or win",
                "a",
                List.of(
                        new SoulWellUpgrade.SoulWellUpgradeTier(1, 50000, "0%", "2%", "chance to earn x2 Souls", 2),
                        new SoulWellUpgrade.SoulWellUpgradeTier(2, 100000, "2%", "4%", "chance to earn x2 Souls", 4),
                        new SoulWellUpgrade.SoulWellUpgradeTier(3, 200000, "4%", "6%", "chance to earn x2 Souls", 6)
                )
        ));

        // Angel of Death default
        defaults.add(new SoulWellUpgrade(
                "angel_of_death",
                "Angel of Death",
                Material.WITHER_SKELETON_SKULL,
                "Corruption Chance - Kills drop player Heads",
                "5",
                List.of(
                        new SoulWellUpgrade.SoulWellUpgradeTier(1, 50000, "0%", "1%", "Corruption Chance", 1),
                        new SoulWellUpgrade.SoulWellUpgradeTier(2, 200000, "1%", "2%", "Corruption Chance", 2),
                        new SoulWellUpgrade.SoulWellUpgradeTier(3, 500000, "2%", "3%", "Corruption Chance", 3)
                )
        ));

        return defaults;
    }
}
