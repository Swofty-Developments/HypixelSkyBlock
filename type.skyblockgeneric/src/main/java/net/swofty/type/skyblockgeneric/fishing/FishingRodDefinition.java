package net.swofty.type.skyblockgeneric.fishing;

import net.swofty.commons.skyblock.statistics.ItemStatistics;
import net.swofty.type.skyblockgeneric.enchantment.EnchantmentType;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public record FishingRodDefinition(
    String itemId,
    String displayName,
    @Nullable String subtitle,
    FishingMedium medium,
    int requiredFishingLevel,
    ItemStatistics statistics,
    Map<EnchantmentType, Integer> enchantments,
    List<String> lore,
    @Nullable String legacyConversionTarget,
    @Nullable String legacyConversionPart,
    boolean rodPartsEnabled
) {
}
