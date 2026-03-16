package net.swofty.type.skyblockgeneric.fishing;

import net.swofty.commons.skyblock.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public record BaitDefinition(
    String itemId,
    String displayName,
    ItemStatistics statistics,
    List<String> lore,
    Map<String, Double> tagBonuses,
    double treasureChanceBonus,
    double treasureQualityBonus,
    double trophyFishChanceBonus,
    double doubleHookChanceBonus,
    List<FishingMedium> mediums,
    @Nullable String texture
) {
}
