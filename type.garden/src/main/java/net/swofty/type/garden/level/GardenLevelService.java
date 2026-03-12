package net.swofty.type.garden.level;

import net.swofty.type.garden.config.GardenConfigRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GardenLevelService {
    private Map<String, Object> config = Map.of();
    private List<Map<String, Object>> levels = List.of();

    public void reload() {
        config = GardenConfigRegistry.getConfig("levels.yml");
        levels = GardenConfigRegistry.getMapList(config, "levels");
    }

    public int getMaxLevel() {
        return GardenConfigRegistry.getInt(config, "max_level", 15);
    }

    public int getCropGrowthPerLevelPercent() {
        return GardenConfigRegistry.getInt(config, "crop_growth_per_level_percent", 10);
    }

    public int getSkyBlockXpPerLevel() {
        return GardenConfigRegistry.getInt(config, "skyblock_xp_per_level", 10);
    }

    public int getUnlockedVisitorsForLevel(int level) {
        return findLevel(level)
            .map(entry -> GardenConfigRegistry.getInt(entry, "visitor_unlocks", 0))
            .orElse(0);
    }

    public List<String> getRewardsForLevel(int level) {
        return findLevel(level)
            .map(entry -> GardenConfigRegistry.getList(entry, "reward_summary").stream()
                .map(String::valueOf)
                .toList())
            .orElse(List.of());
    }

    public List<Map<String, Object>> getLevels() {
        return new ArrayList<>(levels);
    }

    public Map<String, Object> getLevel(int level) {
        return findLevel(level).orElse(Map.of());
    }

    private java.util.Optional<Map<String, Object>> findLevel(int level) {
        return levels.stream()
            .filter(entry -> GardenConfigRegistry.getInt(entry, "level", 0) == level)
            .findFirst();
    }
}
