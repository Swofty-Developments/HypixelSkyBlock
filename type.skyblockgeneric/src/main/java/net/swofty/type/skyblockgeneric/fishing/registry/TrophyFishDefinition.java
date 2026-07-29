package net.swofty.type.skyblockgeneric.fishing.registry;

import java.util.List;
import org.jetbrains.annotations.Nullable;

public record TrophyFishDefinition(
    String id,
    String displayName,
    double catchChance,
    List<String> regions,
    int requiredFishingLevel,
    long minimumCastTimeMs,
    @Nullable String requiredRodId,
    @Nullable String requiredBaitId,
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
