package net.swofty.commons.skyblock.item.reforge;

import lombok.Data;
import net.swofty.commons.YamlFileUtils;
import net.swofty.commons.skyblock.statistics.ItemStatistic;
import net.swofty.commons.skyblock.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;
import org.tinylog.Logger;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.function.BiFunction;

@Data
public class ReforgeLoader {
    private static final File REFORGES_DIR = new File("./configuration/skyblock/reforges");
    private static final Map<String, Reforge> LOADED_REFORGES = new HashMap<>();

    @Data
    public static class ReforgeConfig {
        public String name;
        public String prefix;
        public List<String> applicableTypes;
        public Map<String, ReforgeStatisticData> statistics;
    }

    @Data
    public static class ReforgeStatisticData {
        public @Nullable String expression;
        public @Nullable Map<String, Double> rarityValues;

        // Direct values for each rarity (alternative to expression/rarityValues)
        public @Nullable Double common;
        public @Nullable Double uncommon;
        public @Nullable Double rare;
        public @Nullable Double epic;
        public @Nullable Double legendary;
        public @Nullable Double mythic;
    }

    public static void loadAllReforges() {
        try {
            // Ensure directory exists
            if (!YamlFileUtils.ensureDirectoryExists(REFORGES_DIR)) {
                throw new IOException("Failed to create reforges directory");
            }

            LOADED_REFORGES.clear();

            File[] reforgeFiles = REFORGES_DIR.listFiles((dir, name) -> name.toLowerCase().endsWith(".yml"));
            if (reforgeFiles == null) {
                System.out.println("No reforge files found in " + REFORGES_DIR.getPath());
                return;
            }

            for (File file : reforgeFiles) {
                try {
                    Reforge reforge = loadFromFile(file);
                    if (reforge != null) {
                        LOADED_REFORGES.put(reforge.getName().toLowerCase(), reforge);
                    }
                } catch (Exception e) {
                    Logger.error(e, "Failed to load reforge from file: {}", file.getName());
                }
            }
        } catch (Exception e) {
            Logger.error(e, "Failed to load reforges from directory: {}", REFORGES_DIR.getPath());
        }
    }

    private static Reforge loadFromFile(File file) {
        try {
            Yaml yaml = new Yaml();
            ReforgeConfig config = yaml.loadAs(new FileReader(file), ReforgeConfig.class);

            if (config == null) {
                System.err.println("Failed to parse reforge config from: " + file.getName());
                return null;
            }

            return parseReforge(config);
        } catch (Exception e) {
            Logger.error(e, "Error loading reforge from file: {}", file.getName());
            return null;
        }
    }

    private static Reforge parseReforge(ReforgeConfig config) {
        // Parse applicable types
        Set<ReforgeType> applicableTypes = EnumSet.noneOf(ReforgeType.class);
        if (config.applicableTypes != null) {
            for (String typeStr : config.applicableTypes) {
                try {
                    ReforgeType type = ReforgeType.valueOf(typeStr.toUpperCase());
                    applicableTypes.add(type);
                } catch (IllegalArgumentException e) {
                    System.err.println("Unknown reforge type: " + typeStr + " for reforge: " + config.name);
                }
            }
        }

        // Create the calculation function
        BiFunction<ItemStatistics, Integer, ItemStatistics> calculation = createCalculationFunction(config.statistics);

        return new Reforge(config.name, config.prefix, applicableTypes, calculation);
    }

    private static BiFunction<ItemStatistics, Integer, ItemStatistics> createCalculationFunction(
            Map<String, ReforgeStatisticData> statisticsConfig) {

        return (originalStatistics, level) -> {
            ItemStatistics result = originalStatistics;

            if (statisticsConfig != null) {
                for (Map.Entry<String, ReforgeStatisticData> entry : statisticsConfig.entrySet()) {
                    try {
                        ItemStatistic statistic = ItemStatistic.valueOf(entry.getKey().toUpperCase());
                        ReforgeStatisticData data = entry.getValue();

                        double value = calculateStatisticValue(data, level);
                        result = result.addBase(statistic, value);
                    } catch (IllegalArgumentException e) {
                        System.err.println("Unknown statistic: " + entry.getKey());
                    }
                }
            }

            return result;
        };
    }

    private static double calculateStatisticValue(ReforgeStatisticData data, int level) {
        // If direct rarity values are provided, use them
        if (hasDirectRarityValues(data)) {
            return getDirectRarityValue(data, level);
        }

        // If expression is provided, evaluate it
        if (data.expression != null) {
            return evaluateExpression(data.expression, level, data.rarityValues);
        }

        // If rarityValues map is provided without expression, use direct lookup
        if (data.rarityValues != null) {
            String rarityKey = getRarityKey(level);
            return data.rarityValues.getOrDefault(rarityKey, 0.0);
        }

        return 0.0;
    }

    private static boolean hasDirectRarityValues(ReforgeStatisticData data) {
        return data.common != null || data.uncommon != null || data.rare != null ||
                data.epic != null || data.legendary != null || data.mythic != null;
    }

    private static double getDirectRarityValue(ReforgeStatisticData data, int level) {
        return switch (level) {
            case 1 -> data.common != null ? data.common : 0.0;
            case 2 -> data.uncommon != null ? data.uncommon : 0.0;
            case 3 -> data.rare != null ? data.rare : 0.0;
            case 4 -> data.epic != null ? data.epic : 0.0;
            case 5 -> data.legendary != null ? data.legendary : 0.0;
            case 6, 7, 8 -> data.mythic != null ? data.mythic : 0.0;
            default -> 0.0;
        };
    }

    private static String getRarityKey(int level) {
        return switch (level) {
            case 1 -> "common";
            case 2 -> "uncommon";
            case 3 -> "rare";
            case 4 -> "epic";
            case 5 -> "legendary";
            case 6, 7, 8 -> "mythic";
            default -> "common";
        };
    }

    private static double evaluateExpression(String expression, int level, Map<String, Double> rarityValues) {
        try {
            // Create evaluation context
            Map<String, Double> context = new HashMap<>();
            context.put("level", (double) level);
            context.put("rarity", (double) level);

            // Add rarity-specific values to context
            if (rarityValues != null) {
                context.putAll(rarityValues);

                // Add current rarity value
                String currentRarity = getRarityKey(level);
                if (rarityValues.containsKey(currentRarity)) {
                    context.put("value", rarityValues.get(currentRarity));
                }
            }

            return ReforgeExpressionEvaluator.evaluate(expression, context);
        } catch (Exception e) {
            Logger.error(e, "Failed to evaluate reforge expression '{}' for level {}", expression, level);
            return 0.0;
        }
    }

    public static Reforge getReforge(String name) {
        return LOADED_REFORGES.get(name.toLowerCase());
    }

    public static Collection<Reforge> getAllReforges() {
        return LOADED_REFORGES.values();
    }

    public static List<Reforge> getReforgesForType(ReforgeType type) {
        return LOADED_REFORGES.values().stream()
                .filter(reforge -> reforge.getApplicableTypes().contains(type))
                .toList();
    }

    public static boolean isReforgeApplicable(String reforgeName, ReforgeType type) {
        Reforge reforge = getReforge(reforgeName);
        return reforge != null && reforge.getApplicableTypes().contains(type);
    }

    // Utility method to reload all reforges
    public static void reloadReforges() {
        System.out.println("Reloading all reforges...");
        loadAllReforges();
    }
}