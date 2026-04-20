package net.swofty.type.skyblockgeneric.fishing;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public record TrophyFishDefinition(
    String id,
    String displayName,
    double catchChance,
    List<String> regions,
    int requiredFishingLevel,
    long minimumCastTimeMs,
    @Nullable String requiredRodId,
    @Nullable Double minimumMana,
    @Nullable Double minimumBobberDepth,
    @Nullable Double maximumPlayerDistance,
    boolean requiresStarterRodWithoutEnchantments,
    boolean specialGoldenFish,
    @Nullable String bronzeItemId,
    @Nullable String silverItemId,
    @Nullable String goldItemId,
    @Nullable String diamondItemId
) {
}
