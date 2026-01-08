package net.swofty.type.skywarslobby.level;

import lombok.Data;
import net.minestom.server.item.Material;
import net.swofty.commons.YamlFileUtils;
import org.jetbrains.annotations.Nullable;
import org.tinylog.Logger;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Loader for SkyWars level progression configuration from YAML files.
 * Follows the same pattern as SkillLoader.
 */
@Data
public class SkywarsLevelLoader {
    private static final File LEVELS_DIR = new File("./configuration/skywars");

    @Data
    public static class LevelConfig {
        public List<LevelEntry> levels;
    }

    @Data
    public static class LevelEntry {
        public int level;
        public long requirement;
        public boolean isPrestige;
        public @Nullable String prestigeName;
        public @Nullable String prestigeColor;
        public String material;
        public @Nullable String headTexture;
        public List<RewardEntry> rewards;
    }

    @Data
    public static class RewardEntry {
        public String type;
        public Map<String, Object> data;
    }

    /**
     * Load the SkyWars level configuration from the YAML file
     */
    public static SkywarsLevelCategory loadFromFile() {
        try {
            // Ensure directory exists
            if (!YamlFileUtils.ensureDirectoryExists(LEVELS_DIR)) {
                throw new IOException("Failed to create levels directory");
            }

            File levelsFile = new File(LEVELS_DIR, "levels.yml");

            if (!levelsFile.exists()) {
                Logger.warn("SkyWars levels.yml not found at: " + levelsFile.getAbsolutePath());
                return createDefaultCategory();
            }

            // Load the YAML file
            Yaml yaml = new Yaml();
            LevelConfig config = yaml.loadAs(new FileReader(levelsFile), LevelConfig.class);

            if (config == null || config.levels == null) {
                Logger.warn("Invalid SkyWars levels.yml structure");
                return createDefaultCategory();
            }

            return new SkywarsLevelCategory() {
                private final SkywarsLevel[] levels = parseLevels(config.levels);

                @Override
                public SkywarsLevel[] getLevels() {
                    return levels;
                }
            };
        } catch (Exception e) {
            Logger.error(e, "Failed to load SkyWars levels from file");
            return createDefaultCategory();
        }
    }

    /**
     * Parse level entries from YAML config
     */
    private static SkywarsLevelCategory.SkywarsLevel[] parseLevels(List<LevelEntry> entries) {
        List<SkywarsLevelCategory.SkywarsLevel> levels = new ArrayList<>();

        for (LevelEntry entry : entries) {
            levels.add(parseLevel(entry));
        }

        return levels.toArray(new SkywarsLevelCategory.SkywarsLevel[0]);
    }

    /**
     * Parse a single level entry
     */
    private static SkywarsLevelCategory.SkywarsLevel parseLevel(LevelEntry entry) {
        Material material = parseMaterial(entry.material);
        List<SkywarsLevelCategory.Reward> rewards = parseRewards(entry.rewards, entry);

        return new SkywarsLevelCategory.SkywarsLevel(
                entry.level,
                entry.requirement,
                entry.isPrestige,
                entry.prestigeName,
                entry.prestigeColor,
                material,
                entry.headTexture,
                rewards
        );
    }

    /**
     * Parse material string to Material enum
     */
    private static Material parseMaterial(String materialStr) {
        if (materialStr == null) {
            return Material.LIME_STAINED_GLASS_PANE;
        }

        return Material.values().stream()
                .filter(m -> m.key().value().equalsIgnoreCase(materialStr.toLowerCase()))
                .findFirst()
                .orElse(Material.LIME_STAINED_GLASS_PANE);
    }

    /**
     * Parse reward entries
     */
    private static List<SkywarsLevelCategory.Reward> parseRewards(List<RewardEntry> rewardEntries, LevelEntry levelEntry) {
        List<SkywarsLevelCategory.Reward> rewards = new ArrayList<>();

        if (rewardEntries == null) {
            return rewards;
        }

        for (RewardEntry rewardEntry : rewardEntries) {
            SkywarsLevelCategory.Reward reward = parseReward(rewardEntry, levelEntry);
            if (reward != null) {
                rewards.add(reward);
            }
        }

        return rewards;
    }

    /**
     * Parse a single reward entry
     */
    private static SkywarsLevelCategory.Reward parseReward(RewardEntry entry, LevelEntry levelEntry) {
        if (entry.type == null || entry.data == null) {
            return null;
        }

        return switch (entry.type.toUpperCase()) {
            case "COINS" -> {
                Object amountObj = entry.data.get("amount");
                int amount = amountObj instanceof Number n ? n.intValue() : 0;
                yield new SkywarsLevelCategory.CoinReward(amount);
            }
            case "HYPIXEL_XP" -> {
                Object amountObj = entry.data.get("amount");
                int amount = amountObj instanceof Number n ? n.intValue() : 0;
                yield new SkywarsLevelCategory.HypixelXPReward(amount);
            }
            case "TOKENS" -> {
                Object amountObj = entry.data.get("amount");
                int amount = amountObj instanceof Number n ? n.intValue() : 0;
                yield new SkywarsLevelCategory.TokenReward(amount);
            }
            case "OPAL" -> {
                Object amountObj = entry.data.get("amount");
                int amount = amountObj instanceof Number n ? n.intValue() : 0;
                yield new SkywarsLevelCategory.OpalReward(amount);
            }
            case "PRESTIGE_SCHEME" -> {
                String name = (String) entry.data.get("name");
                String colorCode = levelEntry.prestigeColor != null ? levelEntry.prestigeColor : "7";
                yield new SkywarsLevelCategory.PrestigeSchemeReward(name, colorCode, levelEntry.level);
            }
            case "FEATURE_UNLOCK" -> {
                String feature = (String) entry.data.get("feature");
                yield new SkywarsLevelCategory.FeatureUnlockReward(feature);
            }
            default -> {
                Logger.warn("Unknown SkyWars reward type: " + entry.type);
                yield null;
            }
        };
    }

    /**
     * Create a default category with minimal levels for fallback
     */
    private static SkywarsLevelCategory createDefaultCategory() {
        return () -> new SkywarsLevelCategory.SkywarsLevel[]{
                new SkywarsLevelCategory.SkywarsLevel(
                        1, 0, true, "Stone", "7",
                        Material.LIME_STAINED_GLASS_PANE, null,
                        List.of(new SkywarsLevelCategory.PrestigeSchemeReward("Stone", "7", 1))
                )
        };
    }
}
