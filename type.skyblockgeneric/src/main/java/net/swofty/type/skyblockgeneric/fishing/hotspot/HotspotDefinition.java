package net.swofty.type.skyblockgeneric.fishing.hotspot;

import java.util.List;
import net.swofty.commons.skyblock.statistics.ItemStatistics;
import net.swofty.type.skyblockgeneric.fishing.FishingMedium;

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
    public record SpawnPoint(
        String serverType,
        double x,
        double y,
        double z
    ) {
    }
}
