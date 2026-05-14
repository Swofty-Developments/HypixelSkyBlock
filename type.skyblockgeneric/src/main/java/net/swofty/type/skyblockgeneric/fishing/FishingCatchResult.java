package net.swofty.type.skyblockgeneric.fishing;

import org.jetbrains.annotations.Nullable;

public record FishingCatchResult(
    FishingCatchKind kind,
    @Nullable String itemId,
    @Nullable String seaCreatureId,
    @Nullable String trophyFishId,
    int amount,
    double skillXp,
    boolean corrupted,
    @Nullable String message
) {
}
