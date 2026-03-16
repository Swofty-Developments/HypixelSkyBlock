package net.swofty.type.skyblockgeneric.fishing;

import net.swofty.commons.skyblock.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public record RodPartDefinition(
    String itemId,
    String displayName,
    PartCategory category,
    int requiredFishingLevel,
    ItemStatistics statistics,
    List<String> lore,
    Map<String, Double> tagBonuses,
    boolean treasureOnly,
    boolean bayouTreasureToJunk,
    @Nullable String materializedItemId,
    double materializedChance,
    double baitPreservationChance,
    double hotspotBuffMultiplier,
    @Nullable String texture
) {
    public enum PartCategory {
        HOOK,
        LINE,
        SINKER
    }
}
