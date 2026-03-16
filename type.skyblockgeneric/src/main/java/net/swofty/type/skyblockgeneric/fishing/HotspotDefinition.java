package net.swofty.type.skyblockgeneric.fishing;

import net.swofty.commons.skyblock.statistics.ItemStatistics;

import java.util.List;

public record HotspotDefinition(
    String id,
    String displayName,
    List<String> regions,
    FishingMedium medium,
    int maxActive,
    int durationSeconds,
    ItemStatistics buffs,
    List<String> seaCreatureIds,
    List<SpawnPoint> spawnPoints
) {
    public record SpawnPoint(double x, double y, double z, String serverType) {
    }
}
