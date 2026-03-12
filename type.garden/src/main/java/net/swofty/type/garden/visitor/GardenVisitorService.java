package net.swofty.type.garden.visitor;

import net.swofty.type.garden.config.GardenConfigRegistry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GardenVisitorService {
    private Map<String, Object> config = Map.of();
    private final Map<String, Object> rarityWeights = new HashMap<>();
    private final Map<Integer, int[]> baseItemsByLevel = new HashMap<>();
    private final Map<String, Double> quantityMultipliers = new HashMap<>();
    private final Map<String, Double> requestRarityMultipliers = new HashMap<>();
    private final Map<String, Double> farmingXpRarityMultipliers = new HashMap<>();
    private final Map<String, Double> itemFarmingXpAmounts = new HashMap<>();
    private final Map<String, Double> gardenXpRarityMultipliers = new HashMap<>();
    private final Map<String, Double> copperRarityMultipliers = new HashMap<>();
    private final Map<Integer, Map<String, Integer>> gardenXpByLevel = new HashMap<>();
    private List<Map<String, Object>> bonusRewards = List.of();
    private List<Map<String, Object>> visitors = List.of();

    public void reload() {
        config = GardenConfigRegistry.getConfig("visitors.yml");
        visitors = GardenConfigRegistry.getMapList(config, "registry");
        bonusRewards = GardenConfigRegistry.getMapList(config, "bonus_rewards");

        rarityWeights.clear();
        rarityWeights.putAll(GardenConfigRegistry.getSection(config, "rarity_weights"));

        quantityMultipliers.clear();
        GardenConfigRegistry.getSection(config, "item_quantity_multipliers")
            .forEach((key, value) -> quantityMultipliers.put(normalizeItemKey(key), Double.parseDouble(String.valueOf(value))));

        requestRarityMultipliers.clear();
        GardenConfigRegistry.getSection(config, "rarity_request_multipliers")
            .forEach((key, value) -> requestRarityMultipliers.put(normalizeItemKey(key), Double.parseDouble(String.valueOf(value))));

        farmingXpRarityMultipliers.clear();
        GardenConfigRegistry.getSection(config, "farming_xp_rarity_multipliers")
            .forEach((key, value) -> farmingXpRarityMultipliers.put(normalizeItemKey(key), Double.parseDouble(String.valueOf(value))));

        itemFarmingXpAmounts.clear();
        GardenConfigRegistry.getSection(config, "item_farming_xp_amounts")
            .forEach((key, value) -> itemFarmingXpAmounts.put(normalizeItemKey(key), Double.parseDouble(String.valueOf(value))));

        copperRarityMultipliers.clear();
        GardenConfigRegistry.getSection(config, "copper_rarity_multipliers")
            .forEach((key, value) -> copperRarityMultipliers.put(normalizeItemKey(key), Double.parseDouble(String.valueOf(value))));

        baseItemsByLevel.clear();
        for (Map<String, Object> entry : GardenConfigRegistry.getMapList(config, "base_item_ranges")) {
            int level = GardenConfigRegistry.getInt(entry, "level", 1);
            baseItemsByLevel.put(level, new int[]{
                GardenConfigRegistry.getInt(entry, "min", 500),
                GardenConfigRegistry.getInt(entry, "max", 1000)
            });
        }

        gardenXpByLevel.clear();
        for (Map<String, Object> entry : GardenConfigRegistry.getMapList(config, "garden_xp_by_level")) {
            int level = GardenConfigRegistry.getInt(entry, "level", 1);
            Map<String, Integer> rarityMap = new HashMap<>();
            rarityMap.put("UNCOMMON", GardenConfigRegistry.getInt(entry, "uncommon", 2));
            rarityMap.put("RARE", GardenConfigRegistry.getInt(entry, "rare", rarityMap.get("UNCOMMON")));
            rarityMap.put("LEGENDARY", GardenConfigRegistry.getInt(entry, "legendary", rarityMap.get("UNCOMMON")));
            rarityMap.put("MYTHIC", GardenConfigRegistry.getInt(entry, "mythic", rarityMap.get("LEGENDARY")));
            rarityMap.put("SPECIAL", GardenConfigRegistry.getInt(entry, "special", rarityMap.get("LEGENDARY")));
            gardenXpByLevel.put(level, rarityMap);
        }
    }

    public int getArrivalMinutes(int servedUniqueVisitors) {
        Map<String, Object> arrivalMinutes = GardenConfigRegistry.getSection(config, "arrival_minutes");
        if (servedUniqueVisitors >= 100) {
            return GardenConfigRegistry.getInt(arrivalMinutes, "one_hundred_unique", 9);
        }
        if (servedUniqueVisitors >= 50) {
            return GardenConfigRegistry.getInt(arrivalMinutes, "fifty_unique", 12);
        }
        return GardenConfigRegistry.getInt(arrivalMinutes, "base", 15);
    }

    public int[] getBaseItemsRange(int gardenLevel) {
        return baseItemsByLevel.getOrDefault(gardenLevel, baseItemsByLevel.getOrDefault(1, new int[]{500, 1000}));
    }

    public double getQuantityMultiplier(String itemId) {
        return quantityMultipliers.getOrDefault(normalizeItemKey(itemId), 1D);
    }

    public double getRequestMultiplier(String rarity) {
        return requestRarityMultipliers.getOrDefault(normalizeItemKey(rarity), 1D);
    }

    public double getItemFarmingXpAmount(String itemId) {
        String normalized = normalizeItemKey(itemId);
        return itemFarmingXpAmounts.getOrDefault(normalized, getQuantityMultiplier(normalized));
    }

    public double calculateFarmingXp(int baseItems, double itemFarmingXp, String rarity) {
        return baseItems * itemFarmingXp * farmingXpRarityMultipliers.getOrDefault(normalizeItemKey(rarity), 0.05D);
    }

    public int calculateGardenXp(int gardenLevel, String rarity) {
        Map<String, Integer> rarityMap = gardenXpByLevel.getOrDefault(gardenLevel, gardenXpByLevel.getOrDefault(1, Map.of()));
        return rarityMap.getOrDefault(normalizeItemKey(rarity), rarityMap.getOrDefault("UNCOMMON", 2));
    }

    public int calculateCopper(int baseItems, double itemQuantityMultiplier, String rarity) {
        double copper = (((double) baseItems / 2000D) / itemQuantityMultiplier)
            * copperRarityMultipliers.getOrDefault(normalizeItemKey(rarity), 1D)
            * 1.3D;
        return Math.max(2, (int) Math.floor(copper));
    }

    public double getRarityWeight(String rarity) {
        Object weight = rarityWeights.get(normalizeItemKey(rarity));
        return weight == null ? 0 : Double.parseDouble(String.valueOf(weight));
    }

    public int getBitsBase() {
        return GardenConfigRegistry.getInt(config, "bits_base", 5);
    }

    public int getMaxVisibleVisitors() {
        return GardenConfigRegistry.getInt(config, "max_visible_visitors", 5);
    }

    public int getMaxQueuedVisitors() {
        return GardenConfigRegistry.getInt(config, "max_queued_visitors", 1);
    }

    public int getExpectedUniqueVisitors() {
        return GardenConfigRegistry.getInt(config, "expected_unique_visitors", 137);
    }

    public int getFarmingSpeedupMultiplier() {
        return GardenConfigRegistry.getInt(config, "farming_speedup_multiplier", 3);
    }

    public List<Map<String, Object>> getVisitors() {
        return visitors;
    }

    public List<Map<String, Object>> getBonusRewards() {
        return bonusRewards;
    }

    public Map<String, Object> getVisitor(String id) {
        return visitors.stream()
            .filter(entry -> GardenConfigRegistry.getString(entry, "id", "").equalsIgnoreCase(id))
            .findFirst()
            .orElse(Map.of());
    }

    private String normalizeItemKey(String key) {
        if (key == null) {
            return "";
        }
        return key.trim().replace(' ', '_').toUpperCase();
    }
}
