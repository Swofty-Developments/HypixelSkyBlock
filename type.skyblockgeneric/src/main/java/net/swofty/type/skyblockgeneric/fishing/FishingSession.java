package net.swofty.type.skyblockgeneric.fishing;

import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public record FishingSession(
    UUID ownerUuid,
    String rodItemId,
    FishingMedium medium,
    @Nullable String baitItemId,
    long castAt,
    long biteReadyAt,
    long biteWindowEndsAt,
    boolean biteReady,
    boolean resolved
) {
    public FishingSession withBiteTiming(long readyAt, long windowEndsAt) {
        return new FishingSession(ownerUuid, rodItemId, medium, baitItemId, castAt, readyAt, windowEndsAt, biteReady, resolved);
    }

    public FishingSession withBiteReady(boolean ready) {
        return new FishingSession(ownerUuid, rodItemId, medium, baitItemId, castAt, biteReadyAt, biteWindowEndsAt, ready, resolved);
    }

    public FishingSession withResolved(boolean nextResolved) {
        return new FishingSession(ownerUuid, rodItemId, medium, baitItemId, castAt, biteReadyAt, biteWindowEndsAt, biteReady, nextResolved);
    }
}
